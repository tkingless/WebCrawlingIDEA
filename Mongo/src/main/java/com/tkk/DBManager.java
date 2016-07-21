package com.tkk;

/**
 * Created by tsangkk on 6/27/16.
 */

//Manage the facilitation of DB
public class DBManager {
    private static DBManager ourInstance = new DBManager();

    public static DBManager getInstance() {
        return ourInstance;
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
