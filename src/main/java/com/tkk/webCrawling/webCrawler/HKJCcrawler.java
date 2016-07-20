package com.tkk.webCrawling.webCrawler;


import com.tkk.webCrawling.ConcurrencyMachine;
import com.tkk.webCrawling.crawlee.BoardCrawlee;

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
            System.out.println("HKJCcrwaler run() called");
            GetIndexesFromBoard();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    protected void GetIndexesFromBoard() throws InterruptedException {

        BoardCrawlee boardCrawlee = new BoardCrawlee(this);
        ConcurrencyMachine.GetInstance().RegisterQueue(boardCrawlee);

        synchronized (this) {
            ConcurrencyMachine.GetInstance().InvokeQueue();
        }

        System.out.println("HKJCcrawler thread revives at GetIndexesFromBoard()");
    }

    @Override
    public String toString() {
        return "HKJCCrawler";
    }
}
