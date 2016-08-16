package com.tkingless;

/**
 * Created by tsangkk on 6/27/16.
 */

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import static com.tkingless.MongoDBparam.*;

//Manage the facilitation of DB
public class DBManager {
    private static DBManager instance = new DBManager();
    private MongoClient client;
    private Morphia morphia;
    private Datastore datastore;
    final public static Logger logger = LogManager.getLogger(DBManager.class);


    public static DBManager getInstance() {
        if (instance == null) {
            logger.error("DBmanager","DBmanager instance null");
        }
        return instance;
    }

    private DBManager() {
        MongoClientOptions.Builder o = MongoClientOptions.builder().connectTimeout(3000);
        client = new MongoClient(new ServerAddress(DBaddr,DBport));
        morphia = new Morphia();

        morphia.mapPackage("com.tkingless.WebCrawling.DBobject");

        datastore = morphia.createDatastore(new MongoClient(), webCrawlingDB);
        datastore.ensureIndexes();

    }

    public boolean IsThereConnection() {
        return true;
    }

    void OnDisconnected() {

    }

    void OnConnected() {

    }

    public MongoClient getClient() {
        return client;
    }

    public Morphia getMorphia() {
        return morphia;
    }

    public Datastore getDatastore() {
        return datastore;
    }
}
