package jp.gecko655.fujimiya.bot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public abstract class AbstractCron extends HttpServlet{

    static Logger logger = Logger.getLogger("Fujimiya"); //$NON-NLS-1$
    
    static String consumerKey = Messages.getString("AbstractCron.consumerKey"); //$NON-NLS-1$
    static String consumerSecret = Messages.getString("AbstractCron.consumerSecret"); //$NON-NLS-1$
    static String accessToken = Messages.getString("AbstractCron.accessToken"); //$NON-NLS-1$
    static String accessTokenSecret = Messages.getString("AbstractCron.accessTokenSecret"); //$NON-NLS-1$

    static Twitter twitter;
    static Customsearch.Builder builder = new Customsearch.Builder(new NetHttpTransport(), new JacksonFactory(), null).setApplicationName("Google"); //$NON-NLS-1$
    static Customsearch search = builder.build();
    
    public AbstractCron() {
        logger.setLevel(Level.FINE);
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
        twitter = new TwitterFactory(cb.build()).getInstance();
        twitterCron();
    }

    /**
     * Search fujimiya-san's image and return the url.
     * The return is randomly picked up from the 100 result of google image search.
     * @param query
     * @return
     */
    protected InputStream getFujimiyaUrl(String query){
    	return getFujimiyaUrl(query,100);
    }
    /**
     * Search fujimiya-san's image and return the url.
     * The return is randomly picked up from the maxRankOfResult result of google image search.
     * @param query
     * @param maxRankOfResult
     * @return
     */
    protected InputStream getFujimiyaUrl(String query,int maxRankOfResult){
        try{
            //Get SearchResult
            Customsearch.Cse.List list = search.cse().list(query); //$NON-NLS-1$
            
            list.setCx(Messages.getString("AbstractCron.cx")); //$NON-NLS-1$
            list.setKey(Messages.getString("AbstractCron.key")); //$NON-NLS-1$
            list.setSearchType("image"); //$NON-NLS-1$
            list.setNum(10L);
            list.setImgSize("huge").setImgSize("large").setImgSize("medium").setImgSize("xlarge").setImgSize("xxlarge");
            Search results = null;
            while(results==null){
                try{
                    long rand = (long)(Math.random()*maxRankOfResult+1);
                    list.setStart(rand);
                    logger.log(Level.INFO,"rand: "+rand);
                    results = list.execute();
                }catch(IOException e){
                }
            }
            List<Result> items = results.getItems();
            HttpURLConnection connection = null;
            for(int i=0;(connection==null||connection.getResponseCode()!=200)&&i<10;i++){
                Result result = items.get(i);
                logger.log(Level.INFO,"query: " + query + " URL: "+result.getLink());
                logger.log(Level.INFO,"page URL: "+result.getImage().getContextLink());
                connection = (HttpURLConnection)(new URL(result.getLink())).openConnection();
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(false);
                connection.connect();
            }
            InputStream in = connection.getInputStream();

            return in;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            logger.log(Level.SEVERE,e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.log(Level.SEVERE,e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.log(Level.SEVERE,e.toString());
            e.printStackTrace();
        }
        return null;
}
    
    protected void updateStatusWithMedia(StatusUpdate update, String query, int maxRankOfResult){
                    Status succeededStatus = null;
                    while(succeededStatus==null){
                        try{
                            update.media("fujimiya.jpg",getFujimiyaUrl(query,maxRankOfResult));
                            succeededStatus = twitter.updateStatus(update);
                            logger.log(Level.INFO,"Successfully tweeted: "+succeededStatus.getText());
                        }catch(TwitterException e){
                            logger.log(Level.INFO,"updateStatusWithMedia failed. try again. "+ e.getErrorMessage());
                        }
                    }
        
    }
    
    abstract protected void twitterCron();


}
