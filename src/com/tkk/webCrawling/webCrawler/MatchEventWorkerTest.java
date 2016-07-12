package com.tkk.webCrawling.webCrawler;

import com.tkk.webCrawling.ConcurrencyMachine;
import com.tkk.webCrawling.MatchCONSTANTS;
import com.tkk.webCrawling.crawlee.BoardCrawlee;
import org.junit.*;

/**
 * Created by tsangkk on 7/11/16.
 */
public class MatchEventWorkerTest {

    MatchEventWorker futureWorker;
    BoardCrawlee testBoardCrlr;

    @Before
    public void setUp() throws Exception {
        HKJCcrawler hkjcCrlr = HKJCcrawler.GetInstance();
        testBoardCrlr = new BoardCrawlee(hkjcCrlr, BoardCrawleeTestSample.testBoardhtml);
        ConcurrencyMachine.GetInstance().RegisterQueue(testBoardCrlr);
        synchronized (this) {
            ConcurrencyMachine.GetInstance().InvokeQueue();
        }
        futureWorker = testBoardCrlr.getParsedWorkers().get(0);
        System.out.println("[WorkerTester] the setUp() finished");
    }

    @After
    public void tearDown() throws Exception {
        //TODO (DB feature) remove Future worker DB record
    }

    //@Ignore
    @Test
    public void TestFutureWorkerState() throws Exception {
            System.out.println("TestFutureWorkerState() called");
            MatchCONSTANTS.MatchStatus expectedState = MatchCONSTANTS.MatchStatus.STATE_FUTURE_MATCH;
            //TODO (DB feature) test on DB has record
            Assert.assertEquals(expectedState, futureWorker.getStatus());
            Assert.assertEquals(false, futureWorker.isAlive());
    }

}