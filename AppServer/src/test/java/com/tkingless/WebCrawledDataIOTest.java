package com.tkingless;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.tkingless.DBobject.MatchEventDAO;
import com.tkingless.WebCrawling.DBobject.InPlayAttrUpdates;
import com.tkingless.WebCrawling.DBobject.MatchEventData;
import com.tkingless.utils.DateTimeEntity;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.*;

import static com.tkingless.MongoDBparam.*;

/**
 * Created by tsangkk on 8/10/16.
 */
public class WebCrawledDataIOTest {

    String DBaddr = TestDBAddr;
    int DBport = TestDBport;
    String TestDBname = webCrawlingDB;
    String TestCollname = "testCollWebCrawling";

    MongoClient client;

    MongoDatabase DB;
    Morphia morphia;
    Datastore datastore;

    Date now = new Date();

    @Before
    public void init() throws Exception {
        MongoClientOptions.Builder o = MongoClientOptions.builder().connectTimeout(3000);
        client = new MongoClient(new ServerAddress(DBaddr, DBport));

        try {
            client.getAddress();

            morphia = new Morphia();
            morphia.mapPackage("com.tkingless.WebCrawling.DBobject");

            datastore = morphia.createDatastore(client, TestDBname);
            datastore.ensureIndexes();
            DB = client.getDatabase(TestDBname);
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

        FindIterable<Document> consideredIds = DB.getCollection("MatchEvents").find(
                //Multiple criteria
                new Document("actualCommence", new Document("$exists", true)).append(
                        "actualCommence", new Document("$gte", timeAfterToConsider.GetTheInstant())).append(
                        "scoreUpdates", new Document("$exists", true)).append(
                        "stageUpdates", new Document("$exists", true))
        ).projection(new Document("MatchId", 1).append("scoreUpdates", 1).append("stageUpdates", 1).append("actualCommence", 1).append("endTime", 1))
                .sort(new Document("actualCommence", -1))
                .limit(20);

        consideredIds.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                //System.out.println(document.toJson());
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

        //TODO now should be the time from fixedrateexecutor
        now = new Date();

        for (Integer id : launchedMatchIds) {

                MongoCursor<Document> idOddsCursor = DB.getCollection("InPlayOddsUpdates").find(new Document("MatchId", id)).iterator();

                if (idOddsCursor.hasNext()) {
                    System.out.println("there is odd update for id:" + id);
                    List<DateDocumentObj> updateHistory = GetUpdateHistory(id, DB.getCollection("InPlayAttrUpdates"), DB.getCollection("InPlayOddsUpdates"));

                    if(!updateHistory.isEmpty()){
                        FormDataField(id,null,updateHistory);
                    }
                }
        }
    }


    public List<DateDocumentObj> GetUpdateHistory(Integer id, MongoCollection matchAttrColl, MongoCollection oddsColl) throws Exception {

        List<DateDocumentObj> updateTimeOrders = new ArrayList<>();

        //Match inPlay attr streaming in
        matchAttrColl.find(new Document("MatchId", id)).forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                updateTimeOrders.add(new DateDocumentObj(document.getDate("time"), document));
            }
        });

        //oddsHistory
        oddsColl.find(new Document("MatchId", id)).forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                updateTimeOrders.add(new DateDocumentObj(document.getDate("recorded"), document));
            }
        });

        DateDocumentObj.SortByAscendOrder(updateTimeOrders);

        for (DateDocumentObj Adata : updateTimeOrders) {
            System.out.println(Adata.getDate());
            System.out.println(Adata.getDoc().getString("type"));
        }

        return updateTimeOrders;
    }

    public void FormDataField(Integer id, Date sinceLastIn, List<DateDocumentObj> updateHistory) throws Exception {
        MongoCollection WCDIO = DB.getCollection("WCDIOcsv");

        UpdateOptions updateOpts = new UpdateOptions().upsert(true);
        Bson filter = Filters.eq("MatchId", id);
        Document update = new Document("MatchId", id).append("lastIn", now);


        Document data = new Document();
        WCDIOcsvData head = new WCDIOcsvData();



        WCDIO.updateOne(filter, update, updateOpts);
    }

    public void FormDataField(Integer id, List<DateDocumentObj> updateHistory) throws Exception {

    }

    @Test
    public void LoopArrayObject() throws Exception {

        MatchEventDAO dao = new MatchEventDAO(datastore);
        MatchEventData data = dao.findByMatchId("103909");
        List<InPlayAttrUpdates> scoreHistory = data.getScoreUpdates();

        System.out.println("[Data] " + data.toString());

        for (InPlayAttrUpdates dvp : scoreHistory) {
            System.out.println("dvp key: " + dvp.getTime());
            System.out.println("dvp value: " + dvp.getVal());
        }

    }
}
