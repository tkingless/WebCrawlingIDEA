package com.tkk.DBobject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.tkk.MatchTestCONSTANTS;
import com.tkk.MongoDBparam;
import com.tkk.WebCrawling.DBobject.MatchEventData;
import com.tkk.crawlee.BoardCrawlee;
import com.tkk.webCrawler.BoardCrawleeTestSample;
import com.tkk.webCrawler.MatchCrawleeTestSample;
import com.tkk.webCrawler.MatchEventWorker;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;

import java.util.ArrayList;
import java.util.List;

import static com.tkk.MongoDBparam.TestDBAddr;
import static com.tkk.MongoDBparam.TestDBport;

/**
 * Created by tkingless on 7/30/16.
 */
public class MorphiaBasicTest {

    protected MatchEventWorker preRegWorker;
    protected List<MatchEventWorker> workers;

    MongoClient client;
    Morphia morphia;
    MatchEventDAO matchDao;

    @Before
    public synchronized void setUp() throws Exception {
        workers = new ArrayList<MatchEventWorker>();
        synchronized (workers) {
            workers = BoardCrawlee.GenerateTestWorker(MatchTestCONSTANTS.TestType.TYPE_PRE_REG, BoardCrawleeTestSample.PreRegBoardhtml);
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
    }

    @Test
    public void SaveAMatchEvent() throws Exception {
        preRegWorker = workers.get(0);
        preRegWorker.setMatchCrleTestTarget(MatchCrawleeTestSample.preReg103904NotStartYet);

        //Data modelling
        MatchEventData aData = new MatchEventData();
        matchDao.FutureEventWorkerToDBdata(preRegWorker,aData);

        matchDao.save(aData);

    }

    @Test
    public void DropDB() throws Exception {
        //client.getDatabase(MongoDBparam.webCrawlingDB).drop();
        client.getDatabase(MongoDBparam.webCrawlingTestDB).drop();
    }
}
