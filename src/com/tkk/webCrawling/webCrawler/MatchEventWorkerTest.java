package com.tkk.webCrawling.webCrawler;

import com.tkk.webCrawling.crawlee.BoardCrawlee;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by tsangkk on 7/11/16.
 */
public class MatchEventWorkerTest {
    @Before
    public void setUp() throws Exception {
        HKJCcrawler hkjcCrlr = HKJCcrawler.GetInstance();
        BoardCrawlee boardCrlr = new BoardCrawlee(hkjcCrlr,true);
    }

    @After
    public void tearDown() throws Exception {

    }



}