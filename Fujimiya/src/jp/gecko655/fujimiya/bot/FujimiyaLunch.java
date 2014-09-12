package jp.gecko655.fujimiya.bot;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;

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
    protected void twitterCron() {
        //String message = getTweet(twitter);
        //Twitterに書き出し
        // twitter.updateStatus(message);
        StatusUpdate status =new StatusUpdate(" "); //$NON-NLS-1$
        updateStatusWithMedia(status, "藤宮さん 昼", 10);//$NON-NLS-1$
        
    }

}
