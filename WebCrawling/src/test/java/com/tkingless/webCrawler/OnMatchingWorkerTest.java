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
 * Created by tkingless on 7/31/16.
 */
public class OnMatchingWorkerTest {

    protected MatchEventWorker onMatchingWorker;
    protected List<MatchEventWorker> workers;

    MongoClient client;
    Morphia morphia;
    MatchEventDAO matchDao;

    @Before
    public synchronized void setUp() throws Exception {
        workers = new ArrayList<MatchEventWorker>();
        synchronized (workers) {
            workers = BoardCrawlee.GenerateTestWorker(MatchTestCONSTANTS.TestType.TYPE_MATCHING, BoardCrawleeTestSample.OnMatchingBoardhtml);
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

    //NOTE AFTER THIS TEST MUST RUN DROPDB()
    @Test
    public void FoundAmatchingEvent() throws Exception{
        onMatchingWorker = workers.get(0);

        synchronized (onMatchingWorker) {
            onMatchingWorker.setMatchCrleTestTarget(MatchCrawleeTestSample.onMatching103909firstHalf);
        }

        Thread.sleep(10 * 1000);

        onMatchingWorker.setMatchCrleTestTarget(MatchCrawleeTestSample.onMatching103909HalfTime);

        Thread.sleep(1000 * 5);

        onMatchingWorker.setMatchCrleTestTarget(MatchCrawleeTestSample.onMatching103909secondhalfStart);

        Thread.sleep(1000 * 5);

        onMatchingWorker.setMatchCrleTestTarget(MatchCrawleeTestSample.onMatching103909HADbettingClosed);

        Thread.sleep(1000 * 5);

        onMatchingWorker.setMatchCrleTestTarget(MatchCrawleeTestSample.onMatching103909allBettingClosed);

        Thread.sleep(1000 * 20);
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
