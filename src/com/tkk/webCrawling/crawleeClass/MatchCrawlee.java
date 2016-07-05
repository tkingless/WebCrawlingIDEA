package com.tkk.webCrawling.crawleeClass;

import com.tkk.webCrawling.utils.DateTimeEntity;

/**
 * Created by tkingless on 6/26/16.
 */
public class MatchCrawlee extends baseCrawlee {

    String linkAddr;
    String baseUrl = "http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=";

    DateTimeEntity recordTime;

    public void SetupCrawlee(){
        //TODO
    }

    /*Considerations
    (1) there is possibility the match ended, get null exception:
    http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=103913
    (2) there is possibility no corner high low at all
    (3)
     */
}
