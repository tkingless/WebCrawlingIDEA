package com.tkingless;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.junit.Before;
import org.junit.Test;

import static com.tkingless.MongoDBparam.TestDBAddr;
import static com.tkingless.MongoDBparam.TestDBport;
import static com.tkingless.MongoDBparam.webCrawlingTestDB;

/**
 * Created by tsangkk on 8/10/16.
 */
public class WebCrawledDataIOTest {

    String DBaddr = TestDBAddr;
    int DBport = TestDBport;
    String TestDBname = webCrawlingTestDB;
    String TestCollname = "testCollWebCrawling";

    MongoClient client;

    @Before
    public void init() throws Exception {
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
    public void exportDBcollections() throws Exception{

    }

    @Test
    public void importDBcollections() throws Exception{

    }
}
