package com.tkk.webCrawler;


import com.tkk.ConcurrencyMachine;
import com.tkk.crawlee.BoardCrawlee;
import com.tkk.logTest;

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
            logTest.logger.info("HKJCcrwaler run() called, but this is System.out print");
            GetIndexesFromBoard();
        } catch (Exception e) {
            logTest.logger.error(e);
        }
    }

    protected void GetIndexesFromBoard() throws InterruptedException {

        BoardCrawlee boardCrawlee = new BoardCrawlee(this);
        ConcurrencyMachine.GetInstance().RegisterQueue(boardCrawlee);

        synchronized (this) {
            ConcurrencyMachine.GetInstance().InvokeQueue();
        }

        logTest.logger.info("HKJCcrawler thread revives at GetIndexesFromBoard()");
    }

    @Override
    public String toString() {
        return "HKJCCrawler";
    }
}
