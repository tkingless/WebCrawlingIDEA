package com.tkk.mongodb;

/**
 * Created by tsangkk on 6/27/16.
 */
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
}
