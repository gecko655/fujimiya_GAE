package jp.gecko655.fujimiya.bot;

import java.util.List;

import twitter4j.PagableResponseList;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class FujimiyaRemove extends AbstractCron {

    public FujimiyaRemove() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void twitterCron(ConfigurationBuilder cb) {
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        try {
            long cursor = -1L;
            int friendsCount= twitter.verifyCredentials().getFriendsCount();
            if(friendsCount==0){
                return;
            }
            while(cursor!=0L){
                PagableResponseList<User> followers = twitter.getFriendsList(twitter.getId(), cursor);
                for(User follower: followers){
                    twitter.destroyFriendship(follower.getId());
                    twitter.updateStatus(new StatusUpdate("@"+follower.getScreenName()+" あなた、誰？"));
                    Thread.sleep(5*60*1000/friendsCount);
                }
                cursor = followers.getNextCursor();
            }
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TwitterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
