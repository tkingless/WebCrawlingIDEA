package com.tkingless;

import com.mongodb.Block;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.tkingless.utils.DateTimeEntity;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

/**
 * Created by tsangkk on 8/16/16.
 */
public class WCDIOcsvIn {

    private static WCDIOcsvIn instance = null;

    public static WCDIOcsvIn GetInstance() {

        if (instance == null) {
            WebCrawledDataIO.logger.trace("WCDIOcsvIn init()");
            instance = new WCDIOcsvIn();
        }

        return instance;
    }

    private MongoDatabase db;
    private long thresholdToConsider = 1000 * 60 * 60 * 4;
    private MongoCollection<Document> MatchEventsColl, OddsColl, AttrColl, WCDIOcsvColl;

    List<Integer> launchedMatchIds = new ArrayList<>();
    List<Integer> lostMatchingIds = new ArrayList<>();

    public WCDIOcsvIn() {
        try {
            db = DBManager.getInstance().getClient().getDatabase(MongoDBparam.webCrawlingDB);
            MatchEventsColl = db.getCollection("MatchEvents");
            OddsColl = db.getCollection("InPlayOddsUpdates");
            AttrColl = db.getCollection("InPlayAttrUpdates");
            WCDIOcsvColl = db.getCollection("WCDIOcsv");
        } catch (Exception e){
            WebCrawledDataIO.logger.error("WCDIOcsvIn init error", e);
        }
    }

    public void run(){

        Date now = new Date();
        GetConsideredWorkersByTime(now);
        ProcConsideredId(now,launchedMatchIds,false);
        ProcConsideredId(now,lostMatchingIds,true);
        launchedMatchIds.clear();
        lostMatchingIds.clear();
    }

    void GetConsideredWorkersByTime(Date now) {

        long threshold = now.getTime() - thresholdToConsider;
        DateTimeEntity timeAfterToConsider = new DateTimeEntity(threshold);

        FindIterable<Document> consideredIds = MatchEventsColl.find(
                //Multiple criteria
                new Document("actualCommence", new Document("$exists", true)).append(
                        "actualCommence", new Document("$gte", timeAfterToConsider.GetTheInstant())).append(
                        "scoreUpdates", new Document("$exists", true)).append(
                        "stageUpdates", new Document("$exists", true))
        ).projection(new Document("MatchId", 1).append("scoreUpdates", 1).append("stageUpdates", 1).append("actualCommence", 1).append("endTime", 1))
                .sort(new Document("actualCommence", -1))
                .limit(40);

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
                        if(!document.containsKey("endTime")) {
                            lostMatchingIds.add(document.getInteger("MatchId"));
                        }
                    }

                } catch (Exception e) {
                    WebCrawledDataIO.logger.error("GetConsideredWorkersByTime() error",e);
                }

            }
        });

        WebCrawledDataIO.logger.trace("launchedMatchIds : " + launchedMatchIds.toString());
        WebCrawledDataIO.logger.trace("lostMatchingIds : " + lostMatchingIds.toString());
    }

    public void ProcConsideredId(Date now, List<Integer> ids, boolean toEnd) {

        for (Integer id : ids) {
            try {
                Document IDfilter = new Document("MatchId",id);

                MongoCursor<Document> idOddsCursor = OddsColl.find(IDfilter).iterator();
                FindIterable<Document> WCDIOdoc = WCDIOcsvColl.find(IDfilter.append("lastIn", new Document("$exists", true)));

                if(WCDIOdoc.iterator().hasNext()) {
                    if (WCDIOdoc.first().containsKey("MarkedEnd")) {
                        WebCrawledDataIO.logger.trace("skipped this id as marked end already: " + id);
                        continue;
                    }
                }

                List<DateDocumentObj> updateHistory = null;

                if (idOddsCursor.hasNext()) {
                    //WebCrawledDataIO.logger.trace("there is odd update for id:" + id);
                    updateHistory = GetUpdateHistory(id, AttrColl, OddsColl);
                } else {
                    WebCrawledDataIO.logger.debug("idOddsCursor is null");
                    continue;
                }

                if (!updateHistory.isEmpty()) {
                    boolean shouldInit = true;

                    /*doc.forEach(new Block<Document>() {
                        @Override
                        public void apply(Document document) {
                            System.out.println("Json: " + doc.toString());
                        }
                    });*/

                    if (WCDIOdoc.iterator().hasNext()) {
                        shouldInit = false;
                    }

                    if (shouldInit) {
                        WebCrawledDataIO.logger.trace("should init");
                        InitWCDIOcsv(id, updateHistory, now);
                    } else {
                        WebCrawledDataIO.logger.trace("should not init");
                        // if now is larger than lastIn, do Update In
                        Date lastIn = WCDIOdoc.first().getDate("lastIn");

                        if (updateHistory.get(updateHistory.size()-1).getDate().getTime() > lastIn.getTime()) {
                            WebCrawledDataIO.logger.trace("going to update, id: "+ id);
                            UpdateWCDIOcsv(id, updateHistory, lastIn,now);
                        }

                    }
                }

                Document MatchEventDoc = MatchEventsColl.find(new Document("MatchId", id)).first();
                if(MatchEventDoc != null) {
                    WebCrawledDataIO.logger.debug("[Adding MarkedEnded] MatchEventDoc is not null, id: " + id);
                    if (MatchEventDoc.containsKey("endTime") || toEnd) {
                        WebCrawledDataIO.logger.debug("[Adding MarkedEnded] csv markded ended, id: " + id);
                        MarkedEnded(WCDIOcsvColl, IDfilter, new Document("MarkedEnd", now));
                    }
                } else {
                    WebCrawledDataIO.logger.debug("[Adding MarkedEnded] MatchEventDoc is null, id: " + id);
                }

            } catch (Exception e) {
                WebCrawledDataIO.logger.error("ProcConsideredId() error ", e);
            }
        }
    }

    public void InitWCDIOcsv(Integer id, List<DateDocumentObj> updateHistory, Date now) {

        UpdateOptions updateOpts = new UpdateOptions().upsert(true);
        Bson filter = Filters.eq("MatchId", id);
        Document update;
        Document content = new Document("MatchId", id);

        WCDIOcsvData head = new WCDIOcsvData();


        WCDIOcsvData.InitializeRecordHead(updateHistory, head);

        //testing
        update = new Document("$set", content);
        WCDIOcsvColl.updateOne(filter, update, updateOpts);

        if (PushToDataField(WCDIOcsvColl, filter, head.ToBson())) {
            content.append("lastIn", now);
            update = new Document("$set", content);
            WCDIOcsvColl.updateOne(filter, update, updateOpts);
        }
    }

    public void UpdateWCDIOcsv(Integer id, List<DateDocumentObj> updateHistory, Date since, Date now) {

        try {
            Document filter = new Document("MatchId", id);
            Document lastRecord = GetLastRecordFromData(WCDIOcsvColl.find(filter));

            WCDIOcsvData head = new WCDIOcsvData();
            //init the head first
            WCDIOcsvData.ParseInFromDocument(head, lastRecord);

            Iterator<DateDocumentObj> ite = updateHistory.iterator();

            DateDocumentObj ddo = null;
            while (ite.hasNext()) {
                ddo = ite.next();
                if (ddo.getDate().getTime() > since.getTime()) {
                    //WebCrawledDataIO.logger.trace("iteration end, ddo time: " + ddo.getDate() + " , and lastIn: " + since.getTime());
                    break;
                }
            }

            if (ddo != null) {
                UpdateAction(ddo, head, id, filter, now);
                while (ite.hasNext()) {
                    ddo = ite.next();
                    UpdateAction(ddo, head, id, filter, now);
                }
            }

            }catch(Exception e){
                WebCrawledDataIO.logger.error("UpdateWCDIOcsv()", e);
            }

    }

    void UpdateAction(DateDocumentObj ddo, WCDIOcsvData head, Integer id, Document filter, Date now){
        WCDIOcsvData.SetHeadByType(ddo.getDoc(), head);
        //WebCrawledDataIO.logger.debug("UpdateAction, head: " + head.toString());

        if(PushToDataField(WCDIOcsvColl, new Document("MatchId", id), head.ToBson())) {
            Document updateLastIn = new Document("lastIn", now);
            Document update = new Document("$set", updateLastIn);
            WCDIOcsvColl.updateOne(filter, update);
            WebCrawledDataIO.logger.debug("has updated lastIn as: "+ now);
        }
    }

    Document GetLastRecordFromData(FindIterable<Document> queried) {
        Document lastRecord = null;
        try {
            List<Document> csv = (List<Document>) queried.first().get("data");
            lastRecord = csv.get(csv.size() - 1);
        } catch ( Exception e){
            WebCrawledDataIO.logger.error("GetLastRecordFromData()",e);
        }
        return lastRecord;
    }

    public List<DateDocumentObj> GetUpdateHistory(Integer id, MongoCollection matchAttrColl, MongoCollection oddsColl){

        List<DateDocumentObj> updateTimeOrders = new ArrayList<>();

        try {
            //Match inPlay attr streaming in
            matchAttrColl.find(new Document("MatchId", id)).forEach(new Block<Document>() {
                @Override
                public void apply(Document document) {
                    //WebCrawledDataIO.logger.debug("attr doc in history found, id: " + id);
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

            /*for(DateDocumentObj ddo: updateTimeOrders){
                WebCrawledDataIO.logger.debug("GetUpdateHistory, ddo date: "+ddo.getDate()+" type: " + ddo.getDoc().get("type"));
            }*/

        } catch (Exception e){
            WebCrawledDataIO.logger.error("GetUpdateHistory()",e);
        }

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

    boolean MarkedEnded(MongoCollection aColl, Bson filter, Document data){
        boolean writeSucess = false;
        try {
            Document update;
            update = new Document("$set", data);

            aColl.updateOne(filter, update);

            writeSucess = true;
        } catch (Exception e) {
            WebCrawledDataIO.logger.error("MarkedEnded() error", e);
        }
        return writeSucess;
    }
}
