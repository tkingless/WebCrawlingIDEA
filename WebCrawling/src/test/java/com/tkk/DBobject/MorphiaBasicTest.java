package com.tkk.DBobject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.tkk.MatchCONSTANTS;
import com.tkk.MatchTestCONSTANTS;
import com.tkk.MongoDBparam;
import com.tkk.WebCrawling.DBobject.MatchEventData;
import com.tkk.crawlee.BoardCrawlee;
import com.tkk.utils.DateTimeEntity;
import com.tkk.webCrawler.BoardCrawleeTestSample;
import com.tkk.webCrawler.MatchCrawleeTestSample;
import com.tkk.webCrawler.MatchEventWorker;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;

import java.util.*;

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
        matchDao.RegisterMatchEventWorker(preRegWorker);
    }

    @Test
    public void GetByID() throws Exception{
        MatchEventData data = matchDao.findByMatchId(103904);

        System.out.println(data.getMatchKey());
    }

    @Test
    public void TestFindFunctions() throws Exception {

        System.out.println(matchDao.IsMatchRegisteredBefore(103904));
    }

    @Test
    public void TestInsertEndtime() throws Exception {
        preRegWorker = workers.get(0);
        matchDao.SetField(preRegWorker,"endTime",new Date());
    }

    @Test
    public void TestGetEndtime() throws Exception{
        preRegWorker = workers.get(0);
        MatchEventData data =  matchDao.findByMatchId(Integer.parseInt(preRegWorker.getMatchId()));
        System.out.println(data.getEndTime());
    }

    @Test
    public void TestEndTimeExist() throws Exception{
        preRegWorker = workers.get(0);

        System.out.println(matchDao.QueryDataFieldExists(preRegWorker,"endTime"));
    }

    @Test
    public void TestcommenceTimeSetting() throws Exception{
        preRegWorker = workers.get(0);
        long timestampOfcommence = matchDao.findByMatchId(Integer.parseInt(preRegWorker.getMatchId())).getCommence().getTime();
        DateTimeEntity commenceTime = new DateTimeEntity(timestampOfcommence);
        System.out.println("The commence time is: " + commenceTime.GetTheInstant());
    }

    @Test
    public void TestUpdatePoolType() throws Exception {
        preRegWorker = workers.get(0);
        Set<MatchCONSTANTS.InplayPoolType> typePool = EnumSet.noneOf(MatchCONSTANTS.InplayPoolType.class);
        typePool.add(MatchCONSTANTS.InplayPoolType.HAD);
        typePool.add(MatchCONSTANTS.InplayPoolType.CHL);

        matchDao.SetField(preRegWorker,"poolTypes",typePool);

    }

    @Test
    public void TestGetPoolType() throws Exception {
        preRegWorker = workers.get(0);

        java.util.Set<MatchCONSTANTS.InplayPoolType> queried = matchDao.QueryPoolTypes(preRegWorker);

        System.out.println("queried: " + queried);

    }

    @Test
    public void TestUpdateHashMap() throws Exception {
        preRegWorker = workers.get(0);

        HashMap<Date,String> AscoreUpdate = new HashMap();

        //AscoreUpdate.put((new Date()).getTime(),"0 : 2");

        //scoreUpdate
        matchDao.AddHashMap(preRegWorker,"scoreUpdate",AscoreUpdate);
    }

    @Test
    public void TestQueryHashMap() throws  Exception{
        preRegWorker = workers.get(0);

        HashMap<Date,String> hmap = matchDao.QueryHashMap(preRegWorker,"scoreUpdate");

        for(Date d: hmap.keySet()){
            //System.out.println("Hashmap ele" + hmap.get(d));
        }


    }

    @Test
    public void DropDB() throws Exception {
        //client.getDatabase(MongoDBparam.webCrawlingDB).drop();
        client.getDatabase(MongoDBparam.webCrawlingTestDB).drop();
    }
}
