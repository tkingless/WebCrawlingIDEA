package com.tkk;

/**
 * Created by tsangkk on 6/27/16.
 */

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mongodb.morphia.Morphia;

import static com.tkk.MongoDBparam.*;

//Manage the facilitation of DB
public class DBManager {
    private static DBManager instance = new DBManager();
    private MongoClient client;
    private Morphia morphia;
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
}
