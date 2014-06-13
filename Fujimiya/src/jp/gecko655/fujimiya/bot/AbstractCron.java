package jp.gecko655.fujimiya.bot;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.conf.ConfigurationBuilder;

public abstract class AbstractCron extends HttpServlet{

    static Logger logger = Logger.getLogger("Fujimiya");
    
    static String consumerKey = "E3Izw5Ye8jUDsm9xcC9XtMWIy";
    static String consumerSecret = "KsA2A4exrK0HCcEzV0KatEViRJL6jKrtcoZMgGOUfHMP0R9q4t";
    
    //For gecko535
    //static String accessToken = "614382034-LMokKg0iljTw2sXQk7xjTPXD89fw6FaVOcfVKbcy";
    //static String accessTokenSecret = "1bW5MajAJMsSd9QW3QjaRC8QmmgbYltCuzaviqs0WcXsL";
    static String accessToken = "2565501103-GKLBwOVXap1GlkK8Ucocsgx9jHwSNvS9Z7zaxdj";
    static String accessTokenSecret = "m8LqF0jf29xzUI4AiW2n588LIKxagfzDKrl7LnjIsy6nF";
    
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        //http://twitter4j.org/ja/configuration.html
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret)
            .setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret);
        twitterCron(cb);
    }
    
    abstract protected void twitterCron(ConfigurationBuilder cb);

    public AbstractCron() {
        // TODO Auto-generated constructor stub
    }

}
