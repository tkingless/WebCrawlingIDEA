package com.tkk;
import com.mongodb.*;
import java.net.UnknownHostException;

import com.tkk.MongoDBparam.*;

import static com.tkk.MongoDBparam.DBaddr;
import static com.tkk.MongoDBparam.DBport;

/**
 * Created by tkingless on 23/6/2016.
 */
public class MongoDBHelloWorld {
    public static void main(String[] args) throws UnknownHostException {
        //
        // Creates a new instance of MongoDBClient and connect to localhost
        // port 27017.
        //
        MongoClient client = new MongoClient(
                new ServerAddress(DBaddr, DBport));

        //
        // Gets the peopledb from the MongoDB instance.
        //
        DB database = client.getDB("peopledb");

        //
        // Gets the persons collections from the database.
        //
        DBCollection collection = database.getCollection("users");

        //
        // Gets a single document / object from this collection.
        //
        DBObject document = collection.findOne();

        //
        // Prints out the document.
        //
        System.out.println(document);
    }
}
