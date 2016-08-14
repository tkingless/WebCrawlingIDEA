package com.tkingless;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.tkingless.utils.DateTimeEntity;
import com.tkingless.utils.FileManager;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tsangkk on 8/8/16.
 *
 */
public class WebCrawledDataIO implements ServletContextListener {

    final static Logger logger = LogManager.getLogger(WebCrawledDataIO.class);

    //TODO can write a file into the filesharing folder
    //TODO can connect to DB to get data
    //TODO can call CSV, file manager
    //TODO can have working threads, again....my gosh

    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
    private String filePath;
    private Document config;
    private MongoDatabase db;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("WebCrawledDataIO init() called.");

        config = LoadConfigFile();

        filePath = (String) config.get("HostedFilesPath");
        FileManager fileManager;

        if(FileManager.CreateFolder(filePath)){
            fileManager = new FileManager(filePath + "/helloWorld.txt");

            try {
                fileManager.Append("you are the best!");
                fileManager.Close();
            } catch (IOException e) {
                logger.error("filemanager: ",e);
            }
        }

        LoadConfigFile();

        logger.info("[Important] The WDCIO config json file should be placed at: current path: " + (new File(".")).getAbsolutePath());

        if(FileManager.CheckFileExist("WCDIOconfig_sample.json")){
            System.out.println("found the json");
        } else {
            System.out.println("Not found the json");
        }

        try {
            db = DBManager.getInstance().getClient().getDatabase(MongoDBparam.webCrawlingDB);
        } catch (Exception e){
            logger.error("context init error", e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    private Document LoadConfigFile(){

        Document config = null;

        ClassLoader classLoader = getClass().getClassLoader();
        try {

            String jsonString = "";
            jsonString = IOUtils.toString(classLoader.getResourceAsStream("WCDIOconfig_sample.json"));
            config = Document.parse(jsonString);

        } catch (IOException e) {
            logger.error("Have you handled WCDIOconfig.json? ", e);
        }

        return config;
    }


    //=======================================
    //WDCIO rundown
    //=======================================

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
                    logger.error("Considered id error",e);
                }

            }
        });
        logger.info("launchedMatchIds : " + launchedMatchIds.toString());
        logger.info("lostMatchingIds : " + lostMatchingIds.toString());
    }
}
