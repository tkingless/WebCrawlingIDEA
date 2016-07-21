package com.tkk.webCrawler;

import com.tkk.WebCrawling.MatchCONSTANTS;
import com.tkk.WebCrawling.MatchTestCONSTANTS;
import com.tkk.WebCrawling.crawlee.BoardCrawlee;
import com.tkk.WebCrawling.utils.DateTimeEntity;
import com.tkk.WebCrawling.webCrawler.MatchEventWorker;
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
        simulatedMatchCrleSrc = MatchCrawleeTestSample.preReg103904NotStartYet;
        preRegWorker.setMatchCrleTestTarget(simulatedMatchCrleSrc);

        Thread.sleep(500);
        System.out.println("preRegWorker matchid: " + preRegWorker.getMatchId());
        System.out.println("preRegWorker commence time: " + preRegWorker.getCommenceTime());
        System.out.println("now is : " + new DateTimeEntity());
        MatchCONSTANTS.MatchStatus expectedStatJustAfterInit = MatchCONSTANTS.MatchStatus.STATE_PRE_REGISTERED;
        MatchCONSTANTS.MatchStage expectedStageJustAfterInit = MatchCONSTANTS.MatchStage.STAGE_ESST;
        Assert.assertEquals(expectedStatJustAfterInit,preRegWorker.getStatus());
        Assert.assertEquals(expectedStageJustAfterInit,preRegWorker.getStage());

        Thread.sleep(1000 * 15);
        System.out.println("Now feed the match turned to start: ");

        simulatedMatchCrleSrc = MatchCrawleeTestSample.preReg103904firstHalf;
        preRegWorker.setMatchCrleTestTarget((simulatedMatchCrleSrc));

        Thread.sleep(2000);
        MatchCONSTANTS.MatchStatus expectedStatJustAfterPreReg = MatchCONSTANTS.MatchStatus.STATE_MATCH_LOGGING;
        Assert.assertEquals(expectedStatJustAfterPreReg,preRegWorker.getStatus());

        Thread.sleep(1000 * 6);

        simulatedMatchCrleSrc = MatchCrawleeTestSample.preReg103904firstHalf2;
        preRegWorker.setMatchCrleTestTarget(simulatedMatchCrleSrc);

        Thread.sleep(1000 * 5);

        simulatedMatchCrleSrc = MatchCrawleeTestSample.PreReg103904CornerSuspend;
        preRegWorker.setMatchCrleTestTarget(simulatedMatchCrleSrc);

        Thread.sleep(1000 * 5);

        simulatedMatchCrleSrc = MatchCrawleeTestSample.PreReg103904firstHalf3;
        preRegWorker.setMatchCrleTestTarget(simulatedMatchCrleSrc);

        Thread.sleep(1000 * 5);

        simulatedMatchCrleSrc = MatchCrawleeTestSample.PreReg103904AllSuspend;
        preRegWorker.setMatchCrleTestTarget(simulatedMatchCrleSrc);

        Thread.sleep(1000 * 5);

        simulatedMatchCrleSrc = MatchCrawleeTestSample.PreReg103904HalfTime;
        preRegWorker.setMatchCrleTestTarget(simulatedMatchCrleSrc);

        Thread.sleep(1000 * 5);

        simulatedMatchCrleSrc = MatchCrawleeTestSample.PreReg103904secondhalfStart;
        preRegWorker.setMatchCrleTestTarget(simulatedMatchCrleSrc);

        Thread.sleep(1000 * 5);

        simulatedMatchCrleSrc = MatchCrawleeTestSample.PreReg103904AllSuspend2;
        preRegWorker.setMatchCrleTestTarget(simulatedMatchCrleSrc);

        Thread.sleep(1000 * 5);

        simulatedMatchCrleSrc = MatchCrawleeTestSample.PreReg103904HADbettingClosed;
        preRegWorker.setMatchCrleTestTarget(simulatedMatchCrleSrc);

        Thread.sleep(1000 * 5);

        simulatedMatchCrleSrc = MatchCrawleeTestSample.PreReg103904allBettingClosed;
        preRegWorker.setMatchCrleTestTarget(simulatedMatchCrleSrc);

        Thread.sleep(1000 * 5);

        simulatedMatchCrleSrc = MatchCrawleeTestSample.secondHalf103904AllpoolsEnd;
        preRegWorker.setMatchCrleTestTarget(simulatedMatchCrleSrc);

        Thread.sleep(1000 * 5);

        System.out.println("[WorkerTester] the setUp() finished");
    }

}