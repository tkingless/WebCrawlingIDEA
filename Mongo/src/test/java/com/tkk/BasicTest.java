package com.tkk;

import com.mongodb.client.AggregateIterable;
import com.sun.org.apache.bcel.internal.generic.DCONST;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.*;
import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

import static java.util.Arrays.asList;

/**
 * Created by tsangkk on 7/26/16.
 */
public class BasicTest {

    String DBaddr = "127.0.0.1";
    int DBport = 27017;
    String TestDBname = "MongoTestWebcrawling";
    String TestCollname = "testCollWebCrawling";

    MongoClient client;

    @Before
    public void TestConnection () throws Exception {
        MongoClientOptions.Builder o = MongoClientOptions.builder().connectTimeout(3000);
        client = new MongoClient(new ServerAddress(DBaddr,DBport));

        try {
            client.getAddress();
        } catch (Exception e) {
            System.out.println("Mongo is down");
            client.close();
            return;
        }
    }

    @Test
    public void GetOrCreateDB () throws Exception {
        MongoDatabase DB = client.getDatabase(TestDBname);
        MongoCollection dbCollection = DB.getCollection(TestCollname);

        //InsertTestObject(dbCollection);

    }

    public void InsertTestObject(MongoCollection aColl){

        aColl.insertOne(new Document
                ("testAttribute",
                        new Document().append("subAttr","val1")
                )
        );
    }

    @Test
    public void FindQuery() throws Exception {
        String jsonString = JsonFromFileToString("primer-dataset.json");
        Document doc = Document.parse(jsonString);
        //client.getDatabase(TestDBname).getCollection("restaurants").insertOne(doc);

        FindAllDocAndDo(client.getDatabase(TestDBname).getCollection("restaurants"));

        //REF: other queries
        //db.getCollection("restaurants").find(new Document("borough", "Manhattan"));
        //db.getCollection("restaurants").find(eq("borough", "Manhattan"));

        //embedded doc query
        FindIterable<Document> embeddedDocQuery = client.getDatabase(TestDBname).getCollection("restaurants").find(
                new Document("address.zipcode", "10462"));

        embeddedDocQuery.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });

        //TODO make address as indexed object
        //TODO query returning embedded doc only

        //Filters helper
        //db.getCollection("restaurants").find(lt("grades.score", 10));
        //combined And operator
        //db.getCollection("restaurants").find(and(eq("cuisine", "Italian"), eq("address.zipcode", "10075")));
        //combined Or operator
        /*
        FindIterable<Document> iterable = db.getCollection("restaurants").find(
        new Document("$or", asList(new Document("cuisine", "Italian"),
                new Document("address.zipcode", "10075"))));
         */

        //Sorting
        /*
        FindIterable<Document> iterable = db.getCollection("restaurants").find()
        .sort(new Document("borough", 1).append("address.zipcode", 1));
         */
        //Sort Helper
        /*
        db.getCollection("restaurants").find().sort(ascending("borough", "address.zipcode"));
         */


    }

    public void FindAllDocAndDo (MongoCollection coll){
        FindIterable<Document> iterable = coll.find();

        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });
    }

    @Test
    public void UpdateQuery() throws Exception{

        client.getDatabase(TestDBname).getCollection("restaurants").updateOne(new Document("borough","Bronx"),
                //record, update with datetime
                //TODO format the current date
                //TODO from DB to java date
                new Document("$set", new Document("borough","PforO")).append("$currentDate",
                                new Document("lastModified",true))
        );

    }

    @Test
    public void AggregateQuery() throws Exception {
        AggregateIterable<Document> iterable = client.getDatabase(TestDBname).getCollection("restaurants").aggregate(asList(
                new Document("$group", new Document("_id", "$borough").append("count", new Document("$sum", 1)))));

        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        });


    }

    @Test
    public void InsertDateTime() throws Exception{
        client.getDatabase(TestDBname).getCollection("restaurants").insertOne(new Document("$currentDate", new Document("CreatedAt",true)));
    }



    @After
    public void cleanDB() throws Exception{
        //client.getDatabase(TestDBname).drop();
    }

    //resources folder usage
    //Ref: https://www.mkyong.com/java/java-read-a-file-from-resources-folder/
    public String JsonFromFileToString (String PathToRes){
        String result = "";

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(PathToRes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
