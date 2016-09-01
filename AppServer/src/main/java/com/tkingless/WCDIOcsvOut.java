package com.tkingless;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tkingless.utils.DateTimeEntity;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tsangkk on 8/17/16.
 */
public class WCDIOcsvOut {

    private MongoDatabase db;
    private MongoCollection<Document> MatchEventsColl, WCDIOcsvColl;

    private Document config;
    private String fileSharingPath;
    List<MatchCSVhandler> hdlrs = new ArrayList<>();
    private long thresholdToConsider = 1000 * 60 * 60 * 4;
    Date now;


    public WCDIOcsvOut() {
        try {
            db = DBManager.getInstance().getClient().getDatabase(MongoDBparam.webCrawlingDB);
            MatchEventsColl = db.getCollection("MatchEvents");
            WCDIOcsvColl = db.getCollection("WCDIOcsv");
        } catch (Exception e){
            WebCrawledDataIO.logger.error("WCDIOcsvIn init error", e);
        }

        try {

            config = LoadConfigFile();
            fileSharingPath = (String) config.get("HostedFilesPath");

            WebCrawledDataIO.logger.info("file path is: " + fileSharingPath);


        } catch (Exception e) {
            WebCrawledDataIO.logger.error("csv out init error",e);
        }

        now = new Date();
    }

    public void run(){
        GatherID();
        ProcHdlrs();

    }

    void GatherID() {

        try {

            long threshold = now.getTime() - thresholdToConsider;
            DateTimeEntity timeAfterToConsider = new DateTimeEntity(threshold);

            FindIterable<Document> consideredDocs = WCDIOcsvColl.find(
                    //Multiple criteria
                    new Document("lastIn", new Document("$exists", true)).append("lastIn", new Document("$gte", timeAfterToConsider.GetTheInstant()))
            ).sort(new Document("lastIn", -1)).limit(40);

            consideredDocs.forEach(new Block<Document>() {
                @Override
                public void apply(Document document) {
                    Document matchDoc = GetMatchEventDoc(document.getInteger("MatchId"));

                    hdlrs.add(new MatchCSVhandler(document,matchDoc,fileSharingPath,now));
                }
            });

        } catch (Exception e) {
            WebCrawledDataIO.logger.error("GatherID() error", e);
        }

        hdlrs.forEach(hdlr->WebCrawledDataIO.logger.trace("considered doc: " + hdlr.toString()));

    }

    void ProcHdlrs(){

        hdlrs.forEach(hdlr->{
            hdlr.run();
            if(hdlr.isLastOutSucceed()){

                Document filter = new Document("MatchId",hdlr.getMatchDoc().getInteger("MatchId"));
                Document data = new Document("lastOut",now);
                UpdateLastOut(WCDIOcsvColl,filter,data);
            }
        });

    }

    boolean UpdateLastOut(MongoCollection aColl, Bson filter, Document data){
        boolean writeSucess = false;
        try {
            Document update;
            update = new Document("$set", data);

            aColl.updateOne(filter, update);

            writeSucess = true;
        } catch (Exception e) {
            WebCrawledDataIO.logger.error("UpdateLastOut() error", e);
        }
        return writeSucess;
    }

    Document GetMatchEventDoc(Integer id) {
        Document result = MatchEventsColl.find(new Document("MatchId", id)).first();
        if (result != null) {
            return result;
        }
        return null;
    }

    private Document LoadConfigFile() {

        Document config = null;

        ClassLoader classLoader = getClass().getClassLoader();
        try {

            String jsonString = "";
            InputStream input = classLoader.getResourceAsStream("WCDIOconfig.json");
            jsonString = IOUtils.toString(input);
            config = Document.parse(jsonString);
            input.close();

        } catch (IOException e) {
            WebCrawledDataIO.logger.error("Have you handled WCDIOconfig.json? ", e);
        }

        return config;
    }
}
