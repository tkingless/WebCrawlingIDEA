package com.tkk;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.*;
import org.bson.Document;

import org.apache.commons.io.IOUtils;

import javax.print.Doc;
import java.io.IOException;

/**
 * Created by tsangkk on 7/26/16.
 */
public class BasicTest {

    String DBaddr = "127.0.0.1";
    int DBport = 27017;

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
        MongoDatabase DB = client.getDatabase("MongoTestWebcrawling");
        MongoCollection dbCollection = DB.getCollection("testCollWebCrawling");

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
    public void LoadTestResourcesJson() throws Exception{

        String jsonString = JsonFromFileToString("primer-dataset.json");
        Document doc = Document.parse(jsonString);

        client.getDatabase("MongoTestWebcrawling").getCollection("restaurants").insertOne(doc);
    }

    //@After
    @Test
    public void cleanDB() throws Exception{
        client.getDatabase("MongoTestWebcrawling").drop();
    }

    //resources folder usage
    //Ref: https://www.mkyong.com/java/java-read-a-file-from-resources-folder/
    public String JsonFromFileToString (String PathToRes){
        String result = "";

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = org.apache.commons.io.IOUtils.toString(classLoader.getResourceAsStream(PathToRes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
