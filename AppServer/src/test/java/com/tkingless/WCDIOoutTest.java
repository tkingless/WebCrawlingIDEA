package com.tkingless;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tkingless.utils.DateTimeEntity;
import com.tkingless.utils.FileManager;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tkingless.MongoDBparam.TestDBAddr;
import static com.tkingless.MongoDBparam.TestDBport;
import static com.tkingless.MongoDBparam.webCrawlingDB;

/**
 * Created by tkingless on 8/21/16.
 */
public class WCDIOoutTest {

    String DBaddr = TestDBAddr;
    int DBport = TestDBport;
    String TestDBname = webCrawlingDB;
    Date now;

    MongoClient client;

    MongoDatabase DB;
    Morphia morphia;
    Datastore datastore;
    MongoCollection<Document> WCDIO, MatchEventsColl;

    private Document config;
    private String fileSharingPath;

    List<MatchCSVhandler> hdlrs = new ArrayList<>();
    private long thresholdToConsider = 1000 * 60 * 60 * 96;


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
            MatchEventsColl = DB.getCollection("MatchEvents");
        } catch (Exception e) {
            System.out.println("Mongo is down");
            client.close();
            return;
        }

        try {
            config = LoadConfigFile();
            fileSharingPath = (String) config.get("HostedFilesPath");

            WebCrawledDataIO.logger.info("[Important] The WDCIO config json file should be placed at: current path: " + (new File(".")).getAbsolutePath());

            if (FileManager.CheckFileExist("WCDIOconfig.json")) {
                System.out.println("found the json");
            } else {
                System.out.println("Not found the json");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fileSharingPath = (String) config.get("HostedFilesPath");
        WebCrawledDataIO.logger.info("file path is: " + fileSharingPath);

        now = new Date();

    }

    private Document LoadConfigFile() {

        Document config = null;

        ClassLoader classLoader = getClass().getClassLoader();
        try {

            String jsonString = "";
            jsonString = IOUtils.toString(classLoader.getResourceAsStream("WCDIOconfig.json"));
            config = Document.parse(jsonString);

        } catch (IOException e) {
            WebCrawledDataIO.logger.error("Have you handled WCDIOconfig.json? ", e);
        }

        return config;
    }

    //TODO check file exists, check folder exists, mv files

    @Test
    public void GatherID() throws Exception {

        try {

            long threshold = now.getTime() - thresholdToConsider;
            DateTimeEntity timeAfterToConsider = new DateTimeEntity(threshold);

            FindIterable<Document> consideredDocs = WCDIO.find(
                    //Multiple criteria
                    new Document("lastIn", new Document("$exists", true)).append("lastIn", new Document("$gte", timeAfterToConsider.GetTheInstant()))
            ).sort(new Document("lastIn", -1)).limit(40);

            consideredDocs.forEach(new Block<Document>() {
                @Override
                public void apply(Document document) {
                    Document matchDoc = GetMatchEventDoc(document.getInteger("MatchId"));


                    hdlrs.add(new MatchCSVhandler(document,matchDoc,fileSharingPath));
                }
            });

        } catch (Exception e) {
            WebCrawledDataIO.logger.error("GatherID() error", e);
        }

        hdlrs.forEach(hdlr->System.out.println("considered doc: " + hdlr.toString()));

    }

    @Test
    public void ProcHdlrs() throws Exception{

        GatherID();

        hdlrs.forEach(hdlr->{
            hdlr.run();
            //TODO: check if write succeed, if yes than update lastOut
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

    @Test
    public void FileManagerTest() throws Exception{
        if(FileManager.CheckFileExist("/home/tsangkk/Development/Github/WebCrawlingIDEA/AppServer/WCDIOconfig.json")){
            System.out.println("found the json");
        } else {
            System.out.println("Not found the json");
        }

    }

}
