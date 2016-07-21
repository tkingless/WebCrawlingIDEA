package com.tkk.webCrawler;

import com.tkk.ConcurrencyMachine;
import com.tkk.MatchCONSTANTS;
import com.tkk.crawlee.BoardCrawlee;
import com.tkk.webCrawler.HKJCcrawler;
import com.tkk.webCrawler.MatchEventWorker;
import org.junit.*;

/**
 * Created by tsangkk on 7/11/16.
 */
public class FutureWorkerTest {

    MatchEventWorker futureWorker;
    BoardCrawlee testBoardCrlr;

    @Before
    public void setUp() throws Exception {
        HKJCcrawler hkjcCrlr = HKJCcrawler.GetInstance();
        testBoardCrlr = new BoardCrawlee(hkjcCrlr, BoardCrawleeTestSample.FutureBoardhtml);
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
            MatchCONSTANTS.MatchStatus expectedState = MatchCONSTANTS.MatchStatus.STATE_TERMINATED;
            //TODO (DB feature) test on DB has record
            Thread.sleep(500);
            Assert.assertEquals(expectedState, futureWorker.getStatus());
            Assert.assertEquals(false, futureWorker.isAlive());
    }
}