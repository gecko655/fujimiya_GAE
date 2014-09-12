package jp.gecko655.fujimiya.bot;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;

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
