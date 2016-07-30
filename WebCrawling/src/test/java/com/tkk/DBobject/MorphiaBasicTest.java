package com.tkk.DBobject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.tkk.CrawlingDBobjectConverter;
import com.tkk.MongoDBparam;
import com.tkk.WebCrawling.DBobject.MatchEventDAO;
import com.tkk.WebCrawling.DBobject.MatchEventData;
import com.tkk.webCrawler.MatchCrawleeTestSample;
import com.tkk.webCrawler.PreRegWorkerTest;
import org.junit.Test;
import org.mongodb.morphia.Morphia;

import static com.tkk.MongoDBparam.TestDBAddr;
import static com.tkk.MongoDBparam.TestDBport;

/**
 * Created by tkingless on 7/30/16.
 */
public class MorphiaBasicTest extends PreRegWorkerTest {

    MongoClient client;

    @Test
    public void SaveAMatchEvent() throws Exception {
        preRegWorker = workers.get(0);
        preRegWorker.setMatchCrleTestTarget(MatchCrawleeTestSample.preReg103904NotStartYet);

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
        Morphia morphia = new Morphia();
        MatchEventDAO matchDao = new MatchEventDAO(client,morphia);

        //Data modelling
        MatchEventData aData = new MatchEventData();
        CrawlingDBobjectConverter.ExplainEventWorkerToDBdata(preRegWorker,aData);

        matchDao.save(aData);

    }
}
