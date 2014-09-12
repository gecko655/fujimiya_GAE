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

public class FujimiyaBot extends AbstractCron{


    protected String getTweet(Twitter twitter){
        String tweets[] = {
                "玉子焼き作ってきたの。", //$NON-NLS-1$
                "http://twitpic.com/e5ytxh" //$NON-NLS-1$
        };
        int randint = (int)(Math.random()*tweets.length);
        return tweets[randint];
    }
    
    @Override
    protected void twitterCron() {
        //String message = getTweet(twitter);
        //Twitterに書き出し
       // twitter.updateStatus(message);
        StatusUpdate status =new StatusUpdate(" "); //$NON-NLS-1$
        if(((int) (Math.random()*10))==1){//10%
            updateStatusWithMedia(status, "山岸さん 一週間フレンズ。", 30);
        }else{
            updateStatusWithMedia(status, "藤宮さん 一週間フレンズ。", 100);
        }
        
    }

}
