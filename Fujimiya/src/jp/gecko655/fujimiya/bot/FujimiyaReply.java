package jp.gecko655.fujimiya.bot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public FujimiyaReply() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void twitterCron(ConfigurationBuilder cb) {
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        try {
            Pattern pattern = Pattern.compile("(くん|さん|君|ちゃん)$");
            Date now = new Date();
            List<Status> replies = twitter.getMentionsTimeline((new Paging()).count(200));
            for(Status reply: replies){
                Relationship relation = twitter.friendsFollowers().showFriendship(twitter.getId(), reply.getUser().getId());
                if((now.getTime() - reply.getCreatedAt().getTime())<1000*60*10+1000*6){
                	//10 min 6 sec because gae cron sometimes delays up to 5 secs.
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
                        twitter.updateStatus(update);
                        logger.log(Level.INFO,"Successfully followed back to "+reply.getUser().getScreenName());
                    }else{
                    	//auto reply (when fujimiya-san follows the replier)
                        StatusUpdate update= new StatusUpdate("@"+reply.getUser().getScreenName()+" ").media("fujimiya.jpg", new URL(getFujimiyaUrl("藤宮香織 かわいい 一週間フレンズ。",100)).openStream());
                        update.setInReplyToStatusId(reply.getId());
                        twitter.updateStatus(update);
                        logger.log(Level.INFO,"Successfully replied to "+reply.getUser().getScreenName());
                    }
                    Thread.sleep(1000*10);//sleep for 10 secs
                }
            }

        } catch (TwitterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }

}
