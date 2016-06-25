package com.tkk.webCrawling.webCrawler;


import com.tkk.webCrawling.ConcurrencyMachine;
import com.tkk.webCrawling.crawleeClass.BoardCrawlee;
import com.tkk.webCrawling.crawleeClass.TutorCaseCrawlee;
import com.tkk.webCrawling.utils.JsoupHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        //TODO:
        // boardCrawlee's action
        synchronized (this) {
            ConcurrencyMachine.GetInstance().InvokeQueue();
        }

        System.out.println("HKJCcrawler thread revives at GetIndexesFromBoard()");
    }

    public void AnalyzeContentAction(TutorCaseCrawlee tutorCaseCrawlee) {

        Document doc = tutorCaseCrawlee.getJdoc();
        HashMap<String, String> searchNodes = new HashMap<String, String>();
    }

    public void FilterByCritAction() {

    }

    public void PostProcessAction() {

    }

    @Override
    public String toString() {
        return "HKJCCrawler";
    }
}
