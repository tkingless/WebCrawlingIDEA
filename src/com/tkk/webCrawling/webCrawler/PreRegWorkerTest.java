package com.tkk.webCrawling.webCrawler;

import com.tkk.webCrawling.ConcurrencyMachine;
import com.tkk.webCrawling.crawlee.BoardCrawlee;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by tsangkk on 7/12/16.
 */
public class PreRegWorkerTest {

    MatchEventWorker preRegWorker;
    BoardCrawlee testBoardCrlr;

    /*
    The setUp generate PreRegWorker ignoring the actual input commence, it forcefully sets the event start time
    to half of pre-reg time just right before current time
     */
    @Before
    public void setUp() throws Exception {
        HKJCcrawler hkjcCrlr = HKJCcrawler.GetInstance();
        testBoardCrlr = new BoardCrawlee(hkjcCrlr, BoardCrawleeTestSample.testBoardhtml);
        ConcurrencyMachine.GetInstance().RegisterQueue(testBoardCrlr);
        synchronized (this) {
            ConcurrencyMachine.GetInstance().InvokeQueue();
        }
        //TODO make a cheating pre-reg worker
        //preRegWorker = testBoardCrlr.getParsedWorkers().get(1);
        System.out.println("[WorkerTester] the setUp() finished");
    }

    @After
    public void tearDown() throws Exception {

    }

}