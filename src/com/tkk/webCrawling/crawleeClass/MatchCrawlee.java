package com.tkk.webCrawling.crawleeClass;

import com.tkk.webCrawling.utils.DateTimeEntity;
import com.tkk.webCrawling.webCrawler.baseCrawler;

/**
 * Created by tkingless on 6/26/16.
 */

//TODO not to use concurrencyMachine since there is no mass request to be sent
public class MatchCrawlee extends baseCrawlee implements Runnable {

    private String linkAddr;
    private String baseUrl = "http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=";
    private String matchID;

    DateTimeEntity recordTime;

    public MatchCrawlee(baseCrawler crlr,String aMatchID){
        super(crlr);
        //TODO
        matchID = aMatchID;
        linkAddr = baseUrl + aMatchID;
    }

    public void run() {

    }

    /*Considerations
    (1) there is possibility the match ended, get null exception:
    http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=103913
    (2) there is possibility no corner high low at all
    (3)
     */
}
