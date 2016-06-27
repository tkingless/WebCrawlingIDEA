package com.tkk.webCrawling.webCrawler;

import com.tkk.webCrawling.utils.DateTimeEntity;

/**
 * Created by tkingless on 6/26/16.
 */

//this is class is to be long-live thread worker to log happening event
public class MatchEventWorker extends baseCrawler {

    static final String threadName = "MatchEventWorker-thread";

    DateTimeEntity commenceTime;

    protected MatchEventWorker() {
        super(CrawlerKeyBinding.MatchEvent, threadName);
    }

    public boolean IsLive(){
        //TODO
        return false;
    }

    public void LaunchProcess(){
        //TODO, if fullfill criteria, start a monitoring thread
    }
}
