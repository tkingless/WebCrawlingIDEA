package com.tkk.webCrawling.webCrawler;

import com.tkk.webCrawling.ConcurrencyMachine;
import com.tkk.webCrawling.crawlee.BoardCrawlee;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        //preRegWorker = BoardCrawlee.GenerateTestWorker(MatchTestCONSTANTS.TestType.TYPE_PRE_REG,BoardCrawleeTestSample.testBoardhtml).get(0);
        System.out.println("[WorkerTester] the setUp() finished");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void Sandbox() throws Exception {

        BoardCrawlee.GenerateTestWorker(MatchTestCONSTANTS.TestType.TYPE_PRE_REG, BoardCrawleeTestSample.testBoardhtml).get(0);

        System.out.println("preRegWorker status: " + preRegWorker.getStatus());
        System.out.println("preRegWorker state: " + preRegWorker.getState());
        System.out.println("preRegWorker state: " + preRegWorker.getMatchId());
        System.out.println("[WorkerTester] the setUp() finished");
    }

}