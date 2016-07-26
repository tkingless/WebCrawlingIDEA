package com.tkk;

/**
 * Created by tsangkk on 6/27/16.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO DAO pattern: http://www.tutorialspoint.com/design_pattern/data_access_object_pattern.htm
//TODO: Morphia https://mongodb.github.io/morphia/, https://docs.mongodb.com/manual/aggregation/
//TODO: IDEA mongo plugin: https://plugins.jetbrains.com/plugin/7141

//Manage the facilitation of DB
public class DBManager {
    private static DBManager instance = new DBManager();
    final public static Logger logger = LogManager.getLogger(DBManager.class);

    public static DBManager getInstance() {
        if (instance == null) {
            logger.error("DBmanager","DBmanager instance null");
        }
        return instance;
    }

    private DBManager() {
    }

    public boolean IsThereConnection() {
        return true;
    }

    //TODO
    void OnDisconnected() {

    }

    void OnConnected() {

    }
}
