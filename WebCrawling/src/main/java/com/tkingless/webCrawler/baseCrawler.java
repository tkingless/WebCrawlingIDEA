package com.tkingless.webCrawler;

import com.tkingless.utils.logTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by tkingless on 24/6/2016.
 */
public abstract class baseCrawler extends Thread {

    final static Logger logger = LogManager.getLogger(baseCrawler.class);

    public enum CrawlingStates {
        STATE_PARSE_IN_CONFIG,
        STATE_PROCESS_URL,
        STATE_ANALYSE_CONTENT,
        STATE_SEARCH_CRIT_FILTER,
        STATE_POSTPROCESS
    }

    public enum CrawlerKeyBinding {
        TutorGroup, ECTutor, L4Tutor, HKJC, MatchEvent
    }

    protected CrawlerKeyBinding mID;

    public CrawlerKeyBinding get_mID() {
        return mID;
    }

    protected Thread thread;

    public void JoinThread() throws InterruptedException {
        thread.join();
    }

    protected baseCrawler(CrawlerKeyBinding id, String threadName) {
        mID = id;
        if(thread == null){
            thread = new Thread(this, threadName);
        }
    }

    //to be called externally, to ask inherited to run run(), with creating a new thread
    public void NewThreadRun() {
        if(thread != null){
            thread.start(); //jump to the run function to see what to do
        }else{
            logTest.logger.error("thread not initialized");
        }
    }
}
