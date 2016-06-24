package com.tkk.webCrawling.webCrawler;

/**
 * Created by tkingkwun on 24/6/2016.
 */
public abstract class baseCrawler extends Thread {

    public enum CrawlingStates {
        STATE_PARSE_IN_CONFIG,
        STATE_PROCESS_URL,
        STATE_ANALYSE_CONTENT,
        STATE_SEARCH_CRIT_FILTER,
        STATE_POSTPROCESS
    }

    public enum CrawlerKeyBinding {
        TutorGroup, ECTutor, L4Tutor, HKJC
    }

    protected CrawlerKeyBinding mID;

    public CrawlerKeyBinding get_mID() {
        return mID;
    }

    protected Thread thread;

    protected baseCrawler(CrawlerKeyBinding id, String threadName) {
        mID = id;
        if(thread == null){
            thread = new Thread(this, threadName);
        }
    }

    public void StartRun() {
        if(thread != null){
            thread.start(); //jump to the run function to see what to do
        }else{
            System.err.println("thread not initialized");
        }
    }
}
