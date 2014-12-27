package jp.gecko655.fujimiya.bot;


import java.text.DateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.regex.Pattern;

import twitter4j.Paging;
import twitter4j.Relationship;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class FujimiyaReply extends AbstractCron {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static final String KEY = "LastTimeStatus";
    static final DateFormat format = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
    private static final Pattern keishouPattern = Pattern.compile("(くん|さん|君|ちゃん)$");
    private static final Pattern whoPattern = Pattern.compile("(誰だ[^と]|だれだ[^と])");

    public FujimiyaReply() {
        format.setTimeZone(TimeZone.getDefault());
    }

    @Override
    protected void twitterCron() {
        MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
        try {
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
                    followBack(reply);
                }else if(whoPattern.matcher(reply.getText()).find()){
                    //TODO make black list.
                }else{
                    //auto reply (when fujimiya-san follows the replier)
                    
                    StatusUpdate update= new StatusUpdate("@"+reply.getUser().getScreenName()+" ");
                    update.setInReplyToStatusId(reply.getId());
                    updateStatusWithMedia(update, "藤宮香織 かわいい 一週間フレンズ。",100);
                }
            }
        } catch (TwitterException e) {
            logger.log(Level.WARNING,e.toString());
            e.printStackTrace();
		}


    }

    private void followBack(Status reply) throws TwitterException {
        twitter.createFriendship(reply.getUser().getId());
        String userName = reply.getUser().getName();
        if(keishouPattern.matcher(userName).find()){
        }else{
            userName = userName + "くん";
        }
        StatusUpdate update= new StatusUpdate("@"+reply.getUser().getScreenName()+" もしかして、あなたが"+userName+"？");
        update.setInReplyToStatusId(reply.getId());
        twitter.updateStatus(update);
        
    }

}
