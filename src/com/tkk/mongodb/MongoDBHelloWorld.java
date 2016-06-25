package com.tkk.mongodb;
import com.mongodb.*;
import java.net.UnknownHostException;

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
                new ServerAddress("192.168.33.11", 27017));

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
