package com.tkk.webCrawling.crawleeClass;

import com.tkk.webCrawling.webCrawler.MatchEventWorker;
import com.tkk.webCrawling.webCrawler.baseCrawler;
import com.tkk.webCrawling.utils.JsoupHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tkingless on 24/6/2016.
 */
public class BoardCrawlee extends baseCrawlee {

    List<String> onboard_indices = new ArrayList<String>();
    List<String> idx_urls = new ArrayList<String>();

    static String boardUrl = "http://bet.hkjc.com/football/odds/odds_inplay.aspx?ci=en-US";

    public BoardCrawlee(baseCrawler crawlerBelonged) {
        super(crawlerBelonged);
    }

    List<MatchEventWorker> matcheWorkers = new ArrayList<MatchEventWorker>();

    //callable callbacks
    public Document call() {

        System.out.println("BoardCrawlee call() for callable called");
        try {
            Jdoc = JsoupHelper.GetDocumentFrom(boardUrl);
            GetChildNodes();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return Jdoc;
    }

    void GetChildNodes() {

        HashMap<String, String> searchNodes = new HashMap<String, String>();
        searchNodes.put("onboardChildUrls", "td[class$=cdAllIn] > a[href]");

        Elements onboardChildUrls = Jdoc.select(searchNodes.get("onboardChildUrls"));
        Pattern linkaddr = Pattern.compile("tmatchid=[0-9]{1,}");

        for (Element aRefUrl : onboardChildUrls) {

            Matcher matchid = linkaddr.matcher(aRefUrl.toString());

            while (matchid.find()) {
                String str = matchid.group();
                str = str.substring(str.lastIndexOf('=') + 1);
                matcheWorkers.add(new MatchEventWorker(str));
            }
        }

        System.out.println("The size of matcheWorkers: " + matcheWorkers.size());


    }
}
