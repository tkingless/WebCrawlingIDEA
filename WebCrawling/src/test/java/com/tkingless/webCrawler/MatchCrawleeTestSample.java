package com.tkingless.webCrawler;

/**
 * Created by tsangkk on 7/12/16.
 */
public class MatchCrawleeTestSample {

    //TODO refer to Gradle samples, java/multiprojects/sharing, use resources to parse xml instead

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

    static public String preReg103904NotStartYet = "<?xml version='1.0' ?> <SB_INFO IS_REFRESH_REQUIRED='True' NEW_MSN=''><MATCH ID='103904' MATCH_STARTED='False' NTS_ETS_DEFINED='True' IS_BETTING_DELAY_NEED='False' IS_BETTING_DELAY_NEED_TQL='False' MATCH_STAGE='InPlayESST_nobr' SCORE='-1 :  -1' NINETY_MINS_SCORE='' NINETY_MINS_TOTAL_CORNER='-' NTS_DIV='' STATUS='0' VOID='False' HASRESULT='False' INPLAY_POOLS='HAD,HIL,CHL,NTS,CRS' HT_POOLS=''><POOL TYPE='HAD' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' A='100@2.25' D='100@3.45' H='100@2.57' PREV_A='100@2.30' PREV_D='100@3.45' PREV_H='100@2.50' SELL='True' ISINITIAL_ODDS='False' /><POOL TYPE='HIL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='100@1.50' L='100@2.40' PREV_H='100@1.54' PREV_L='100@2.30' SELL='True' ISINITIAL_ODDS='False' LINE='2.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CHL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='100@1.72' L='100@2.00' PREV_H='100@1.72' PREV_L='100@2.00' SELL='True' ISINITIAL_ODDS='False' LINE='9.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CRS' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' M1MA='100@40.00' M1MD='100@100.0' M1MH='100@50.00' S0000='100@16.00' S0001='100@10.00' S0002='100@13.00' S0003='100@22.00' S0004='100@50.00' S0005='100@120.0' S0100='100@10.50' S0101='100@6.75' S0102='100@7.50' S0103='100@16.00' S0104='100@35.00' S0105='100@80.00' S0200='100@15.00' S0201='100@8.50' S0202='100@10.50' S0203='100@18.00' S0204='100@50.00' S0205='100@120.0' S0300='100@26.00' S0301='100@18.00' S0302='100@20.00' S0303='100@32.00' S0400='100@60.00' S0401='100@40.00' S0402='100@50.00' S0500='100@150.0' S0501='100@100.0' S0502='100@120.0' PREV_M1MA='100@40.00' PREV_M1MD='100@125.0' PREV_M1MH='100@50.00' PREV_0000='100@14.00' PREV_0001='100@10.00' PREV_0002='100@13.00' PREV_0003='100@22.00' PREV_0004='100@50.00' PREV_0005='100@120.0' PREV_0100='100@10.50' PREV_0101='100@6.75' PREV_0102='100@7.50' PREV_0103='100@16.00' PREV_0104='100@35.00' PREV_0105='100@80.00' PREV_0200='100@15.00' PREV_0201='100@8.50' PREV_0202='100@10.50' PREV_0203='100@18.00' PREV_0204='100@50.00' PREV_0205='100@120.0' PREV_0300='100@26.00' PREV_0301='100@18.00' PREV_0302='100@20.00' PREV_0303='100@35.00' PREV_0400='100@60.00' PREV_0401='100@40.00' PREV_0402='100@50.00' PREV_0500='100@150.0' PREV_0501='100@100.0' PREV_0502='100@120.0' SELL='True' ISINITIAL_ODDS='False' /></MATCH></SB_INFO>\n";

    static public String preReg103904firstHalf ="<?xml version='1.0' ?> <SB_INFO IS_REFRESH_REQUIRED='True' NEW_MSN=''><MATCH ID='103904' MATCH_STARTED='True' NTS_ETS_DEFINED='True' IS_BETTING_DELAY_NEED='True' IS_BETTING_DELAY_NEED_TQL='True' MATCH_STAGE='firsthalf' SCORE='0 : 0' NINETY_MINS_SCORE='' NINETY_MINS_TOTAL_CORNER='0' NTS_DIV='' STATUS='0' VOID='False' HASRESULT='False' INPLAY_POOLS='HAD,HIL,NTS,CHL,CRS' HT_POOLS=''><POOL TYPE='HAD' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' A='110@2.24' D='110@3.44' H='110@2.58' PREV_A='100@2.25' PREV_D='100@3.45' PREV_H='100@2.57' SELL='True' ISINITIAL_ODDS='False' /><POOL TYPE='HIL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='110@1.68' L='110@2.05' PREV_H='110@1.68' PREV_L='110@2.05' SELL='True' ISINITIAL_ODDS='False' LINE='2.5/3' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='NTS' IN='1' MATCH_POOL_STATUS='' INPLAY ='True' HT_SELL='False' A='110@1.80' H='110@1.95' N='110@16.00' PREV_A='100@1.81' PREV_H='110@1.95' PREV_N='110@16.00' SELL='False' ISINITIAL_ODDS='True' /><POOL TYPE='CHL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='110@1.75' L='110@1.95' PREV_H='100@1.72' PREV_L='100@2.00' SELL='True' ISINITIAL_ODDS='False' LINE='9.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CRS' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' M1MA='100@40.00' M1MD='100@100.0' M1MH='100@50.00' S0000='100@16.00' S0001='110@9.75' S0002='100@13.00' S0003='100@22.00' S0004='100@50.00' S0005='110@125.0' S0100='100@10.50' S0101='110@6.70' S0102='110@7.60' S0103='100@16.00' S0104='100@35.00' S0105='100@80.00' S0200='100@15.00' S0201='100@8.50' S0202='100@10.50' S0203='100@18.00' S0204='100@50.00' S0205='110@125.0' S0300='100@26.00' S0301='100@18.00' S0302='100@20.00' S0303='110@30.00' S0400='100@60.00' S0401='110@45.00' S0402='100@50.00' S0500='100@150.0' S0501='100@100.0' S0502='110@125.0' PREV_M1MA='100@40.00' PREV_M1MD='100@125.0' PREV_M1MH='100@50.00' PREV_0000='100@14.00' PREV_0001='100@10.00' PREV_0002='100@13.00' PREV_0003='100@22.00' PREV_0004='100@50.00' PREV_0005='100@120.0' PREV_0100='100@10.50' PREV_0101='100@6.75' PREV_0102='100@7.50' PREV_0103='100@16.00' PREV_0104='100@35.00' PREV_0105='100@80.00' PREV_0200='100@15.00' PREV_0201='100@8.50' PREV_0202='100@10.50' PREV_0203='100@18.00' PREV_0204='100@50.00' PREV_0205='100@120.0' PREV_0300='100@26.00' PREV_0301='100@18.00' PREV_0302='100@20.00' PREV_0303='100@32.00' PREV_0400='100@60.00' PREV_0401='100@40.00' PREV_0402='100@50.00' PREV_0500='100@150.0' PREV_0501='100@100.0' PREV_0502='100@120.0' SELL='True' ISINITIAL_ODDS='False' /></MATCH></SB_INFO>";

    static public String preReg103904firstHalf2 ="<?xml version='1.0' ?> <SB_INFO IS_REFRESH_REQUIRED='True' NEW_MSN=''><MATCH ID='103904' MATCH_STARTED='True' NTS_ETS_DEFINED='True' IS_BETTING_DELAY_NEED='True' IS_BETTING_DELAY_NEED_TQL='True' MATCH_STAGE='firsthalf' SCORE='0 : 0' NINETY_MINS_SCORE='' NINETY_MINS_TOTAL_CORNER='0' NTS_DIV='' STATUS='0' VOID='False' HASRESULT='False' INPLAY_POOLS='HAD,HIL,NTS,CHL,CRS' HT_POOLS=''><POOL TYPE='HAD' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' A='110@2.24' D='110@3.44' H='110@2.58' PREV_A='100@2.25' PREV_D='100@3.45' PREV_H='100@2.57' SELL='True' ISINITIAL_ODDS='False' /><POOL TYPE='HIL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='110@1.68' L='110@2.05' PREV_H='110@1.68' PREV_L='110@2.05' SELL='True' ISINITIAL_ODDS='False' LINE='2.5/3' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='NTS' IN='1' MATCH_POOL_STATUS='' INPLAY ='True' HT_SELL='False' A='110@1.80' H='110@1.95' N='110@16.00' PREV_A='100@1.81' PREV_H='110@1.95' PREV_N='110@16.00' SELL='False' ISINITIAL_ODDS='True' /><POOL TYPE='CHL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='110@1.85' L='110@1.95' PREV_H='100@1.72' PREV_L='100@2.00' SELL='True' ISINITIAL_ODDS='False' LINE='9.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CRS' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' M1MA='100@40.00' M1MD='100@100.0' M1MH='100@50.00' S0000='100@16.00' S0001='110@9.75' S0002='100@13.00' S0003='100@22.00' S0004='100@50.00' S0005='110@125.0' S0100='100@10.50' S0101='110@6.70' S0102='110@7.60' S0103='100@16.00' S0104='100@35.00' S0105='100@80.00' S0200='100@15.00' S0201='100@8.50' S0202='100@10.50' S0203='100@18.00' S0204='100@50.00' S0205='110@125.0' S0300='100@26.00' S0301='100@18.00' S0302='100@20.00' S0303='110@30.00' S0400='100@60.00' S0401='110@45.00' S0402='100@50.00' S0500='100@150.0' S0501='100@100.0' S0502='110@125.0' PREV_M1MA='100@40.00' PREV_M1MD='100@125.0' PREV_M1MH='100@50.00' PREV_0000='100@14.00' PREV_0001='100@10.00' PREV_0002='100@13.00' PREV_0003='100@22.00' PREV_0004='100@50.00' PREV_0005='100@120.0' PREV_0100='100@10.50' PREV_0101='100@6.75' PREV_0102='100@7.50' PREV_0103='100@16.00' PREV_0104='100@35.00' PREV_0105='100@80.00' PREV_0200='100@15.00' PREV_0201='100@8.50' PREV_0202='100@10.50' PREV_0203='100@18.00' PREV_0204='100@50.00' PREV_0205='100@120.0' PREV_0300='100@26.00' PREV_0301='100@18.00' PREV_0302='100@20.00' PREV_0303='100@32.00' PREV_0400='100@60.00' PREV_0401='100@40.00' PREV_0402='100@50.00' PREV_0500='100@150.0' PREV_0501='100@100.0' PREV_0502='100@120.0' SELL='True' ISINITIAL_ODDS='False' /></MATCH></SB_INFO>";

    static public String PreReg103904CornerSuspend = "<?xml version='1.0' ?> <SB_INFO IS_REFRESH_REQUIRED='True' NEW_MSN=''><MATCH ID='103904' MATCH_STARTED='True' NTS_ETS_DEFINED='True' IS_BETTING_DELAY_NEED='True' IS_BETTING_DELAY_NEED_TQL='True' MATCH_STAGE='firsthalf' SCORE='0 : 0' NINETY_MINS_SCORE='' NINETY_MINS_TOTAL_CORNER='---' NTS_DIV='' STATUS='0' VOID='False' HASRESULT='False' INPLAY_POOLS='HAD,HIL,NTS,CHL,CRS' HT_POOLS=''><POOL TYPE='HAD' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' A='110@2.26' D='110@3.35' H='110@2.60' PREV_A='100@2.24' PREV_D='100@3.40' PREV_H='100@2.58' SELL='True' ISINITIAL_ODDS='False' /><POOL TYPE='HIL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='110@1.80' L='110@1.90' PREV_H='100@1.73' PREV_L='100@1.98' SELL='True' ISINITIAL_ODDS='False' LINE='2.5/3' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='NTS' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' A='110@1.82' H='110@1.96' N='110@14.00' PREV_A='100@1.81' PREV_H='100@1.95' PREV_N='100@15.00' SELL='True' ISINITIAL_ODDS='False' /><POOL TYPE='CHL' IN='1' MATCH_POOL_STATUS='suspended' INPLAY ='True' HT_SELL='False' H='110@1.79' L='110@1.91' PREV_H='100@1.72' PREV_L='100@2.00' SELL='False' ISINITIAL_ODDS='False' LINE='10.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CRS' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' M1MA='110@50.00' M1MD='110@150.0' M1MH='110@60.00' S0000='110@14.00' S0001='110@9.00' S0002='110@12.50' S0003='100@22.00' S0004='100@50.00' S0005='110@150.0' S0100='110@9.75' S0101='110@6.00' S0102='100@7.60' S0103='110@17.00' S0104='100@35.00' S0105='110@100.0' S0200='110@14.50' S0201='100@8.50' S0202='100@10.50' S0203='110@20.00' S0204='110@60.00' S0205='110@150.0' S0300='110@27.00' S0301='100@18.00' S0302='110@22.00' S0303='110@40.00' S0400='110@70.00' S0401='100@45.00' S0402='110@60.00' S0500='110@200.0' S0501='110@150.0' S0502='110@150.0' PREV_M1MA='100@45.00' PREV_M1MD='100@125.0' PREV_M1MH='100@50.00' PREV_0000='100@15.00' PREV_0001='100@9.50' PREV_0002='100@13.00' PREV_0003='100@22.00' PREV_0004='100@50.00' PREV_0005='100@125.0' PREV_0100='100@10.50' PREV_0101='100@6.40' PREV_0102='100@7.50' PREV_0103='100@16.00' PREV_0104='100@35.00' PREV_0105='100@90.00' PREV_0200='100@15.00' PREV_0201='100@8.50' PREV_0202='100@10.50' PREV_0203='100@19.00' PREV_0204='100@50.00' PREV_0205='100@125.0' PREV_0300='100@26.00' PREV_0301='100@18.00' PREV_0302='100@21.00' PREV_0303='100@35.00' PREV_0400='100@60.00' PREV_0401='100@40.00' PREV_0402='100@50.00' PREV_0500='100@150.0' PREV_0501='100@125.0' PREV_0502='100@125.0' SELL='True' ISINITIAL_ODDS='False' /></MATCH></SB_INFO>";

    static public String PreReg103904firstHalf3 = "<?xml version='1.0' ?> <SB_INFO IS_REFRESH_REQUIRED='True' NEW_MSN=''><MATCH ID='103904' MATCH_STARTED='True' NTS_ETS_DEFINED='True' IS_BETTING_DELAY_NEED='True' IS_BETTING_DELAY_NEED_TQL='True' MATCH_STAGE='firsthalf' SCORE='0 : 0' NINETY_MINS_SCORE='' NINETY_MINS_TOTAL_CORNER='---' NTS_DIV='' STATUS='0' VOID='False' HASRESULT='False' INPLAY_POOLS='HAD,HIL,NTS,CHL,CRS' HT_POOLS=''><POOL TYPE='HAD' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' A='110@2.36' D='110@3.39' H='110@3.60' PREV_A='100@2.24' PREV_D='100@3.40' PREV_H='100@2.58' SELL='True' ISINITIAL_ODDS='False' /><POOL TYPE='HIL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='110@1.80' L='110@1.90' PREV_H='100@1.73' PREV_L='100@1.98' SELL='True' ISINITIAL_ODDS='False' LINE='2.5/3' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='NTS' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' A='110@1.82' H='110@1.96' N='110@14.00' PREV_A='100@1.81' PREV_H='100@1.95' PREV_N='100@15.00' SELL='True' ISINITIAL_ODDS='False' /><POOL TYPE='CHL' IN='1' MATCH_POOL_STATUS='suspended' INPLAY ='True' HT_SELL='False' H='110@1.79' L='110@1.91' PREV_H='100@1.72' PREV_L='100@2.00' SELL='False' ISINITIAL_ODDS='False' LINE='10.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CRS' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' M1MA='110@50.00' M1MD='110@150.0' M1MH='110@60.00' S0000='110@14.00' S0001='110@9.00' S0002='110@12.50' S0003='100@22.00' S0004='100@50.00' S0005='110@150.0' S0100='110@9.75' S0101='110@6.00' S0102='100@7.60' S0103='110@17.00' S0104='100@35.00' S0105='110@100.0' S0200='110@14.50' S0201='100@8.50' S0202='100@10.50' S0203='110@20.00' S0204='110@60.00' S0205='110@150.0' S0300='110@27.00' S0301='100@18.00' S0302='110@22.00' S0303='110@40.00' S0400='110@70.00' S0401='100@45.00' S0402='110@60.00' S0500='110@200.0' S0501='110@150.0' S0502='110@150.0' PREV_M1MA='100@45.00' PREV_M1MD='100@125.0' PREV_M1MH='100@50.00' PREV_0000='100@15.00' PREV_0001='100@9.50' PREV_0002='100@13.00' PREV_0003='100@22.00' PREV_0004='100@50.00' PREV_0005='100@125.0' PREV_0100='100@10.50' PREV_0101='100@6.40' PREV_0102='100@7.50' PREV_0103='100@16.00' PREV_0104='100@35.00' PREV_0105='100@90.00' PREV_0200='100@15.00' PREV_0201='100@8.50' PREV_0202='100@10.50' PREV_0203='100@19.00' PREV_0204='100@50.00' PREV_0205='100@125.0' PREV_0300='100@26.00' PREV_0301='100@18.00' PREV_0302='100@21.00' PREV_0303='100@35.00' PREV_0400='100@60.00' PREV_0401='100@40.00' PREV_0402='100@50.00' PREV_0500='100@150.0' PREV_0501='100@125.0' PREV_0502='100@125.0' SELL='True' ISINITIAL_ODDS='False' /></MATCH></SB_INFO>";

    static public String PreReg103904AllSuspend = "<?xml version='1.0' ?> <SB_INFO IS_REFRESH_REQUIRED='True' NEW_MSN=''><MATCH ID='103904' MATCH_STARTED='True' NTS_ETS_DEFINED='True' IS_BETTING_DELAY_NEED='True' IS_BETTING_DELAY_NEED_TQL='True' MATCH_STAGE='firsthalf' SCORE='0 : 0' NINETY_MINS_SCORE='' NINETY_MINS_TOTAL_CORNER='---' NTS_DIV='' STATUS='0' VOID='False' HASRESULT='False' INPLAY_POOLS='HAD,HIL,NTS,CHL,CRS' HT_POOLS=''><POOL TYPE='HAD' IN='1' MATCH_POOL_STATUS='suspended' INPLAY ='True' HT_SELL='False' A='110@2.26' D='110@3.35' H='110@2.60' PREV_A='100@2.24' PREV_D='100@3.40' PREV_H='100@2.58' SELL='False' ISINITIAL_ODDS='False' /><POOL TYPE='HIL' IN='1' MATCH_POOL_STATUS='suspended' INPLAY ='True' HT_SELL='False' H='110@1.80' L='110@1.90' PREV_H='100@1.73' PREV_L='100@1.98' SELL='False' ISINITIAL_ODDS='False' LINE='2.5/3' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='NTS' IN='1' MATCH_POOL_STATUS='suspended' INPLAY ='True' HT_SELL='False' A='110@1.82' H='110@1.96' N='110@14.00' PREV_A='100@1.81' PREV_H='100@1.95' PREV_N='100@15.00' SELL='False' ISINITIAL_ODDS='False' /><POOL TYPE='CHL' IN='1' MATCH_POOL_STATUS='suspended' INPLAY ='True' HT_SELL='False' H='110@1.75' L='110@1.95' PREV_H='110@1.75' PREV_L='110@1.95' SELL='False' ISINITIAL_ODDS='False' LINE='11.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CRS' IN='1' MATCH_POOL_STATUS='suspended' INPLAY ='True' HT_SELL='False' M1MA='110@50.00' M1MD='110@150.0' M1MH='110@60.00' S0000='110@14.00' S0001='110@9.00' S0002='110@12.50' S0003='100@22.00' S0004='100@50.00' S0005='110@150.0' S0100='110@9.75' S0101='110@6.00' S0102='100@7.60' S0103='110@17.00' S0104='100@35.00' S0105='110@100.0' S0200='110@14.50' S0201='100@8.50' S0202='100@10.50' S0203='110@20.00' S0204='110@60.00' S0205='110@150.0' S0300='110@27.00' S0301='100@18.00' S0302='110@22.00' S0303='110@40.00' S0400='110@70.00' S0401='100@45.00' S0402='110@60.00' S0500='110@200.0' S0501='110@150.0' S0502='110@150.0' PREV_M1MA='100@45.00' PREV_M1MD='100@125.0' PREV_M1MH='100@50.00' PREV_0000='100@15.00' PREV_0001='100@9.50' PREV_0002='100@13.00' PREV_0003='100@22.00' PREV_0004='100@50.00' PREV_0005='100@125.0' PREV_0100='100@10.50' PREV_0101='100@6.40' PREV_0102='100@7.50' PREV_0103='100@16.00' PREV_0104='100@35.00' PREV_0105='100@90.00' PREV_0200='100@15.00' PREV_0201='100@8.50' PREV_0202='100@10.50' PREV_0203='100@19.00' PREV_0204='100@50.00' PREV_0205='100@125.0' PREV_0300='100@26.00' PREV_0301='100@18.00' PREV_0302='100@21.00' PREV_0303='100@35.00' PREV_0400='100@60.00' PREV_0401='100@40.00' PREV_0402='100@50.00' PREV_0500='100@150.0' PREV_0501='100@125.0' PREV_0502='100@125.0' SELL='False' ISINITIAL_ODDS='False' /></MATCH></SB_INFO>";

    static public String PreReg103904HalfTime = "<?xml version='1.0' ?> <SB_INFO IS_REFRESH_REQUIRED='True' NEW_MSN=''><MATCH ID='103904' MATCH_STARTED='True' NTS_ETS_DEFINED='True' IS_BETTING_DELAY_NEED='False' IS_BETTING_DELAY_NEED_TQL='False' MATCH_STAGE='halftimecompleted' SCORE='1 : 0' NINETY_MINS_SCORE='' NINETY_MINS_TOTAL_CORNER='7' NTS_DIV='NTS:1:home' STATUS='0' VOID='False' HASRESULT='False' INPLAY_POOLS='HAD,HIL,NTS,CHL,CRS' HT_POOLS=''><POOL TYPE='HAD' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' A='110@5.70' D='100@3.70' H='110@1.47' PREV_A='100@5.40' PREV_D='100@3.80' PREV_H='100@1.49' SELL='True' ISINITIAL_ODDS='False' /><POOL TYPE='HIL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='110@1.78' L='110@1.92' PREV_H='100@1.73' PREV_L='100@1.98' SELL='True' ISINITIAL_ODDS='False' LINE='2.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='NTS' IN='2' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' A='110@1.98' H='110@2.32' N='110@5.10' PREV_A='100@1.95' PREV_H='100@2.29' PREV_N='100@5.50' SELL='True' ISINITIAL_ODDS='False' /><POOL TYPE='CHL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='110@1.81' L='110@1.89' PREV_H='100@1.74' PREV_L='100@1.97' SELL='True' ISINITIAL_ODDS='False' LINE='12.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CRS' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' M1MA='110@200.0' M1MD='110@600.0' M1MH='110@100.0' S0000='101@---' S0001='101@---' S0002='101@---' S0003='101@---' S0004='101@---' S0005='101@---' S0100='110@5.10' S0101='110@4.45' S0102='110@8.50' S0103='110@27.00' S0104='110@100.0' S0105='110@400.0' S0200='110@5.00' S0201='110@5.20' S0202='110@10.50' S0203='110@35.00' S0204='110@125.0' S0205='110@450.0' S0300='110@12.50' S0301='110@13.00' S0302='110@27.00' S0303='110@80.00' S0400='110@50.00' S0401='110@45.00' S0402='110@100.0' S0500='110@200.0' S0501='110@200.0' S0502='110@350.0' PREV_M1MA='100@150.0' PREV_M1MD='100@500.0' PREV_M1MH='100@90.00' PREV_0000='100@15.00' PREV_0001='100@9.50' PREV_0002='100@13.00' PREV_0003='101@22.00' PREV_0004='101@50.00' PREV_0005='100@125.0' PREV_0100='100@5.50' PREV_0101='100@4.55' PREV_0102='100@8.25' PREV_0103='100@25.00' PREV_0104='100@90.00' PREV_0105='100@350.0' PREV_0200='100@5.10' PREV_0201='100@5.10' PREV_0202='100@9.75' PREV_0203='100@30.00' PREV_0204='100@100.0' PREV_0205='100@400.0' PREV_0300='100@12.00' PREV_0301='100@12.50' PREV_0302='100@25.00' PREV_0303='100@70.00' PREV_0400='100@45.00' PREV_0401='100@40.00' PREV_0402='100@90.00' PREV_0500='100@150.0' PREV_0501='100@150.0' PREV_0502='100@300.0' SELL='True' ISINITIAL_ODDS='False' /></MATCH></SB_INFO>";

    static public String PreReg103904secondhalfStart = "<?xml version='1.0' ?> <SB_INFO IS_REFRESH_REQUIRED='True' NEW_MSN=''><MATCH ID='103904' MATCH_STARTED='True' NTS_ETS_DEFINED='True' IS_BETTING_DELAY_NEED='True' IS_BETTING_DELAY_NEED_TQL='True' MATCH_STAGE='secondhalf' SCORE='1 : 0' NINETY_MINS_SCORE='' NINETY_MINS_TOTAL_CORNER='7' NTS_DIV='NTS:1:home' STATUS='0' VOID='False' HASRESULT='False' INPLAY_POOLS='HAD,HIL,NTS,CHL,CRS' HT_POOLS=''><POOL TYPE='HAD' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' A='110@6.10' D='100@3.70' H='110@1.44' PREV_A='100@5.70' PREV_D='100@3.80' PREV_H='100@1.47' SELL='True' ISINITIAL_ODDS='False' /><POOL TYPE='HIL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='110@1.85' L='110@1.85' PREV_H='100@1.78' PREV_L='100@1.92' SELL='True' ISINITIAL_ODDS='False' LINE='2.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='NTS' IN='2' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' A='110@2.01' H='110@2.36' N='110@4.75' PREV_A='100@1.98' PREV_H='100@2.32' PREV_N='100@5.10' SELL='True' ISINITIAL_ODDS='False' /><POOL TYPE='CHL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='110@1.95' L='110@1.75' PREV_H='100@1.88' PREV_L='100@1.82' SELL='True' ISINITIAL_ODDS='False' LINE='12.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CRS' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' M1MA='110@250.0' M1MD='100@600.0' M1MH='100@100.0' S0000='101@---' S0001='101@---' S0002='101@---' S0003='101@---' S0004='101@---' S0005='101@---' S0100='110@4.75' S0101='110@4.40' S0102='110@8.75' S0103='110@29.00' S0104='110@125.0' S0105='110@450.0' S0200='110@4.90' S0201='110@5.30' S0202='110@11.00' S0203='100@35.00' S0204='110@150.0' S0205='110@500.0' S0300='110@13.00' S0301='110@14.00' S0302='110@30.00' S0303='110@100.0' S0400='100@50.00' S0401='110@50.00' S0402='100@100.0' S0500='100@200.0' S0501='110@250.0' S0502='110@400.0' PREV_M1MA='100@200.0' PREV_M1MD='100@500.0' PREV_M1MH='100@90.00' PREV_0000='100@15.00' PREV_0001='100@9.50' PREV_0002='100@13.00' PREV_0003='101@22.00' PREV_0004='101@50.00' PREV_0005='100@125.0' PREV_0100='100@5.10' PREV_0101='100@4.45' PREV_0102='100@8.50' PREV_0103='100@27.00' PREV_0104='100@100.0' PREV_0105='100@400.0' PREV_0200='100@5.00' PREV_0201='100@5.20' PREV_0202='100@10.50' PREV_0203='100@30.00' PREV_0204='100@125.0' PREV_0205='100@450.0' PREV_0300='100@12.50' PREV_0301='100@13.00' PREV_0302='100@27.00' PREV_0303='100@80.00' PREV_0400='100@45.00' PREV_0401='100@45.00' PREV_0402='100@90.00' PREV_0500='100@150.0' PREV_0501='100@200.0' PREV_0502='100@350.0' SELL='True' ISINITIAL_ODDS='False' /></MATCH></SB_INFO>";

    static public String PreReg103904AllSuspend2 = "<?xml version='1.0' ?> <SB_INFO IS_REFRESH_REQUIRED='True' NEW_MSN=''><MATCH ID='103904' MATCH_STARTED='True' NTS_ETS_DEFINED='True' IS_BETTING_DELAY_NEED='True' IS_BETTING_DELAY_NEED_TQL='True' MATCH_STAGE='secondhalf' SCORE='2 : 0' NINETY_MINS_SCORE='' NINETY_MINS_TOTAL_CORNER='---' NTS_DIV='NTS:1:home' STATUS='0' VOID='False' HASRESULT='False' INPLAY_POOLS='HAD,HIL,NTS,CHL,CRS' HT_POOLS=''><POOL TYPE='HAD' IN='1' MATCH_POOL_STATUS='suspended' INPLAY ='True' HT_SELL='False' A='110@27.00' D='110@11.00' H='110@1.05' PREV_A='100@9.25' PREV_D='100@3.75' PREV_H='100@1.33' SELL='False' ISINITIAL_ODDS='False' /><POOL TYPE='HIL' IN='1' MATCH_POOL_STATUS='suspended' INPLAY ='True' HT_SELL='False' H='110@1.64' L='110@2.12' PREV_H='110@1.64' PREV_L='110@2.12' SELL='False' ISINITIAL_ODDS='False' LINE='2.5/3' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='NTS' IN='2' MATCH_POOL_STATUS='suspended' INPLAY ='True' HT_SELL='False' A='110@2.49' H='110@3.04' N='110@2.49' PREV_A='100@2.44' PREV_H='100@2.98' PREV_N='100@2.60' SELL='False' ISINITIAL_ODDS='False' /><POOL TYPE='CHL' IN='1' MATCH_POOL_STATUS='suspended' INPLAY ='True' HT_SELL='False' H='110@2.29' L='110@1.55' PREV_H='100@2.15' PREV_L='100@1.62' SELL='False' ISINITIAL_ODDS='False' LINE='11.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CRS' IN='1' MATCH_POOL_STATUS='suspended' INPLAY ='True' HT_SELL='False' M1MA='110@300.0' M1MD='110@1000' M1MH='110@250.0' S0000='101@---' S0001='101@---' S0002='101@---' S0003='101@---' S0004='101@---' S0005='101@---' S0100='111@---' S0101='111@---' S0102='111@---' S0103='111@---' S0104='111@---' S0105='111@---' S0200='110@2.49' S0201='110@3.65' S0202='110@11.50' S0203='110@28.00' S0204='110@150.0' S0205='110@1000' S0300='110@4.50' S0301='110@7.80' S0302='110@28.00' S0303='110@125.0' S0400='110@20.00' S0401='110@35.00' S0402='110@125.0' S0500='110@125.0' S0501='110@250.0' S0502='110@800.0' PREV_M1MA='100@1000' PREV_M1MD='100@900.0' PREV_M1MH='100@700.0' PREV_0000='100@15.00' PREV_0001='100@9.50' PREV_0002='100@13.00' PREV_0003='101@22.00' PREV_0004='101@50.00' PREV_0005='100@125.0' PREV_0100='100@2.60' PREV_0101='100@4.00' PREV_0102='100@10.00' PREV_0103='100@50.00' PREV_0104='100@400.0' PREV_0105='100@800.0' PREV_0200='100@4.50' PREV_0201='100@7.80' PREV_0202='100@26.00' PREV_0203='100@125.0' PREV_0204='100@1000' PREV_0205='100@900.0' PREV_0300='100@20.00' PREV_0301='100@35.00' PREV_0302='100@125.0' PREV_0303='100@600.0' PREV_0400='100@125.0' PREV_0401='100@250.0' PREV_0402='100@900.0' PREV_0500='100@1000' PREV_0501='100@1000' PREV_0502='100@1000' SELL='False' ISINITIAL_ODDS='False' /></MATCH></SB_INFO>";

    static public String PreReg103904HADbettingClosed = "<?xml version='1.0' ?> <SB_INFO IS_REFRESH_REQUIRED='True' NEW_MSN=''><MATCH ID='103904' MATCH_STARTED='True' NTS_ETS_DEFINED='True' IS_BETTING_DELAY_NEED='True' IS_BETTING_DELAY_NEED_TQL='True' MATCH_STAGE='secondhalf' SCORE='2 : 0' NINETY_MINS_SCORE='' NINETY_MINS_TOTAL_CORNER='10' NTS_DIV='NTS:1:home,NTS:2:home' STATUS='0' VOID='False' HASRESULT='False' INPLAY_POOLS='HAD,HIL,NTS,CHL,CRS' HT_POOLS=''><POOL TYPE='HAD' IN='1' MATCH_POOL_STATUS='bettingclosed' INPLAY ='True' HT_SELL='False' A='110@100.0' D='110@21.00' H='110@1.001' PREV_A='100@90.00' PREV_D='100@17.00' PREV_H='100@1.005' SELL='False' ISINITIAL_ODDS='False' /><POOL TYPE='HIL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='110@3.20' L='110@1.30' PREV_H='100@3.00' PREV_L='100@1.33' SELL='True' ISINITIAL_ODDS='False' LINE='2.5/3' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='NTS' IN='3' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' A='110@5.50' H='110@3.50' N='110@1.50' PREV_A='100@5.70' PREV_H='100@3.60' PREV_N='100@1.47' SELL='True' ISINITIAL_ODDS='False' /><POOL TYPE='CHL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='110@1.98' L='110@1.73' PREV_H='100@1.87' PREV_L='100@1.83' SELL='True' ISINITIAL_ODDS='False' LINE='11.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CRS' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' M1MA='110@1000' M1MD='100@1000' M1MH='110@1000' S0000='101@---' S0001='101@---' S0002='101@---' S0003='101@---' S0004='101@---' S0005='101@---' S0100='101@---' S0101='101@---' S0102='101@---' S0103='101@---' S0104='101@---' S0105='101@---' S0200='110@1.50' S0201='110@6.50' S0202='110@40.00' S0203='110@500.0' S0204='110@1000' S0205='100@1000' S0300='110@4.00' S0301='110@21.00' S0302='110@200.0' S0303='110@900.0' S0400='110@28.00' S0401='110@125.0' S0402='110@900.0' S0500='110@250.0' S0501='110@900.0' S0502='100@1000' PREV_M1MA='100@900.0' PREV_M1MD='100@900.0' PREV_M1MH='100@600.0' PREV_0000='100@15.00' PREV_0001='100@9.50' PREV_0002='100@13.00' PREV_0003='101@22.00' PREV_0004='101@50.00' PREV_0005='100@125.0' PREV_0100='100@2.60' PREV_0101='100@4.00' PREV_0102='100@10.00' PREV_0103='100@50.00' PREV_0104='100@400.0' PREV_0105='100@800.0' PREV_0200='100@1.47' PREV_0201='100@6.70' PREV_0202='100@21.10' PREV_0203='100@150.0' PREV_0204='100@800.0' PREV_0205='100@900.0' PREV_0300='100@4.15' PREV_0301='100@19.00' PREV_0302='100@150.0' PREV_0303='100@800.0' PREV_0400='100@26.00' PREV_0401='100@100.0' PREV_0402='100@800.0' PREV_0500='100@200.0' PREV_0501='100@800.0' PREV_0502='100@900.0' SELL='True' ISINITIAL_ODDS='False' /></MATCH></SB_INFO>";

    static public String PreReg103904allBettingClosed = "<?xml version='1.0' ?> <SB_INFO IS_REFRESH_REQUIRED='True' NEW_MSN=''><MATCH ID='103904' MATCH_STARTED='True' NTS_ETS_DEFINED='True' IS_BETTING_DELAY_NEED='True' IS_BETTING_DELAY_NEED_TQL='True' MATCH_STAGE='secondhalf' SCORE='2 : 0' NINETY_MINS_SCORE='' NINETY_MINS_TOTAL_CORNER='---' NTS_DIV='NTS:1:home,NTS:2:home' STATUS='0' VOID='False' HASRESULT='False' INPLAY_POOLS='HAD,HIL,NTS,CHL,CRS' HT_POOLS=''><POOL TYPE='HAD' IN='1' MATCH_POOL_STATUS='bettingclosed' INPLAY ='True' HT_SELL='False' A='100@100.0' D='100@21.00' H='100@1.001' PREV_A='100@90.00' PREV_D='100@17.00' PREV_H='100@1.005' SELL='False' ISINITIAL_ODDS='False' /><POOL TYPE='HIL' IN='1' MATCH_POOL_STATUS='bettingclosed' INPLAY ='True' HT_SELL='False' H='110@6.40' L='110@1.08' PREV_H='100@5.75' PREV_L='100@1.10' SELL='False' ISINITIAL_ODDS='False' LINE='2.5/3' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='NTS' IN='3' MATCH_POOL_STATUS='bettingclosed' INPLAY ='True' HT_SELL='False' A='110@13.00' H='110@8.50' N='110@1.08' PREV_A='100@11.50' PREV_H='100@7.40' PREV_N='100@1.11' SELL='False' ISINITIAL_ODDS='False' /><POOL TYPE='CHL' IN='1' MATCH_POOL_STATUS='bettingclosed' INPLAY ='True' HT_SELL='False' H='110@4.90' L='110@1.14' PREV_H='100@4.10' PREV_L='100@1.19' SELL='False' ISINITIAL_ODDS='False' LINE='15.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CRS' IN='1' MATCH_POOL_STATUS='bettingclosed' INPLAY ='True' HT_SELL='False' M1MA='100@1000' M1MD='100@1000' M1MH='100@1000' S0000='101@---' S0001='101@---' S0002='101@---' S0003='101@---' S0004='101@---' S0005='101@---' S0100='101@---' S0101='101@---' S0102='101@---' S0103='101@---' S0104='101@---' S0105='101@---' S0200='110@1.08' S0201='110@13.50' S0202='110@250.0' S0203='110@1000' S0204='100@1000' S0205='100@1000' S0300='110@8.60' S0301='110@50.00' S0302='110@800.0' S0303='100@1000' S0400='110@90.00' S0401='110@700.0' S0402='100@1000' S0500='110@900.0' S0501='100@1000' S0502='100@1000' PREV_M1MA='100@900.0' PREV_M1MD='100@900.0' PREV_M1MH='100@600.0' PREV_0000='100@15.00' PREV_0001='100@9.50' PREV_0002='100@13.00' PREV_0003='101@22.00' PREV_0004='101@50.00' PREV_0005='100@125.0' PREV_0100='100@2.60' PREV_0101='100@4.00' PREV_0102='100@10.00' PREV_0103='100@50.00' PREV_0104='100@400.0' PREV_0105='100@800.0' PREV_0200='100@1.11' PREV_0201='100@12.50' PREV_0202='100@200.0' PREV_0203='100@900.0' PREV_0204='100@800.0' PREV_0205='100@900.0' PREV_0300='100@7.80' PREV_0301='100@45.00' PREV_0302='100@700.0' PREV_0303='100@900.0' PREV_0400='100@70.00' PREV_0401='100@600.0' PREV_0402='100@900.0' PREV_0500='100@800.0' PREV_0501='100@900.0' PREV_0502='100@900.0' SELL='False' ISINITIAL_ODDS='False' /></MATCH></SB_INFO>";

    static public String secondHalf103904AllpoolsEnd =  "<?xml version='1.0' ?> <SB_INFO IS_REFRESH_REQUIRED='True' NEW_MSN=''><MATCH ID='103904' MATCH_STARTED='True' NTS_ETS_DEFINED='True' IS_BETTING_DELAY_NEED='True' IS_BETTING_DELAY_NEED_TQL='True' MATCH_STAGE='secondhalf' SCORE='2 : 0' NINETY_MINS_SCORE='' NINETY_MINS_TOTAL_CORNER='---' NTS_DIV='NTS:1:home,NTS:2:home' STATUS='0' VOID='False' HASRESULT='False' INPLAY_POOLS='HAD,HIL,NTS,CHL,CRS' HT_POOLS=''><POOL TYPE='HAD' IN='1' MATCH_POOL_STATUS='bettingclosed' INPLAY ='True' HT_SELL='False' A='100@100.0' D='100@21.00' H='100@1.001' PREV_A='100@90.00' PREV_D='100@17.00' PREV_H='100@1.005' SELL='False' ISINITIAL_ODDS='False' /><POOL TYPE='HIL' IN='1' MATCH_POOL_STATUS='bettingclosed' INPLAY ='True' HT_SELL='False' H='110@6.40' L='110@1.08' PREV_H='100@5.75' PREV_L='100@1.10' SELL='False' ISINITIAL_ODDS='False' LINE='2.5/3' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='NTS' IN='3' MATCH_POOL_STATUS='bettingclosed' INPLAY ='True' HT_SELL='False' A='110@13.00' H='110@8.50' N='110@1.08' PREV_A='100@11.50' PREV_H='100@7.40' PREV_N='100@1.11' SELL='False' ISINITIAL_ODDS='False' /><POOL TYPE='CHL' IN='1' MATCH_POOL_STATUS='bettingclosed' INPLAY ='True' HT_SELL='False' H='110@4.90' L='110@1.14' PREV_H='100@4.10' PREV_L='100@1.19' SELL='False' ISINITIAL_ODDS='False' LINE='15.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CRS' IN='1' MATCH_POOL_STATUS='bettingclosed' INPLAY ='True' HT_SELL='False' M1MA='100@1000' M1MD='100@1000' M1MH='100@1000' S0000='101@---' S0001='101@---' S0002='101@---' S0003='101@---' S0004='101@---' S0005='101@---' S0100='101@---' S0101='101@---' S0102='101@---' S0103='101@---' S0104='101@---' S0105='101@---' S0200='110@1.08' S0201='110@13.50' S0202='110@250.0' S0203='110@1000' S0204='100@1000' S0205='100@1000' S0300='110@8.60' S0301='110@50.00' S0302='110@800.0' S0303='100@1000' S0400='110@90.00' S0401='110@700.0' S0402='100@1000' S0500='110@900.0' S0501='100@1000' S0502='100@1000' PREV_M1MA='100@900.0' PREV_M1MD='100@900.0' PREV_M1MH='100@600.0' PREV_0000='100@15.00' PREV_0001='100@9.50' PREV_0002='100@13.00' PREV_0003='101@22.00' PREV_0004='101@50.00' PREV_0005='100@125.0' PREV_0100='100@2.60' PREV_0101='100@4.00' PREV_0102='100@10.00' PREV_0103='100@50.00' PREV_0104='100@400.0' PREV_0105='100@800.0' PREV_0200='100@1.11' PREV_0201='100@12.50' PREV_0202='100@200.0' PREV_0203='100@900.0' PREV_0204='100@800.0' PREV_0205='100@900.0' PREV_0300='100@7.80' PREV_0301='100@45.00' PREV_0302='100@700.0' PREV_0303='100@900.0' PREV_0400='100@70.00' PREV_0401='100@600.0' PREV_0402='100@900.0' PREV_0500='100@800.0' PREV_0501='100@900.0' PREV_0502='100@900.0' SELL='False' ISINITIAL_ODDS='False' /></MATCH></SB_INFO>";

    static public String onMatching103909firstHalf ="<?xml version='1.0' ?> <SB_INFO IS_REFRESH_REQUIRED='True' NEW_MSN=''><MATCH ID='103909' MATCH_STARTED='True' NTS_ETS_DEFINED='True' IS_BETTING_DELAY_NEED='True' IS_BETTING_DELAY_NEED_TQL='True' MATCH_STAGE='firsthalf' SCORE='0 : 0' NINETY_MINS_SCORE='' NINETY_MINS_TOTAL_CORNER='0' NTS_DIV='' STATUS='0' VOID='False' HASRESULT='False' INPLAY_POOLS='HAD,HIL,NTS,CHL,CRS' HT_POOLS=''><POOL TYPE='HAD' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' A='110@2.24' D='110@3.44' H='110@2.58' PREV_A='100@2.25' PREV_D='100@3.45' PREV_H='100@2.57' SELL='True' ISINITIAL_ODDS='False' /><POOL TYPE='HIL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='110@1.68' L='110@2.05' PREV_H='110@1.68' PREV_L='110@2.05' SELL='True' ISINITIAL_ODDS='False' LINE='2.5/3' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='NTS' IN='1' MATCH_POOL_STATUS='' INPLAY ='True' HT_SELL='False' A='110@1.80' H='110@1.95' N='110@16.00' PREV_A='100@1.81' PREV_H='110@1.95' PREV_N='110@16.00' SELL='False' ISINITIAL_ODDS='True' /><POOL TYPE='CHL' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' H='110@1.75' L='110@1.95' PREV_H='100@1.72' PREV_L='100@2.00' SELL='True' ISINITIAL_ODDS='False' LINE='9.5' LINE_STATUS='1' LINE_NO='1' MAINLINE_FLAG='1' /><POOL TYPE='CRS' IN='1' MATCH_POOL_STATUS='start-sell' INPLAY ='True' HT_SELL='False' M1MA='100@40.00' M1MD='100@100.0' M1MH='100@50.00' S0000='100@16.00' S0001='110@9.75' S0002='100@13.00' S0003='100@22.00' S0004='100@50.00' S0005='110@125.0' S0100='100@10.50' S0101='110@6.70' S0102='110@7.60' S0103='100@16.00' S0104='100@35.00' S0105='100@80.00' S0200='100@15.00' S0201='100@8.50' S0202='100@10.50' S0203='100@18.00' S0204='100@50.00' S0205='110@125.0' S0300='100@26.00' S0301='100@18.00' S0302='100@20.00' S0303='110@30.00' S0400='100@60.00' S0401='110@45.00' S0402='100@50.00' S0500='100@150.0' S0501='100@100.0' S0502='110@125.0' PREV_M1MA='100@40.00' PREV_M1MD='100@125.0' PREV_M1MH='100@50.00' PREV_0000='100@14.00' PREV_0001='100@10.00' PREV_0002='100@13.00' PREV_0003='100@22.00' PREV_0004='100@50.00' PREV_0005='100@120.0' PREV_0100='100@10.50' PREV_0101='100@6.75' PREV_0102='100@7.50' PREV_0103='100@16.00' PREV_0104='100@35.00' PREV_0105='100@80.00' PREV_0200='100@15.00' PREV_0201='100@8.50' PREV_0202='100@10.50' PREV_0203='100@18.00' PREV_0204='100@50.00' PREV_0205='100@120.0' PREV_0300='100@26.00' PREV_0301='100@18.00' PREV_0302='100@20.00' PREV_0303='100@32.00' PREV_0400='100@60.00' PREV_0401='100@40.00' PREV_0402='100@50.00' PREV_0500='100@150.0' PREV_0501='100@100.0' PREV_0502='100@120.0' SELL='True' ISINITIAL_ODDS='False' /></MATCH></SB_INFO>";
}
