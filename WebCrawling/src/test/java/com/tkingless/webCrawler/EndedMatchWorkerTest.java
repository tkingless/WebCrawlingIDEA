package com.tkingless.webCrawler;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.tkingless.DBobject.MatchEventDAO;
import com.tkingless.MatchTestCONSTANTS;
import com.tkingless.MongoDBparam;
import com.tkingless.crawlee.BoardCrawlee;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;

import java.util.ArrayList;
import java.util.List;

import static com.tkingless.MongoDBparam.TestDBAddr;
import static com.tkingless.MongoDBparam.TestDBport;

/**
 * Created by tsangkk on 8/4/16.
 */
public class EndedMatchWorkerTest {


    protected MatchEventWorker endedWorker;
    protected List<MatchEventWorker> workers;

    MongoClient client;
    Morphia morphia;
    MatchEventDAO matchDao;

    @Before
    public synchronized void setUp() throws Exception {
        workers = new ArrayList<MatchEventWorker>();
        synchronized (workers) {
            workers = BoardCrawlee.GenerateTestWorker(MatchTestCONSTANTS.TestType.TYPE_ENDED, BoardCrawleeTestSample.EndedBoardhtml);
        }

        //Initialize MongoClient
        MongoClientOptions.Builder o = MongoClientOptions.builder().connectTimeout(3000);
        client = new MongoClient(new ServerAddress(TestDBAddr,TestDBport));
        try {
            client.getAddress();
        } catch (Exception e) {
            System.out.println("Mongo is down");
            client.close();
            return;
        }

        //Morphia DAO
        morphia = new Morphia();
        matchDao = new MatchEventDAO(client,morphia, MongoDBparam.webCrawlingTestDB);
        //DBManager.getInstance();
    }

    @Test
    public void TestAllBettingClosed() throws Exception{

        endedWorker = workers.get(0);

        endedWorker.setMatchCrleTestTarget(MatchCrawleeTestSample.ended103910allBettingClosed);

        Thread.sleep(4 * 1000);

    }

    @Test
    public void TestXmlInvalid() throws Exception{

        endedWorker = workers.get(0);

        endedWorker.setMatchCrleTestTarget(MatchCrawleeTestSample.invalidHtml);

        Thread.sleep(4 * 1000);
    }

    @Test
    public void DropTestDB() throws Exception {
        client.getDatabase(MongoDBparam.webCrawlingTestDB).drop();
    }

    @Test
    public  void DropProdDB() throws Exception{
        client.getDatabase(MongoDBparam.webCrawlingDB).drop();
    }
}
