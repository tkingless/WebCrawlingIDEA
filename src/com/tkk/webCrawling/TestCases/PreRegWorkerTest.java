package com.tkk.webCrawling.TestCases;

import com.tkk.webCrawling.crawlee.BoardCrawlee;
import com.tkk.webCrawling.webCrawler.MatchEventWorker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsangkk on 7/12/16.
 */
public class PreRegWorkerTest {

    MatchEventWorker preRegWorker;
    List<MatchEventWorker> workers;
    /*
    The setUp generate PreRegWorker ignoring the actual input commence, it forcefully sets the event start time
    to half of pre-reg time just right before current time
     */
    @Before
    public synchronized void setUp() throws Exception {
        workers = new ArrayList<MatchEventWorker>();
        synchronized (workers) {
            workers = BoardCrawlee.GenerateTestWorker(MatchTestCONSTANTS.TestType.TYPE_PRE_REG, BoardCrawleeTestSample.FutureBoardhtml);
        }
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void StateVerification() throws Exception {
        preRegWorker = workers.get(0);

        Thread.sleep(500);
        System.out.println("preRegWorker status: " + preRegWorker.getStatus());
        System.out.println("preRegWorker stage: " + preRegWorker.getStage());
        System.out.println("preRegWorker matchid: " + preRegWorker.getMatchId());

        Thread.sleep(1000 * 30);
        System.out.println("[WorkerTester] the setUp() finished");
    }

}