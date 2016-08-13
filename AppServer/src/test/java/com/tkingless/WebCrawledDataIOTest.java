package com.tkingless;

import com.mongodb.*;
import com.mongodb.client.*;
import com.tkingless.utils.DateTimeEntity;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static com.tkingless.MongoDBparam.*;

/**
 * Created by tsangkk on 8/10/16.
 */
public class WebCrawledDataIOTest {

    String DBaddr = TestDBAddr;
    int DBport = TestDBport;
    String TestDBname = webCrawlingTestDB;
    String TestCollname = "testCollWebCrawling";

    MongoClient client;

    MongoDatabase tmpProdDB;

    @Before
    public void init() throws Exception {
        MongoClientOptions.Builder o = MongoClientOptions.builder().connectTimeout(3000);
        client = new MongoClient(new ServerAddress(DBaddr, DBport));

        try {
            client.getAddress();
            tmpProdDB = client.getDatabase(webCrawlingDB);
        } catch (Exception e) {
            System.out.println("Mongo is down");
            client.close();
            return;
        }
    }

    List<Integer> onMatchingIds = new ArrayList<>();
    List<Integer> lostMatchingIds = new ArrayList<>();

    @Test
    public void GetConsideredWorkersByTime() throws Exception {

        long threshold = (new Date()).getTime() - 1000 * 60 * 60 * 96;
        DateTimeEntity timeAfterToConsider = new DateTimeEntity(threshold);

        FindIterable<Document> consideredIds = tmpProdDB.getCollection("MatchEvents").find(
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
                        onMatchingIds.add(document.getInteger("MatchId"));
                    } else {
                        lostMatchingIds.add(document.getInteger("MatchId"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        System.out.println("onMatchingIds : " + onMatchingIds.toString());
        System.out.println("lostMatchingIds : " + lostMatchingIds.toString());

    }

    @Test
    public void importDBcollections() throws Exception {
        GetConsideredWorkersByTime();

        MongoCollection WCDIO = tmpProdDB.getCollection("WCDIOcsv");

        for(Integer id : onMatchingIds){
           Document doc = (Document) WCDIO.find(new Document("MatchId", id)).first();

            if(doc == null){
                System.out.println("This onMatchingIds not existing, adding new csv object");
                Document initDoc = new Document("MatchId",id);

            MongoCursor<Document> idOddsCursor = tmpProdDB.getCollection("AllOdds").find(new Document("MatchId",id)).iterator();

                if (idOddsCursor.hasNext()){
                    System.out.println("there is odd update for id:" + id);
                    //doSomethingCrazyHere()
                }


            }
        }
    }

    @Test
    public void DoSomethingCrazy() throws Exception {

    }
}
