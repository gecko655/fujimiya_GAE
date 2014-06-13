package jp.gecko655.fujimiya.bot;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import twitter4j.Relationship;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class FujimiyaFollow extends AbstractCron {

    public FujimiyaFollow() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void twitterCron(ConfigurationBuilder cb) {
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        try {
            List<Status> replies = twitter.getMentionsTimeline();
            for(Status reply: replies){
                Relationship relation = twitter.friendsFollowers().showFriendship(twitter.getId(), reply.getUser().getId());
                Date now = new Date();
                if(!relation.isSourceFollowingTarget()&&
                    ((now.getTime() - reply.getCreatedAt().getTime())<1000*60*20)
                        ){
                    User user =twitter.createFriendship(reply.getUser().getId());
                    StatusUpdate update= new StatusUpdate("@"+reply.getUser().getScreenName()+" フォローしたよ！");
                    update.setInReplyToStatusId(reply.getId());
                    twitter.updateStatus(update);
                    logger.log(Level.INFO,"Successfully followed back to "+reply.getUser().getScreenName());
                }
            }

        } catch (TwitterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}
