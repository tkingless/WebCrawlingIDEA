package com.tkingless;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by tsangkk on 8/17/16.
 */
public class WCDIOcsvOut {

    private MongoDatabase db;
    private MongoCollection<Document> MatchEventsColl, WCDIOcsvColl;

    public WCDIOcsvOut() {
        try {
            db = DBManager.getInstance().getClient().getDatabase(MongoDBparam.webCrawlingDB);
            MatchEventsColl = db.getCollection("MatchEvents");
            WCDIOcsvColl = db.getCollection("WCDIOcsv");
        } catch (Exception e){
            WebCrawledDataIO.logger.error("WCDIOcsvIn init error", e);
        }
    }

    public void run(){

    }
}
