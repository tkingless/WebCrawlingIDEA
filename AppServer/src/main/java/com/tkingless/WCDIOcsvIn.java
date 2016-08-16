package com.tkingless;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.tkingless.utils.DateTimeEntity;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tsangkk on 8/16/16.
 */
public class WCDIOcsvIn extends Thread{

    private static WCDIOcsvIn instance = null;

    public static WCDIOcsvIn GetInstance() {

        if (instance == null) {
            instance = new WCDIOcsvIn();
        }

        return instance;
    }

    private MongoDatabase db;

    public WCDIOcsvIn() {
        try {
            db = DBManager.getInstance().getClient().getDatabase(MongoDBparam.webCrawlingDB);
        } catch (Exception e){
            WebCrawledDataIO.logger.error("WCDIOcsvIn init error", e);
        }
    }

    public void run(){

    }

    void GetConsideredWorkersByTime() {

        long threshold = (new Date()).getTime() - 1000 * 60 * 60 * 96;
        DateTimeEntity timeAfterToConsider = new DateTimeEntity(threshold);

        List<Integer> launchedMatchIds = new ArrayList<>();
        List<Integer> lostMatchingIds = new ArrayList<>();

        FindIterable<Document> consideredIds = db.getCollection("MatchEvents").find(
                //Multiple criteria
                new Document("actualCommence", new Document("$exists", true)).append(
                        "actualCommence", new Document("$gte", timeAfterToConsider.GetTheInstant())).append(
                        "scoreUpdates", new Document("$exists", true)).append(
                        "stageUpdates", new Document("$exists", true))
        ).projection(new Document("MatchId", 1).append("scoreUpdates", 1).append("stageUpdates", 1).append("actualCommence", 1).append("endTime", 1))
                .sort(new Document("actualCommence",-1))
                .limit(20);

        consideredIds.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
                //if(document.keySet().containsAll(Arrays.asList("scoreUpdates","stageUpdates"))){

                try {

                    Date docCommenceDate = (Date) document.get("actualCommence");
                    DateTimeEntity docCommence = new DateTimeEntity(docCommenceDate.getTime());

                    if (docCommence.CalTimeIntervalDiff(timeAfterToConsider) >= 0) {
                        launchedMatchIds.add(document.getInteger("MatchId"));
                    } else {
                        lostMatchingIds.add(document.getInteger("MatchId"));
                    }

                } catch (Exception e) {
                    WebCrawledDataIO.logger.error("Considered id error",e);
                }

            }
        });
        WebCrawledDataIO.logger.info("launchedMatchIds : " + launchedMatchIds.toString());
        WebCrawledDataIO.logger.info("lostMatchingIds : " + lostMatchingIds.toString());
    }
}
