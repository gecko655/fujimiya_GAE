package jp.gecko655.fujimiya.bot;

import java.io.IOException;
import java.net.MalformedURLException;
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

import twitter4j.conf.ConfigurationBuilder;

public abstract class AbstractCron extends HttpServlet{

    static Logger logger = Logger.getLogger("Fujimiya"); //$NON-NLS-1$
    
    static String consumerKey = Messages.getString("AbstractCron.consumerKey"); //$NON-NLS-1$
    static String consumerSecret = Messages.getString("AbstractCron.consumerSecret"); //$NON-NLS-1$
    static String accessToken = Messages.getString("AbstractCron.accessToken"); //$NON-NLS-1$
    static String accessTokenSecret = Messages.getString("AbstractCron.accessTokenSecret"); //$NON-NLS-1$
    
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
        twitterCron(cb);
    }

    /**
     * Search fujimiya-san's image and return the url.
     * The return is randomly picked up from the 100 result of google image search.
     * @param query
     * @return
     */
    protected String getFujimiyaUrl(String query){
    	return getFujimiyaUrl(query,100);
    }
    /**
     * Search fujimiya-san's image and return the url.
     * The return is randomly picked up from the maxRankOfResult result of google image search.
     * @param query
     * @param maxRankOfResult
     * @return
     */
    protected String getFujimiyaUrl(String query,int maxRankOfResult){
        try{
            //Get SearchResult
            Customsearch.Builder builder = new Customsearch.Builder(new NetHttpTransport(), new JacksonFactory(), null).setApplicationName("Google"); //$NON-NLS-1$
            Customsearch search = builder.build();
            Customsearch.Cse.List list = search.cse().list(query); //$NON-NLS-1$
            
            list.setCx(Messages.getString("AbstractCron.cx")); //$NON-NLS-1$
            list.setKey(Messages.getString("AbstractCron.key")); //$NON-NLS-1$
            list.setSearchType("image"); //$NON-NLS-1$
            list.setNum(1L);
            list.setImgSize("huge").setImgSize("large").setImgSize("medium").setImgSize("xlarge").setImgSize("xxlarge");
            long rand = (long)(Math.random()*maxRankOfResult+1);
            list.setStart(rand);
            Search results = list.execute();
            List<Result> items = results.getItems();
            Result result = items.get(0);
            logger.log(Level.INFO,"query: " + query+" rand :"+rand + " URL: "+result.getLink());
            logger.log(Level.INFO,"page URL: "+result.getImage().getContextLink());
            return result.getLink();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
}
    
    abstract protected void twitterCron(ConfigurationBuilder cb);


}
