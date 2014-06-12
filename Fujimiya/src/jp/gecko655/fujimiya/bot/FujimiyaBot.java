package jp.gecko655.fujimiya.bot;

import java.util.logging.Level;
import java.util.logging.Logger;


import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class FujimiyaBot extends AbstractCron{

    private String getTweet(){
        String tweets[] = {
                "玉子焼き作ってきたの。",
                "http://twitpic.com/e5ytxh"
        };
        int randint = (int)(Math.random()*tweets.length);
        return tweets[randint];
    }
    
    @Override
    protected void twitterCron(ConfigurationBuilder cb) {
        String message = getTweet();
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        try {
            //Twitterに書き出し
            twitter.updateStatus(message);
            logger.log(Level.SEVERE, "Successfully tweeted");
        } catch (TwitterException e) {
            logger.log(Level.SEVERE, "Twitter error", e);
        }
        
    }

}
