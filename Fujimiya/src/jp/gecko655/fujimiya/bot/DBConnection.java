package jp.gecko655.fujimiya.bot;

import java.util.logging.Level;
import java.util.logging.Logger;

import twitter4j.Status;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class DBConnection {

    static final String DSLastStatusKEY = "LastTimeStatus";
    static DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    static MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();

    static Logger logger = Logger.getLogger("Fujimiya"); //$NON-NLS-1$


    public static void storeImageUrl(Status succeededStatus, FetchedImage fetchedImage) {
        Entity imageUrlEntity = new Entity("ImageUrl",succeededStatus.getId());
        imageUrlEntity.setProperty("URL",fetchedImage.getUrl());
        ds.put(imageUrlEntity);
    }

    public static boolean storeImageUrlToBlackList(Status reply){
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey("ImageUrl", reply.getInReplyToStatusId());
        try {
            Entity entity = ds.get(key);
            String url = (String)entity.getProperty("URL");
            Entity notFujimiya = new Entity("NotFujimiya",url);
            ds.put(notFujimiya);
            return true;
        } catch (EntityNotFoundException e) {
            logger.log(Level.WARNING,"Image URL was not found in datastore");
            logger.log(Level.WARNING,e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isInBlackList(String url) {
        try {
            ds.get(KeyFactory.createKey("NotFujimiya", url));
        } catch (EntityNotFoundException e) {
            return false;//There isn't the url in blacklist.
        }
        return true;//There is the url in blacklist.
    }
    
    public static Status getLastStatus(){
        return (Status) memcache.get(DSLastStatusKEY);
    }

    public static void setLastStatus(Status status){
        memcache.put(DSLastStatusKEY, status);

        
    }

}
