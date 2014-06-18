package jp.gecko655.fujimiya.bot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

public class FujimiyaReply extends AbstractCron {

    public FujimiyaReply() {
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
                if((now.getTime() - reply.getCreatedAt().getTime())<1000*60*10+1000*6){
                	//10 min 6 sec because gae cron sometimes delays up to 5 secs.
                    if(!relation.isSourceFollowingTarget()){
                    	//follow back
                        User user =twitter.createFriendship(reply.getUser().getId());
                        StatusUpdate update= new StatusUpdate("@"+reply.getUser().getScreenName()+" もしかして、あなたが"+reply.getUser().getName()+"くん？");
                        update.setInReplyToStatusId(reply.getId());
                        twitter.updateStatus(update);
                        logger.log(Level.INFO,"Successfully followed back to "+reply.getUser().getScreenName());
                    }else{
                        StatusUpdate update= new StatusUpdate("@"+reply.getUser().getScreenName()+" ").media("fujimiya.jpg", new URL(getFujimiyaUrl("藤宮さん かわいい")).openStream());
                        update.setInReplyToStatusId(reply.getId());
                        twitter.updateStatus(update);
                        logger.log(Level.INFO,"Successfully replied to "+reply.getUser().getScreenName());
                    }
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
		}


    }

}
