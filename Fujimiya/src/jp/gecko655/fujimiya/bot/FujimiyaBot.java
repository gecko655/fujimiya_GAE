package jp.gecko655.fujimiya.bot;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class FujimiyaBot extends HttpServlet{

    static Logger logger = Logger.getLogger("Fujimiya");
    
    static String consumerKey = "E3Izw5Ye8jUDsm9xcC9XtMWIy";
    static String consumerSecret = "KsA2A4exrK0HCcEzV0KatEViRJL6jKrtcoZMgGOUfHMP0R9q4t";
    
    static String accessToken = "614382034-LMokKg0iljTw2sXQk7xjTPXD89fw6FaVOcfVKbcy";
    static String accessTokenSecret = "1bW5MajAJMsSd9QW3QjaRC8QmmgbYltCuzaviqs0WcXsL";
    
    private String getTweet(){
        String tweets[] = {
                "玉子焼き作ってきたの。"
        };
        int randint = (int)(Math.random()*tweets.length);
        return tweets[randint];
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        //http://twitter4j.org/ja/configuration.html
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret)
            .setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret);
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
    public FujimiyaBot() {
        // TODO Auto-generated constructor stub
    }

}
