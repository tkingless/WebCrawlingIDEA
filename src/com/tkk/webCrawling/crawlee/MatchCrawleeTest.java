package com.tkk.webCrawling.crawlee;

import com.tkk.webCrawling.MatchCONSTANTS;
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


}