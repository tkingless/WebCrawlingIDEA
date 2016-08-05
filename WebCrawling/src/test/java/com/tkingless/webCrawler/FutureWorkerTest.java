package com.tkingless.webCrawler;

import com.tkingless.utils.ConcurrencyMachine;
import com.tkingless.MatchCONSTANTS;
import com.tkingless.crawlee.BoardCrawlee;
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
    }

    @After
    public void tearDown() throws Exception {
    }

    //@Ignore
    @Test
    public void TestFutureWorkerState() throws Exception {
            System.out.println("TestFutureWorkerState() called");
            MatchCONSTANTS.MatchStatus expectedState = MatchCONSTANTS.MatchStatus.STATE_TERMINATED;

            Thread.sleep(500);
            Assert.assertEquals(expectedState, futureWorker.getStatus());
            Assert.assertEquals(false, futureWorker.isAlive());
    }
}