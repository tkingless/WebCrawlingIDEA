package com.tkingless;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.tkingless.MongoDBparam.TestDBAddr;
import static com.tkingless.MongoDBparam.TestDBport;
import static com.tkingless.MongoDBparam.webCrawlingDB;

/**
 * Created by tkingless on 8/7/16.
 */
public class CrawlerTest {

    String DBaddr = TestDBAddr;
    int DBport = TestDBport;
    String TestDBname = webCrawlingDB;

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
    public void ShowBettingEndedOdds() throws Exception{
        MongoDatabase DB = client.getDatabase(TestDBname);
        MongoCollection dbCollection = DB.getCollection("InPlayOddsUpdates");

        FindIterable<Document> embeddedDocQuery = dbCollection.find(new Document("poolStatus", "bettingclosed"));

        embeddedDocQuery.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });
    }

    @Test
    public void ShowAMatchOdds() throws Exception{
        MongoDatabase DB = client.getDatabase(TestDBname);
        MongoCollection dbCollection = DB.getCollection("InPlayOddsUpdates");

        FindIterable<Document> embeddedDocQuery = dbCollection.find(new Document("MatchId", 104790));

        embeddedDocQuery.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });
    }

    @Test
    public void GetPoolTypes() throws Exception{
        MongoDatabase DB = client.getDatabase(TestDBname);
        MongoCollection dbCollection = DB.getCollection("MatchEvents");

        FindIterable<Document> embeddedDocQuery = dbCollection.find(new Document("MatchId", 106038));

        embeddedDocQuery.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {

                List<String> pools = (List<String>) document.get("poolTypes");

                System.out.println(pools);

                if(pools.contains("CHL")){
                    System.out.println("yes");
                }
            }
        });
    }
}
