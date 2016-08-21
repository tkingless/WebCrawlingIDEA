package com.tkingless;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.tkingless.utils.DateTimeEntity;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.print.Doc;
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
    MongoCollection WCDIO;

    Date now = new Date();

    @Before
    public void init() throws Exception {
        MongoClientOptions.Builder o = MongoClientOptions.builder().connectTimeout(3000);
        client = new MongoClient(new ServerAddress(DBaddr, DBport));

        try {
            client.getAddress();

            morphia = new Morphia();
            //morphia.mapPackage("com.tkingless.WebCrawling.DBobject");

            datastore = morphia.createDatastore(client, TestDBname);
            datastore.ensureIndexes();
            DB = client.getDatabase(TestDBname);
            WCDIO = DB.getCollection("WCDIOcsv");
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
    public void ProcConsideredId() throws Exception {
        GetConsideredWorkersByTime();

        //TODO now should be the time from fixedrateexecutor
        now = new Date();

        for (Integer id : launchedMatchIds) {
            try {
                MongoCursor<Document> idOddsCursor = DB.getCollection("InPlayOddsUpdates").find(new Document("MatchId", id)).iterator();

                if (idOddsCursor.hasNext()) {
                    System.out.println("there is odd update for id:" + id);
                    List<DateDocumentObj> updateHistory = GetUpdateHistory(id, DB.getCollection("InPlayAttrUpdates"), DB.getCollection("InPlayOddsUpdates"));

                    if (!updateHistory.isEmpty()) {

                        boolean shouldInit = true;

                        FindIterable<Document> doc = WCDIO.find(new Document("MatchId", id).append("lastIn", new Document("$exists", true)));

                    /*doc.forEach(new Block<Document>() {
                        @Override
                        public void apply(Document document) {
                            System.out.println("Json: " + doc.toString());
                        }
                    });*/

                        if (doc.iterator().hasNext()) {
                            shouldInit = false;
                        }

                        if (shouldInit) {
                            WebCrawledDataIO.logger.trace("should init");
                            InitWCDIOcsv(id, updateHistory);
                        } else {
                            WebCrawledDataIO.logger.trace("should not init");
                            // if now is larger than lastIn, do Update In
                            Date lastIn = doc.first().getDate("lastIn");
                            Document lastRecord = GetLastRecordFromData(WCDIO.find(new Document("MatchId", id)));
                            if (lastRecord.getDate("recorded").getTime() > lastIn.getTime()) {
                                WebCrawledDataIO.logger.trace("should update");
                                UpdateWCDIOcsv(id, updateHistory, lastIn, lastRecord);
                            }

                        }
                    }
                }
            } catch (Exception e) {
                WebCrawledDataIO.logger.error("error ", e);
            }
        }
    }

    public void InitWCDIOcsv(Integer id, List<DateDocumentObj> updateHistory) throws Exception {

        UpdateOptions updateOpts = new UpdateOptions().upsert(true);
        Bson filter = Filters.eq("MatchId", id);
        Document update;
        Document content = new Document("MatchId", id);

        WCDIOcsvData head = new WCDIOcsvData();

        WCDIOcsvData.InitializeRecordHead(updateHistory, head);

        //testing
        update = new Document("$set", content);
        WCDIO.updateOne(filter, update, updateOpts);

        if (PushToDataField(WCDIO, filter, head.ToBson())) {
            content.append("lastIn", now);
            update = new Document("$set", content);
            WCDIO.updateOne(filter, update, updateOpts);
        }
    }

    public void UpdateWCDIOcsv(Integer id, List<DateDocumentObj> updateHistory, Date since, Document lastRecord) throws Exception {

        Document filter = new Document("MatchId", id);

        WCDIOcsvData head = new WCDIOcsvData();

        Iterator<DateDocumentObj> ite = updateHistory.iterator();

        DateDocumentObj ddo = null;
        while (ite.hasNext()) {
            ddo = ite.next();
            if (ddo.getDate().getTime() > since.getTime()) {
                WebCrawledDataIO.logger.trace("iteration end, ddo time: " + ddo.getDate() + " , and lastIn: " + since.getTime());
                break;
            }
        }

        if (ddo != null){

        }

        //init the head first
        WCDIOcsvData.ParseInFromDocument(head, lastRecord);


    }

    //TODO this should really be utility of DAO
    Document GetLastRecordFromData(FindIterable<Document> queried) {
        Document lastRecord = null;
        List<Document> csv = (List<Document>) queried.first().get("data");
        lastRecord = csv.get(csv.size() - 1);
        return lastRecord;
    }

    public List<DateDocumentObj> GetUpdateHistory(Integer id, MongoCollection matchAttrColl, MongoCollection oddsColl) throws Exception {

        List<DateDocumentObj> updateTimeOrders = new ArrayList<>();

        //Match inPlay attr streaming in
        matchAttrColl.find(new Document("MatchId", id)).forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                updateTimeOrders.add(new DateDocumentObj(document.getDate("recorded"), document));
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

        /*for (DateDocumentObj Adata : updateTimeOrders) {
            WebCrawledDataIO.logger.trace(Adata.getDate());
            WebCrawledDataIO.logger.trace(Adata.getDoc().getString("type"));
        }*/

        return updateTimeOrders;
    }

    boolean PushToDataField(MongoCollection aColl, Bson filter, Document data) {

        boolean writeSucess = false;
        try {
            Document update;
            Document content;
            content = new Document("data", data);
            update = new Document("$push", content);

            aColl.updateOne(filter, update);

            writeSucess = true;
        } catch (Exception e) {
            WebCrawledDataIO.logger.error("PushToDataField() error", e);
        }
        return writeSucess;
    }

    @Test
    public void LoopArrayObject() throws Exception {

        Document filter = new Document("MatchId", 105238);

        WCDIO.find(filter).projection(new Document("data", 1)).forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                try {
                    List<Document> data = (List<Document>) document.get("data");

                    for (Document datum : data) {
                        System.out.println("=============================");
                        for (String key : datum.keySet()) {
                            System.out.println("Key: " + key);
                            System.out.println("Value: " + datum.get(key));
                        }
                    }

                } catch (Exception e) {
                    WebCrawledDataIO.logger.error("Get data error", e);
                }
            }
        });

    }
}
