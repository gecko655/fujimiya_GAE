package jp.gecko655.fujimiya.bot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

public class FujimiyaLunch extends AbstractCron{

    protected String getTweet(Twitter twitter){
        String tweets[] = {
                "玉子焼き作ってきたの。", //$NON-NLS-1$
                "http://twitpic.com/e5ytxh" //$NON-NLS-1$
        };
        int randint = (int)(Math.random()*tweets.length);
        return tweets[randint];
    }
    
    @Override
    protected void twitterCron(ConfigurationBuilder cb) {
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        //String message = getTweet(twitter);
        try {
            //Twitterに書き出し
           // twitter.updateStatus(message);
            StatusUpdate status =new StatusUpdate(" "); //$NON-NLS-1$
            status.media("fujimiya.jpg", new URL(getFujimiyaUrl("藤宮さん 玉子焼き")).openStream()); //$NON-NLS-1$
            twitter.updateStatus(status);
            logger.log(Level.INFO, "Successfully tweeted"); //$NON-NLS-1$
        } catch (TwitterException e) {
            logger.log(Level.SEVERE, "Twitter error", e); //$NON-NLS-1$
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
