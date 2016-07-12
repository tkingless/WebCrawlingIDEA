package com.tkk.webCrawling.webCrawler;

import com.tkk.webCrawling.ConcurrencyMachine;
import com.tkk.webCrawling.crawlee.BoardCrawlee;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by tsangkk on 7/11/16.
 */
public class MatchEventWorkerTest {

    MatchEventWorker testWorker;

    @Before
    public void setUp() throws Exception {
        HKJCcrawler hkjcCrlr = HKJCcrawler.GetInstance();
        BoardCrawlee boardCrlr = new BoardCrawlee(hkjcCrlr, BoardCrawleeTestSample.testBoardhtml);
        ConcurrencyMachine.GetInstance().RegisterQueue(boardCrlr);
        synchronized (this) {
            ConcurrencyMachine.GetInstance().InvokeQueue();
        }
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void sandbox() throws Exception {

    }

}