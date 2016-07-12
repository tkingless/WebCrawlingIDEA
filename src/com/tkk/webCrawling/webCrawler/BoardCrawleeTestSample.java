package com.tkk.webCrawling.webCrawler;

/**
 * Created by tsangkk on 7/12/16.
 */
public class BoardCrawleeTestSample {

    static public String testBoardhtml = "\n" +
            "\n" +
            "<html>\n" +
            "   <body>\n" +
            "      <form name=\"aspnetForm\" method=\"post\" action=\"odds_inplay.aspx?ci=en-US\" id=\"aspnetForm\">\n" +
            "         <table class=\"tINPLAYHAD tOdds\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
            "            <tbody>\n" +
            "               <tr class=\"rAlt0 cou2 tgCou2\" id=\"rmid104030\">\n" +
            "                  <td class=\"cday ttgR2\"><span><a href=\"/football/odds/odds_allodds.aspx?lang=EN&amp;tmatchid=104030&amp;tdate=13-07-2016&amp;tday=WED&amp;tnum=9\" title=\"All Odds\">WED 9</a></span></td>\n" +
            "                  <td class=\"cflag ttgR2\"><span><img src=\"/nas/jcbwinfo/football/info/images/flag_JD1.gif?CV=L205R2g\" alt=\"Japanese Division 1\" title=\"Japanese Division 1\" class=\"cfJD1\" onerror=\"errImg(this);\"></span></td>\n" +
            "                  <td class=\"cteams ttgR2\"><span><input type=\"hidden\" id=\"104030_delay\" value=\"false\"><span class=\"teamname\">A Future Match</span><span class=\"nolnk span_vs\" id=\"sr104030\"><span class=\"matchresult\">vs</span></span><span class=\"teamname\">Vissel Kobe</span></span></td>\n" +
            "                  <td class=\"cvenue\"><span class=\"ng\"><img src=\"/football/info/images/icon_neutral.gif?CV=\" alt=\"This match will be played at Mitsuzawa Stadium, Yokohama .\" title=\"This match will be played at Mitsuzawa Stadium, Yokohama .\" onerror=\"errImg(this);\"></span></td>\n" +
            "                  <td class=\"ctv\"><span class=\"tv\"><span onclick=\"javascript:goTVUrl();\" style=\"cursor: pointer;\"><img src=\"/nas/jcbwinfo/football/info/images/icon_tv-C61.gif?CV=\" alt=\"C61-i-CABLE Super Soccer Channel \n" +
            "                     201-i-CABLE hd201\" title=\"C61-i-CABLE Super Soccer Channel \n" +
            "                     201-i-CABLE hd201\" onerror=\"errImg(this);\"></span></span></td>\n" +
            "                  <td class=\"cesst\"><span id=\"sst104030\"><input type=\"hidden\" id=\"hsst104030\" value=\"InPlayESST_nobr\">Expected In Play start selling time: <br>13/12 18:00</span></td>\n" +
            "                  <td class=\"codds\"><span class=\"s1\"><input type=\"checkbox\" disabled name=\"chkHAD\" id=\"104030_HAD_H_c\" value=\"\" onclick=\"tgTD(this);\" class=\"104030_HAD_H_0_c\"><a class=\"oddsLink noUL\"><span id=\"104030_HAD_H\">---</span></a></span></td>\n" +
            "                  <td class=\"codds\"><span class=\"dl\"><input type=\"checkbox\" disabled name=\"chkHAD\" id=\"104030_HAD_D_c\" value=\"\" onclick=\"tgTD(this);\" class=\"104030_HAD_D_0_c\"><a class=\"oddsLink noUL\"><span id=\"104030_HAD_D\">---</span></a></span></td>\n" +
            "                  <td class=\"codds\"><span class=\"dl\"><input type=\"checkbox\" disabled name=\"chkHAD\" id=\"104030_HAD_A_c\" value=\"\" onclick=\"tgTD(this);\" class=\"104030_HAD_A_0_c\"><a class=\"oddsLink noUL\"><span id=\"104030_HAD_A\">---</span></a></span></td>\n" +
            "                  <td class=\"cdAllIn\"><a href=\"/football/odds/odds_inplay_all.aspx?lang=EN&amp;tmatchid=104030&amp;tdate=13-07-2016&amp;tday=WED&amp;tnum=9\" title=\"All Odds\"><img src=\"/football/info/images/btn_odds.gif?CV=\" alt=\"All Odds\" title=\"All Odds\" onerror=\"errImg(this);\"></a></td>\n" +
            "               </tr>\n" +
            "            </tbody>\n" +
            "         </table>\n" +
            "      </form>\n" +
            "   </body>\n" +
            "</html>\n" +
            "\n";

    static public String future104030 = "<!--?xml version='1.0' ?-->\n" +
            "<html>\n" +
            " <head></head>\n" +
            " <body>\n" +
            "  <sb_info is_refresh_required=\"True\" new_msn=\"\">\n" +
            "   <match id=\"104030\" match_started=\"False\" nts_ets_defined=\"True\" is_betting_delay_need=\"False\" is_betting_delay_need_tql=\"False\" match_stage=\"InPlayESST_nobr\" score=\"-1 :  -1\" ninety_mins_score=\"\" ninety_mins_total_corner=\"-\" nts_div=\"\" status=\"0\" void=\"False\" hasresult=\"False\" inplay_pools=\"HAD,HIL,CHL,NTS,CRS\" ht_pools=\"\">\n" +
            "    <pool type=\"HAD\" in=\"1\" match_pool_status=\"start-sell\" inplay=\"True\" ht_sell=\"False\" a=\"100@3.30\" d=\"100@3.35\" h=\"100@1.92\" prev_a=\"100@3.30\" prev_d=\"100@3.35\" prev_h=\"100@1.92\" sell=\"True\" isinitial_odds=\"False\" />\n" +
            "    <pool type=\"HIL\" in=\"1\" match_pool_status=\"start-sell\" inplay=\"True\" ht_sell=\"False\" h=\"100@1.95_100@1.68_100@3.55\" l=\"100@1.75_100@2.05_100@1.25\" prev_h=\"100@1.95_100@1.68_100@3.55\" prev_l=\"100@1.75_100@2.05_100@1.25\" sell=\"True\" isinitial_odds=\"False\" line=\"2.5_2/2.5_3.5\" line_status=\"1_1_1\" line_no=\"1_3_2\" mainline_flag=\"1_0_0\" />\n" +
            "    <pool type=\"CHL\" in=\"1\" match_pool_status=\"start-sell\" inplay=\"True\" ht_sell=\"False\" h=\"100@2.02\" l=\"100@1.70\" prev_h=\"100@2.02\" prev_l=\"100@1.70\" sell=\"True\" isinitial_odds=\"False\" line=\"9.5\" line_status=\"1\" line_no=\"1\" mainline_flag=\"1\" />\n" +
            "    <pool type=\"CRS\" in=\"1\" match_pool_status=\"start-sell\" inplay=\"True\" ht_sell=\"False\" m1ma=\"100@120.0\" m1md=\"100@400.0\" m1mh=\"100@60.00\" s0000=\"100@9.50\" s0001=\"100@8.75\" s0002=\"100@16.00\" s0003=\"100@40.00\" s0004=\"100@100.0\" s0005=\"100@400.0\" s0100=\"100@6.60\" s0101=\"100@6.50\" s0102=\"100@9.75\" s0103=\"100@28.00\" s0104=\"100@80.00\" s0105=\"100@300.0\" s0200=\"100@8.50\" s0201=\"100@7.25\" s0202=\"100@14.00\" s0203=\"100@30.00\" s0204=\"100@100.0\" s0205=\"100@400.0\" s0300=\"100@15.00\" s0301=\"100@14.00\" s0302=\"100@24.00\" s0303=\"100@60.00\" s0400=\"100@40.00\" s0401=\"100@34.00\" s0402=\"100@60.00\" s0500=\"100@100.0\" s0501=\"100@100.0\" s0502=\"100@150.0\" prev_m1ma=\"100@120.0\" prev_m1md=\"100@400.0\" prev_m1mh=\"100@60.00\" prev_0000=\"100@9.50\" prev_0001=\"100@8.75\" prev_0002=\"100@16.00\" prev_0003=\"100@40.00\" prev_0004=\"100@100.0\" prev_0005=\"100@400.0\" prev_0100=\"100@6.60\" prev_0101=\"100@6.50\" prev_0102=\"100@9.75\" prev_0103=\"100@28.00\" prev_0104=\"100@80.00\" prev_0105=\"100@300.0\" prev_0200=\"100@8.50\" prev_0201=\"100@7.25\" prev_0202=\"100@14.00\" prev_0203=\"100@30.00\" prev_0204=\"100@100.0\" prev_0205=\"100@400.0\" prev_0300=\"100@15.00\" prev_0301=\"100@14.00\" prev_0302=\"100@24.00\" prev_0303=\"100@60.00\" prev_0400=\"100@40.00\" prev_0401=\"100@34.00\" prev_0402=\"100@60.00\" prev_0500=\"100@100.0\" prev_0501=\"100@100.0\" prev_0502=\"100@150.0\" sell=\"True\" isinitial_odds=\"False\" />\n" +
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
