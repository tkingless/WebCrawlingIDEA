package com.tkk.webCrawler;

import com.tkk.crawlee.BoardCrawlee;
import com.tkk.utils.logTest;

import java.io.IOException;
import java.text.ParseException;

public class HKJCcrawler extends baseCrawler {

    static final String threadName = "HKJC-thread";

    public HKJCcrawler() {
        super(CrawlerKeyBinding.HKJC, threadName);
    }

    private static HKJCcrawler instance = null;

    public static HKJCcrawler GetInstance() {

        if (instance == null) {
            instance = new HKJCcrawler();
        }

        return instance;
    }

    public void run() {
        try {
            logTest.logger.info("HKJCcrwaler run()");
            GetIndexesFromBoard();
        } catch (Exception e) {
            logTest.logger.error(e);
        }
    }

    protected void GetIndexesFromBoard() throws InterruptedException, IOException, ParseException {

        BoardCrawlee boardCrawlee = new BoardCrawlee(this);

        /*ConcurrencyMachine.GetInstance().RegisterQueue(boardCrawlee);

        synchronized (this) {
            ConcurrencyMachine.GetInstance().InvokeQueue();
        }*/

        boardCrawlee.Process();

        logTest.logger.info("HKJCcrawler thread revives at GetIndexesFromBoard()");
    }

    @Override
    public String toString() {
        return "HKJCCrawler";
    }
}
