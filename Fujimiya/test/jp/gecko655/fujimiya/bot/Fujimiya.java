package jp.gecko655.fujimiya.bot;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Fujimiya {

    @Test
    public void test() {
        FujimiyaBot bot = new FujimiyaBot();
        bot.getFujimiyaUrl("藤宮さん");
        //System.out.println(tweet);
    }
    
    @Test
    public void test2(){
        AbstractCron ac = new AbstractCron(){

            @Override
            protected void twitterCron() {
                try {
                    ResponseList<Status> list = twitter.getMentionsTimeline();
                    for(Status s :list){
                        System.out.println(s.getUser().getName());
                    }
                } catch (TwitterException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        };
        try {
            ac.doGet(null, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
