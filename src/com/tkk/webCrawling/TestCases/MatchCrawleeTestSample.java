package com.tkk.webCrawling.TestCases;

/**
 * Created by tsangkk on 7/12/16.
 */
public class MatchCrawleeTestSample {

    public static String testMatchHtml = "<!--?xml version='1.0' ?-->\n" +
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

    static public String preReg103904NotStartYet = "<?xml version='1.0' ?> <SB_INFO IS_REFRESH_REQUIRED='True' NEW_MSN=''><MATCH ID='103904' MATCH_STARTED='False' NTS_ETS_DEFINED='True' IS_BETTING_DELAY_NEED='False' IS_BETTING_DELAY_NEED_TQL='False' MATCH_STAGE='InPlayESST_nobr' SCORE='-1 :  -1' NINETY_MINS_SCORE='' NINETY_MINS_TOTAL_CORNER='-' NTS_DIV='' STATUS='0' VOID='False' HASRESULT='False' INPLAY_POOLS='HAD,HIL,CHL,NTS,CRS' HT_POOLS=''><POOL TYPE='HAD' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' A='100@2.25' D='100@3.45' H='100@2.57' PREV_A='100@2.30' PREV_D='100@3.45' PREV_H='100@2.50' SELL='True' ISINITIAL_ODDS='False' /><POOL TYPE='HIL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='100@1.50' L='100@2.40' PREV_H='100@1.54' PREV_L='100@2.30' SELL='True' ISINITIAL_ODDS='False' LINE='2.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CHL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='100@1.72' L='100@2.00' PREV_H='100@1.72' PREV_L='100@2.00' SELL='True' ISINITIAL_ODDS='False' LINE='9.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CRS' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' M1MA='100@40.00' M1MD='100@100.0' M1MH='100@50.00' S0000='100@16.00' S0001='100@10.00' S0002='100@13.00' S0003='100@22.00' S0004='100@50.00' S0005='100@120.0' S0100='100@10.50' S0101='100@6.75' S0102='100@7.50' S0103='100@16.00' S0104='100@35.00' S0105='100@80.00' S0200='100@15.00' S0201='100@8.50' S0202='100@10.50' S0203='100@18.00' S0204='100@50.00' S0205='100@120.0' S0300='100@26.00' S0301='100@18.00' S0302='100@20.00' S0303='100@32.00' S0400='100@60.00' S0401='100@40.00' S0402='100@50.00' S0500='100@150.0' S0501='100@100.0' S0502='100@120.0' PREV_M1MA='100@40.00' PREV_M1MD='100@125.0' PREV_M1MH='100@50.00' PREV_0000='100@14.00' PREV_0001='100@10.00' PREV_0002='100@13.00' PREV_0003='100@22.00' PREV_0004='100@50.00' PREV_0005='100@120.0' PREV_0100='100@10.50' PREV_0101='100@6.75' PREV_0102='100@7.50' PREV_0103='100@16.00' PREV_0104='100@35.00' PREV_0105='100@80.00' PREV_0200='100@15.00' PREV_0201='100@8.50' PREV_0202='100@10.50' PREV_0203='100@18.00' PREV_0204='100@50.00' PREV_0205='100@120.0' PREV_0300='100@26.00' PREV_0301='100@18.00' PREV_0302='100@20.00' PREV_0303='100@35.00' PREV_0400='100@60.00' PREV_0401='100@40.00' PREV_0402='100@50.00' PREV_0500='100@150.0' PREV_0501='100@100.0' PREV_0502='100@120.0' SELL='True' ISINITIAL_ODDS='False' /></MATCH></SB_INFO>\n" +
            "<!--<html>\n" +
            "<body>\n" +
            "<div id=\"test\"></div>\n" +
            "</body>\n" +
            "</html>-->";
}