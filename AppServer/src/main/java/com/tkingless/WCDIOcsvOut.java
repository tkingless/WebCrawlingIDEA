package com.tkingless;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tkingless.utils.FileManager;
import org.bson.Document;

import java.io.File;
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

            if (FileManager.CheckFileExist("WCDIOconfig.json")) {
                System.out.println("found the json");
            } else {
                System.out.println("Not found the json");
            }

            config = LoadConfigFile();
            fileSharingPath = (String) config.get("HostedFilesPath");

            WebCrawledDataIO.logger.info("[Important] The WDCIO config json file should be placed at: current path: " + (new File(".")).getAbsolutePath());
            WebCrawledDataIO.logger.info("file path is: " + fileSharingPath);


        } catch (Exception e) {
            e.printStackTrace();
        }

        now = new Date();
    }

    public void run(){

    }
}
