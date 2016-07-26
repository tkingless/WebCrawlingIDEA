package com.tkk;

import com.mongodb.client.MongoDatabase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.mongodb.*;

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
        MongoDatabase coll = client.getDatabase("MongoTestWebcrawling");
    }

    @After
    public void cleanDB() throws Exception{

    }
}
