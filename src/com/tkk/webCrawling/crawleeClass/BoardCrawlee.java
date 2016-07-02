package com.tkk.webCrawling.crawleeClass;

import com.tkk.webCrawling.webCrawler.MatchEventWorker;
import com.tkk.webCrawling.webCrawler.baseCrawler;
import com.tkk.webCrawling.utils.JsoupHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tkingless on 24/6/2016.
 */
public class BoardCrawlee extends baseCrawlee {

    List<String> onboard_indices = new ArrayList<String>();
    List<String> idx_urls = new ArrayList<String>();

    static String boardUrl = "http://bet.hkjc.com/football/odds/odds_inplay.aspx?ci=en-US";
    //TODO
    static List<MatchEventWorker> livingWorkers;

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

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Jdoc;
    }

    void GetChildNodes() throws ParseException {

        HashMap<String, String> searchNodes = new HashMap<String, String>();
        searchNodes.put("onboardChildUrls", "td[class$=cdAllIn] > a[href]");
        //searchNodes.put("MatchNo", "td[class$=\"cday ttgR2\"] > span > a[title$=\"All Odds\"]");
        searchNodes.put("MatchNo", "td[class$=\"cday ttgR2\"]");
        searchNodes.put("MatchTeams", "td[class$=\"cteams ttgR2\"]");
        searchNodes.put("Status", "td[class$=\"cesst\"] > span");

        Elements onboardChildUrls = Jdoc.select(searchNodes.get("onboardChildUrls"));
        Elements matchNos = Jdoc.select(searchNodes.get("MatchNo"));
        Elements matchTeams = Jdoc.select(searchNodes.get("MatchTeams"));
        Elements statuses = Jdoc.select(searchNodes.get("Status"));

        cardinalityChecks.add(onboardChildUrls);
        cardinalityChecks.add(matchNos);
        cardinalityChecks.add(matchTeams);
        cardinalityChecks.add(statuses);

        if (CardinalityChecking()) {
            System.out.println("[Iterator loop start:]");
            Iterator<Element> matchNoIte = matchNos.iterator();
            Iterator<Element> matchTeamIte = matchTeams.iterator();
            Iterator<Element> matchStatIte = statuses.iterator();

            //Get match indexes
            Pattern linkaddr = Pattern.compile("tmatchid=[0-9]{1,}");

            for (Element aRefUrl : onboardChildUrls) {

                Matcher idMatcher = linkaddr.matcher(aRefUrl.toString());
                String matchId = "";

                if (idMatcher.find()) {
                    matchId = idMatcher.group();
                    matchId = matchId.substring(matchId.lastIndexOf('=') + 1);
                    System.out.println("GetChildNodes(), match indexes: " + matchId);
                }

                MatchEventWorker crleWorker = new MatchEventWorker(matchId, matchNoIte.next(),
                        matchStatIte.next(), matchTeamIte.next());
                //TODO: if meet the condition, add it to livingworker

            }
        }
        System.out.println("The size of matcheWorkers: " + matcheWorkers.size());
    }

    List<Elements> cardinalityChecks = new ArrayList<Elements>();

    boolean CardinalityChecking() {
        boolean result = true;

        int cardinality = 0;

        if (!cardinalityChecks.isEmpty()) {
            cardinality = cardinalityChecks.get(0).size();
        } else {
            result = false;
            System.out.println("[Error]BoardCrawlee.CardinalityChecking is null");
        }

        for (Elements eles : cardinalityChecks) {
            if (eles.size() != cardinality) {
                result = false;
                System.out.println("[Error]inconsistent cardinality check number found, hint: ");
                System.out.println(eles.text());
                break;
            }
        }

        //debug use
       /* for (Elements eles: cardinalityChecks){
            System.out.println(eles.size());
        }*/

        return result;
    }

    void ParsingDocIntoMatchWorker() {
        //TODO
    }
}
