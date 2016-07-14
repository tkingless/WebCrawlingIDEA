package com.tkk.webCrawling.TestCases;

import com.tkk.webCrawling.MatchCONSTANTS;
import com.tkk.webCrawling.crawlee.BoardCrawlee;
import com.tkk.webCrawling.utils.DateTimeEntity;
import com.tkk.webCrawling.webCrawler.MatchEventWorker;
import org.junit.After;
import org.junit.Assert;
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
    private String simulatedMatchCrleSrc;
    /*
    The setUp generate PreRegWorker ignoring the actual input commence, it forcefully sets the event start time
    to half of pre-reg time just right before current time
     */
    @Before
    public synchronized void setUp() throws Exception {
        workers = new ArrayList<MatchEventWorker>();
        synchronized (workers) {
            workers = BoardCrawlee.GenerateTestWorker(MatchTestCONSTANTS.TestType.TYPE_PRE_REG, BoardCrawleeTestSample.PreRegBoardhtml);
        }
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void StateVerification() throws Exception {
        preRegWorker = workers.get(0);
        preRegWorker.setMatchCrleTestTarget(simulatedMatchCrleSrc);

        Thread.sleep(500);
        System.out.println("preRegWorker matchid: " + preRegWorker.getMatchId());
        System.out.println("preRegWorker commence time: " + preRegWorker.getCommenceTime());
        System.out.println("now is : " + new DateTimeEntity());
        MatchCONSTANTS.MatchStatus expectedStatJustAfterInit = MatchCONSTANTS.MatchStatus.STATE_PRE_REGISTERED;
        MatchCONSTANTS.MatchStage expectedStageJustAfterInit = MatchCONSTANTS.MatchStage.STAGE_ESST;
        Assert.assertEquals(expectedStatJustAfterInit,preRegWorker.getStatus());
        Assert.assertEquals(expectedStageJustAfterInit,preRegWorker.getStage());

        simulatedMatchCrleSrc = MatchCrawleeTestSample.preReg104031NotStartYet;

        Thread.sleep(1000 * 30);



        System.out.println("[WorkerTester] the setUp() finished");
    }

}