package com.tkk.webCrawling.webCrawler;

import com.tkk.webCrawling.utils.DateTimeEntity;

import java.util.Timer;

/**
 * Created by tkingless on 6/26/16.
 */

//this is class is to be long-live thread worker to log happening event
public class MatchEventWorker extends baseCrawler {

    static final String threadName = "MatchEventWorker-thread";

    String baseUrl = "http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=";
    //The unique id for this worker
    String matchId;

    DateTimeEntity commenceTime;
    Timer scanPeriod;

    public MatchEventWorker(String aMatchId) {
        super(CrawlerKeyBinding.MatchEvent, threadName+"-"+aMatchId);
        matchId = aMatchId;
        System.out.println("MatchEventWorker constructed, matchId:" + matchId);
        //System.out.println("and allOddsLink: " + linkAddr);
    }

    public boolean IsLive(){
        //TODO
        return false;
    }

    public void LaunchProcess(){
        //TODO, if fullfill criteria, start a monitoring thread
    }

    public void SetupParam(){

    }

    void EmitRequest () {
        //TODO grab data periodically
    }
}
