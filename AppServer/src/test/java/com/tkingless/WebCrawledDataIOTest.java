package com.tkingless;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.tkingless.DBobject.MatchEventDAO;
import com.tkingless.WebCrawling.DBobject.APoolOddsData;
import com.tkingless.WebCrawling.DBobject.DateValuePair;
import com.tkingless.WebCrawling.DBobject.MatchEventData;
import com.tkingless.utils.DateTimeEntity;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static com.tkingless.MongoDBparam.*;
import static java.util.Arrays.asList;

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

    List<Integer> launchedMatchIds = new ArrayList<>();
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
                        launchedMatchIds.add(document.getInteger("MatchId"));
                    } else {
                        lostMatchingIds.add(document.getInteger("MatchId"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        System.out.println("launchedMatchIds : " + launchedMatchIds.toString());
        System.out.println("lostMatchingIds : " + lostMatchingIds.toString());

    }

    @Test
    public void InitWCDIOcsv() throws Exception {
        GetConsideredWorkersByTime();

        MongoCollection WCDIO = tmpProdDB.getCollection("WCDIOcsv");

        for(Integer id : launchedMatchIds){
           Document doc = (Document) WCDIO.find(new Document("MatchId", id)).first();

            if(doc == null){
                System.out.println("This launchedMatchIds not existing, adding new csv object");
                Document initDoc = new Document("MatchId",id);

            MongoCursor<Document> idOddsCursor = tmpProdDB.getCollection("AllOdds").find(new Document("MatchId",id)).iterator();

                if (idOddsCursor.hasNext()){
                    System.out.println("there is odd update for id:" + id);
                    DoSomethingCrazy(id, tmpProdDB.getCollection("MatchEvents") ,tmpProdDB.getCollection("AllOdds"));
                    //doSomethingCrazyHere()
                }


            }
        }
    }


    public void DoSomethingCrazy(Integer id, MongoCollection matchColl, MongoCollection oddsColl) throws Exception {

        List<DateDocumentObj> updateTimeOrders = new ArrayList<>();

        //scoreHistory streaming in
        matchColl.find(new Document("MatchId",id)).projection(new Document("scoreUpdates",1)).forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {

                for(DateValuePair dvp : (List<DateValuePair>) document.get("scoreUpdates")){
                    //updateTimeOrders.add( dvp.getTime(), document, DateDocumentObj.HistoryType.UPDATE_SCORE));
                }
            }
        });
        //stageHistory
        matchColl.find(new Document("MatchId",id)).projection(new Document("stageUpdates",1)).forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                //updateTimeOrders.add(new DateDocumentObj(document.getObjectId("_id").getDate(),document));
            }
        });
        //oddsHistory
        oddsColl.find(new Document("MatchId",id)).forEach(new Block<Document>(){
            @Override
            public void apply(Document document){
                if(document.getString("type").equals("HAD")) {
                    updateTimeOrders.add(new DateDocumentObj(document.getDate("recorded"), document, DateDocumentObj.HistoryType.UPDATE_HAD_ODD));
                }else if(document.getString("type").equals("CHL")) {
                    updateTimeOrders.add(new DateDocumentObj(document.getDate("recorded"), document, DateDocumentObj.HistoryType.UPDATe_CHL_ODD));
                }
            }
        });

        DateDocumentObj.SortByAscendOrder(updateTimeOrders);

        for(DateDocumentObj Adata : updateTimeOrders){
            System.out.println(Adata.getDate());
            System.out.println(Adata.getDoc().keySet());
        }

        //UpdateOptions updateOpts = new UpdateOptions().upsert(true);
        //Bson filter = Filters.eq("MatchId", id);
    }

    @Test
    public void LoopArrayObject() throws Exception {
       // Document projectedDoc = tmpProdDB.getCollection("MatchEvents").find(new Document("MatchId",104971)).first();

        //System.out.println("Doc" + projectedDoc);

        //System.out.println( projectedDoc.get("scoreUpdates") );

       /* List<DateValuePair> dvps = (List<DateValuePair>) projectedDoc.get("scoreUpdates");

        for(DateValuePair dvp : dvps) {
            System.out.println("dvp key: " + dvp.getTime());
            System.out.println("dvp value: " + dvp.getVal());
        }*/


        MatchEventDAO dao = new MatchEventDAO(DBManager.getInstance().getClient(),DBManager.getInstance().getMorphia());
        MatchEventData data = dao.findByMatchId("104971");
        List<DateValuePair> scoreHistory = data.getScoreUpdates();

        System.out.println("[Data] " + data.toString());

        for (DateValuePair dvp : scoreHistory){
            System.out.println("dvp key: " + dvp.getTime());
            System.out.println("dvp value: " + dvp.getVal());
        }

       // projectedDoc.get()
    }
}
