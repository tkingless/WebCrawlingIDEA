package com.tkingless.webCrawler;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.tkingless.DBobject.MatchEventDAO;
import com.tkingless.MatchTestCONSTANTS;
import com.tkingless.MongoDBparam;
import com.tkingless.MatchCONSTANTS;
import com.tkingless.crawlee.BoardCrawlee;
import org.junit.*;
import org.mongodb.morphia.Morphia;

import static com.tkingless.MongoDBparam.TestDBAddr;
import static com.tkingless.MongoDBparam.TestDBport;

/**
 * Created by tsangkk on 7/11/16.
 */
public class FutureWorkerTest {

    MatchEventWorker futureWorker;
    BoardCrawlee testBoardCrlr;

    MongoClient client;
    Morphia morphia;
    MatchEventDAO matchDao;

    @Before
    public void setUp() throws Exception {

        //Initialize MongoClient
        MongoClientOptions.Builder o = MongoClientOptions.builder().connectTimeout(3000);
        client = new MongoClient(new ServerAddress(TestDBAddr, TestDBport));
        try {
            client.getAddress();
        } catch (Exception e) {
            System.out.println("Mongo is down");
            client.close();
            return;
        }

        //Morphia DAO
        morphia = new Morphia();
        matchDao = new MatchEventDAO(client, morphia, MongoDBparam.webCrawlingTestDB);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void TestFoundFutureEvent() throws Exception {
        futureWorker = BoardCrawlee.GenerateTestWorker(MatchTestCONSTANTS.TestType.TYPE_FUTURE, BoardCrawleeTestSample.FutureBoardhtml).get(0);
        futureWorker.setMatchCrleTestTarget(MatchCrawleeTestSample.future104030NotStartYet);

        Thread.sleep(4 * 1000);
    }

    @Test
    public void DropTestDB() throws Exception {
        client.getDatabase(MongoDBparam.webCrawlingTestDB).drop();
    }

    @Test
    public void DropProdDB() throws Exception {
        client.getDatabase(MongoDBparam.webCrawlingDB).drop();
    }
}