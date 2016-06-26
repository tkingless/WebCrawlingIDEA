package com.tkk.webCrawling.crawleeClass;

import com.tkk.webCrawling.utils.DateTimeEntity;

/**
 * Created by tkingless on 6/26/16.
 */
public class MatchCrawlee extends baseCrawlee {

    static String allOddsBaseUrl = "http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=";

    public String allOddsLinkAddr;
    public String matchId;

    DateTimeEntity recordTime;

    public MatchCrawlee (String aMatchId) {
        matchId = aMatchId;
        allOddsLinkAddr = allOddsBaseUrl + aMatchId;
        //System.out.println("MatchStruct constructed, matchId:" + matchId);
        System.out.println("and allOddsLink: " + allOddsLinkAddr);
    }
}
