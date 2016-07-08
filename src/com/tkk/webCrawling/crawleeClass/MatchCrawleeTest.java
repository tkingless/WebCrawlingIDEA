package com.tkk.webCrawling.crawleeClass;

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

        crle = new MatchCrawlee(testMatchHtml);
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

    String testMatchHtml = "<!--?xml version='1.0' ?-->\n" +
            "<html>\n" +
            " <head></head>\n" +
            " <body>\n" +
            "  <sb_info is_refresh_required=\"True\" new_msn=\"\">\n" +
            "   <match id=\"103973\" match_started=\"False\" nts_ets_defined=\"True\" is_betting_delay_need=\"False\" is_betting_delay_need_tql=\"False\" match_stage=\"InPlayESST_nobr\" score=\"-1 :  -1\" ninety_mins_score=\"\" ninety_mins_total_corner=\"-\" nts_div=\"\" status=\"0\" void=\"False\" hasresult=\"False\" inplay_pools=\"HAD,HIL,CHL,NTS,CRS\" ht_pools=\"\">\n" +
            "    <pool type=\"HAD\" in=\"1\" match_pool_status=\"start-sell\" inplay=\"True\" ht_sell=\"False\" a=\"100@2.85\" d=\"100@3.10\" h=\"100@2.22\" prev_a=\"100@2.85\" prev_d=\"100@3.10\" prev_h=\"100@2.22\" sell=\"True\" isinitial_odds=\"False\" />\n" +
            "    <pool type=\"HIL\" in=\"1\" match_pool_status=\"start-sell\" inplay=\"True\" ht_sell=\"False\" h=\"100@2.00\" l=\"100@1.72\" prev_h=\"100@2.00\" prev_l=\"100@1.72\" sell=\"True\" isinitial_odds=\"False\" line=\"2.5\" line_status=\"1\" line_no=\"1\" mainline_flag=\"1\" />\n" +
            "    <pool type=\"CHL\" in=\"1\" match_pool_status=\"start-sell\" inplay=\"True\" ht_sell=\"False\" h=\"100@1.85\" l=\"100@1.85\" prev_h=\"100@1.85\" prev_l=\"100@1.85\" sell=\"True\" isinitial_odds=\"False\" line=\"9.5\" line_status=\"1\" line_no=\"1\" mainline_flag=\"1\" />\n" +
            "    <pool type=\"CRS\" in=\"1\" match_pool_status=\"start-sell\" inplay=\"True\" ht_sell=\"False\" m1ma=\"100@100.0\" m1md=\"100@300.0\" m1mh=\"100@80.00\" s0000=\"100@9.00\" s0001=\"100@8.00\" s0002=\"100@13.00\" s0003=\"100@30.00\" s0004=\"100@80.00\" s0005=\"100@200.0\" s0100=\"100@7.00\" s0101=\"100@6.25\" s0102=\"100@9.00\" s0103=\"100@23.00\" s0104=\"100@70.00\" s0105=\"100@200.0\" s0200=\"100@10.00\" s0201=\"100@7.75\" s0202=\"100@12.00\" s0203=\"100@30.00\" s0204=\"100@80.00\" s0205=\"100@250.0\" s0300=\"100@20.00\" s0301=\"100@18.00\" s0302=\"100@26.00\" s0303=\"100@50.00\" s0400=\"100@50.00\" s0401=\"100@40.00\" s0402=\"100@70.00\" s0500=\"100@120.0\" s0501=\"100@120.0\" s0502=\"100@150.0\" prev_m1ma=\"100@100.0\" prev_m1md=\"100@300.0\" prev_m1mh=\"100@80.00\" prev_0000=\"100@9.00\" prev_0001=\"100@8.00\" prev_0002=\"100@13.00\" prev_0003=\"100@30.00\" prev_0004=\"100@80.00\" prev_0005=\"100@200.0\" prev_0100=\"100@7.00\" prev_0101=\"100@6.25\" prev_0102=\"100@9.00\" prev_0103=\"100@23.00\" prev_0104=\"100@70.00\" prev_0105=\"100@200.0\" prev_0200=\"100@10.00\" prev_0201=\"100@7.75\" prev_0202=\"100@12.00\" prev_0203=\"100@30.00\" prev_0204=\"100@80.00\" prev_0205=\"100@250.0\" prev_0300=\"100@20.00\" prev_0301=\"100@18.00\" prev_0302=\"100@26.00\" prev_0303=\"100@50.00\" prev_0400=\"100@50.00\" prev_0401=\"100@40.00\" prev_0402=\"100@70.00\" prev_0500=\"100@120.0\" prev_0501=\"100@120.0\" prev_0502=\"100@150.0\" sell=\"True\" isinitial_odds=\"False\" />\n" +
            "   </match>\n" +
            "  </sb_info> \n" +
            "  <!--<html>\n" +
            "<body>\n" +
            "<div id=\"test\"></div>\n" +
            "</body>\n" +
            "</html>-->\n" +
            " </body>\n" +
            "</html>";
}