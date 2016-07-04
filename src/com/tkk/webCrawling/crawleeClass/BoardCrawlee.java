package com.tkk.webCrawling.crawleeClass;

import com.tkk.webCrawling.webCrawler.MatchEventWorker;
import com.tkk.webCrawling.webCrawler.baseCrawler;
import com.tkk.webCrawling.utils.JsoupHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tkingless on 24/6/2016.
 */
public class BoardCrawlee extends baseCrawlee {

    static String boardUrl = "http://bet.hkjc.com/football/odds/odds_inplay.aspx?ci=en-US";

    public BoardCrawlee(baseCrawler crawlerBelonged) {
        super(crawlerBelonged);
    }

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
            List<MatchEventWorker> parsedWorkers = ParsingDocIntoMatchWorker(onboardChildUrls,matchNos,matchTeams,statuses);
        }
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

    List<MatchEventWorker> ParsingDocIntoMatchWorker( Elements onboardChildUrls, Elements matchNos, Elements matchTeams, Elements statuses ) {

        List<MatchEventWorker> workerList = new ArrayList<MatchEventWorker>();

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

            workerList.add(crleWorker);

        }

        return workerList;
    }

    static List<String> livingWorkerMatchIDs = new ArrayList<String>();
    public synchronized static boolean IsRegisteredByID(MatchEventWorker worker){
        boolean isRegistered = false;
        String ID = worker.getMatchId();

        if(livingWorkerMatchIDs.contains(ID))
            isRegistered = true;

        return isRegistered;
    }

    public synchronized static void RegisterWorker(MatchEventWorker worker){
        livingWorkerMatchIDs.add(worker.getMatchId());
    }

    public synchronized static void DetachWorker(MatchEventWorker worker) {
        livingWorkerMatchIDs.remove(worker.getMatchId());
    }
}
