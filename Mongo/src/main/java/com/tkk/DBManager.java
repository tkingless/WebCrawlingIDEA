package com.tkk;

/**
 * Created by tsangkk on 6/27/16.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO DAO pattern: http://www.pretechsol.com/2012/09/java-mongodb-morphia-basicdao-example.html#.V5owYpN96Rs
//TODO: Morphia https://mongodb.github.io/morphia/

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
