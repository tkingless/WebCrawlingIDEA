package com.tkingless;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tkingless.utils.FileManager;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.junit.Before;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.io.File;
import java.io.IOException;

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

    MongoClient client;

    MongoDatabase DB;
    Morphia morphia;
    Datastore datastore;
    MongoCollection WCDIO;

    private Document config;
    private String filePath;


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

        try {
            config = LoadConfigFile();
            filePath = (String) config.get("HostedFilesPath");

           WebCrawledDataIO.logger.info("[Important] The WDCIO config json file should be placed at: current path: " + (new File(".")).getAbsolutePath());

            if(FileManager.CheckFileExist("WCDIOconfig_sample.json")){
                System.out.println("found the json");
            } else {
                System.out.println("Not found the json");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    private Document LoadConfigFile(){

        Document config = null;

        ClassLoader classLoader = getClass().getClassLoader();
        try {

            String jsonString = "";
            jsonString = IOUtils.toString(classLoader.getResourceAsStream("WCDIOconfig_sample.json"));
            config = Document.parse(jsonString);

        } catch (IOException e) {
            WebCrawledDataIO.logger.error("Have you handled WCDIOconfig.json? ", e);
        }

        return config;
    }

}
