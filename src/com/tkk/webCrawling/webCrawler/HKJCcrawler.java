package com.tkk.webCrawling.webCrawler;


import com.tkk.webCrawling.crawleeClass.TutorCaseCrawlee;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HKJCcrawler extends baseCrawler {

    static final String boardUrl = "http://bet.hkjc.com/football/odds/odds_inplay.aspx?ci=en-US";
    static final String allOddsBaseUrl = "http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=";

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
            ProcessUrlsAction();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    protected void ProcessUrlsAction() {

        List<String> onboard_indices = new ArrayList<String>();
        List<String> idx_urls = new ArrayList<String>();

        try {
            Document idxDoc = Jsoup.connect(boardUrl).data("query", "Java").userAgent("Mozilla")
                    .cookie("auth", "token").timeout(6000).post();

            HashMap<String, String> searchNodes = new HashMap<String, String>();
            searchNodes.put("onboardIdxs", "td[class$=cdAllIn] > a[href]");

            Pattern atrbt = Pattern.compile("");
            Matcher rawIdxUrls = atrbt.matcher(idxDoc.body().toString());

            while (rawIdxUrls.find()) {
                String str = rawIdxUrls.group();
                str = str.substring(str.lastIndexOf('_') + 1);
                onboard_indices.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void AnalyzeContentAction(TutorCaseCrawlee tutorCaseCrawlee){

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
