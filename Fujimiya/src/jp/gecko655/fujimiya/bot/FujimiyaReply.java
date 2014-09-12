package jp.gecko655.fujimiya.bot;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import twitter4j.Paging;
import twitter4j.Relationship;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class FujimiyaReply extends AbstractCron {
    
    static final String KEY = "LastTimeStatus";
    static final DateFormat format = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);

    public FujimiyaReply() {
        format.setTimeZone(TimeZone.getDefault());
    }

    @Override
    protected void twitterCron(ConfigurationBuilder cb) {
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
        try {
            Pattern pattern = Pattern.compile("(くん|さん|君|ちゃん)$");
            Status lastStatus = (Status)memcache.get(KEY);
            List<Status> replies = twitter.getMentionsTimeline((new Paging()).count(20));
            memcache.put(KEY, replies.get(0));
            for(Status reply: replies){
                if(lastStatus == null){
                    logger.log(Level.INFO,"memcache saved"+reply.getUser().getName()+"'s tweet at "+format.format(reply.getCreatedAt()));
                    return;
                }else if(reply.getCreatedAt().getTime()-lastStatus.getCreatedAt().getTime()<=0){
                    logger.log(Level.INFO, reply.getUser().getName()+"'s tweet at "+format.format(reply.getCreatedAt()) +" is out of date");
                    return;
                }
                Relationship relation = twitter.friendsFollowers().showFriendship(twitter.getId(), reply.getUser().getId());
                if(!relation.isSourceFollowingTarget()){
                    //follow back
                    twitter.createFriendship(reply.getUser().getId());
                    String userName = reply.getUser().getName();
                    if(pattern.matcher(userName).find()){
                    }else{
                        userName = userName + "くん";
                    }
                    StatusUpdate update= new StatusUpdate("@"+reply.getUser().getScreenName()+" もしかして、あなたが"+userName+"？");
                    update.setInReplyToStatusId(reply.getId());
                }else{
                    //auto reply (when fujimiya-san follows the replier)
                    
                    Status succeededStatus = null;
                    while(succeededStatus==null){
                        try{
                            StatusUpdate update= new StatusUpdate("@"+reply.getUser().getScreenName()+" ").media("fujimiya.jpg", getFujimiyaUrl("藤宮香織 かわいい 一週間フレンズ。",100));
                            update.setInReplyToStatusId(reply.getId());
                            succeededStatus = twitter.updateStatus(update);
                            logger.log(Level.INFO,"Successfully replied to "+reply.getUser().getScreenName());
                        }catch(TwitterException e){
                            logger.log(Level.INFO,"Reply failed. try again. "+ e.getErrorMessage());
                        }
                    }
                }
                Thread.sleep(1000*10);//sleep for 10 secs
            }
        } catch (TwitterException e) {
            logger.log(Level.WARNING,e.toString());
            e.printStackTrace();
		} catch (InterruptedException e) {
            logger.log(Level.WARNING,e.toString());
			e.printStackTrace();
		}


    }

}
