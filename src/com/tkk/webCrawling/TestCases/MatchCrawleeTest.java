package com.tkk.webCrawling.TestCases;

import com.tkk.webCrawling.MatchCONSTANTS;
import com.tkk.webCrawling.crawlee.MatchCrawlee;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.util.EnumSet;
import java.util.Set;

/**
 * Created by tkingless on 7/8/16.
 */
public class MatchCrawleeTest {

    private MatchCrawlee crle;

    @Before
    public void setUp() throws Exception {

        crle = new MatchCrawlee(MatchCrawleeTestSample.testMatchHtml);
        crle.run();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void extractMatchPools() throws Exception {
        Set<MatchCONSTANTS.InplayPoolType> expected = EnumSet.of(MatchCONSTANTS.InplayPoolType.HAD,
                MatchCONSTANTS.InplayPoolType.HIL, MatchCONSTANTS.InplayPoolType.CHL, MatchCONSTANTS.InplayPoolType.NTS,
                MatchCONSTANTS.InplayPoolType.CRS);

        Assert.assertEquals(expected,crle.getPoolType());
    }

    @Test
    public void extractMatchStage() throws Exception {
        MatchCONSTANTS.MatchStage expected = MatchCONSTANTS.MatchStage.STAGE_ESST;

        Assert.assertEquals(expected,crle.getMatchStage());
    }

    @Test
    public void comparableTest() throws Exception {
        MatchCrawlee preRegCrle = new MatchCrawlee(MatchCrawleeTestSample.preReg103904NotStartYet);
        MatchCrawlee startCrle = new MatchCrawlee(MatchCrawleeTestSample.preReg103904firstHalf);

        preRegCrle.run();
        startCrle.run();

        Assert.assertEquals(true,MatchCrawlee.HasUpdate(preRegCrle,startCrle));

        MatchCrawlee preRegCrle2 = new MatchCrawlee(MatchCrawleeTestSample.preReg103904NotStartYet);
        preRegCrle2.run();

        Assert.assertEquals(false,MatchCrawlee.HasUpdate(preRegCrle,preRegCrle2));

        MatchCrawlee nullCrle=null;

        Assert.assertEquals(true,MatchCrawlee.HasUpdate(nullCrle,preRegCrle));

        MatchCrawlee startCrle2 = new MatchCrawlee((MatchCrawleeTestSample.preReg103904firstHalf2));
        startCrle2.run();

        Assert.assertEquals(true,MatchCrawlee.HasUpdate(startCrle,startCrle2));
    }

    @Test
    public void BettingAllClosed() throws Exception {
        MatchCrawlee aCrle = new MatchCrawlee(MatchCrawleeTestSample.secondHalf103904AllpoolsEnd);
        MatchCrawlee aCrle2 = new MatchCrawlee(MatchCrawleeTestSample.preReg103904firstHalf2);
        aCrle.run();
        aCrle2.run();

        Assert.assertEquals(true,aCrle.isAllPoolClosed());
        Assert.assertEquals(false,aCrle2.isAllPoolClosed());
    }

}