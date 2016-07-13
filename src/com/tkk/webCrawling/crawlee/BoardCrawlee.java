package com.tkk.webCrawling.crawlee;

import com.tkk.webCrawling.webCrawler.MatchEventWorker;
import com.tkk.webCrawling.webCrawler.MatchTestCONSTANTS;
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

    public BoardCrawlee(baseCrawler crawlerBelonged, String testHtml){
        super(crawlerBelonged);
        Jdoc = JsoupHelper.GetDocumentFromStr(testHtml);
    }


    //callable callbacks
    public Document call() {

        ////System.out.println("BoardCrawlee call() for callable called");
        try {
            if(Jdoc == null) {
                Jdoc = JsoupHelper.GetDocumentFrom(boardUrl);
                //System.out.println("Jdoc is: ");
                //System.out.println(Jdoc.toString());
            }
            GetChildNodes();
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Jdoc;
    }

    public List<MatchEventWorker> getParsedWorkers() {
        return parsedWorkers;
    }

    List<MatchEventWorker> parsedWorkers;
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

        List<Elements> cardinalityChecks = new ArrayList<Elements>();
        cardinalityChecks.add(onboardChildUrls);
        cardinalityChecks.add(matchNos);
        cardinalityChecks.add(matchTeams);
        cardinalityChecks.add(statuses);

        if (CardinalityChecking(cardinalityChecks)) {
             parsedWorkers = ParsingDocIntoMatchWorker(onboardChildUrls,matchNos,matchTeams,statuses);
        }
    }

    static List<MatchEventWorker> GetTestWorkers(MatchTestCONSTANTS.TestType test_type, Element jDoc) throws ParseException {
        List<MatchEventWorker> workers = new ArrayList<MatchEventWorker>();
        HashMap<String, String> searchNodes = new HashMap<String, String>();
        searchNodes.put("onboardChildUrls", "td[class$=cdAllIn] > a[href]");
        //searchNodes.put("MatchNo", "td[class$=\"cday ttgR2\"] > span > a[title$=\"All Odds\"]");
        searchNodes.put("MatchNo", "td[class$=\"cday ttgR2\"]");
        searchNodes.put("MatchTeams", "td[class$=\"cteams ttgR2\"]");
        searchNodes.put("Status", "td[class$=\"cesst\"] > span");

        Elements onboardChildUrls = jDoc.select(searchNodes.get("onboardChildUrls"));
        Elements matchNos = jDoc.select(searchNodes.get("MatchNo"));
        Elements matchTeams = jDoc.select(searchNodes.get("MatchTeams"));
        Elements statuses = jDoc.select(searchNodes.get("Status"));

        List<Elements> checks = new ArrayList<Elements>();

        checks.add(onboardChildUrls);
        checks.add(matchNos);
        checks.add(matchTeams);
        checks.add(statuses);

        if (CardinalityChecking(checks)) {
            if(test_type == MatchTestCONSTANTS.TestType.TYPE_PRE_REG){
                workers = ParsingDocIntoMatchWorker(onboardChildUrls, matchNos, matchTeams, statuses,test_type);
            }else {
                workers = ParsingDocIntoMatchWorker(onboardChildUrls, matchNos, matchTeams, statuses);
            }
        }

        return workers;
    }

    private static boolean CardinalityChecking(List<Elements> cardinalchecks) {
        boolean result = true;

        int cardinality = 0;

        if (!cardinalchecks.isEmpty()) {
            cardinality = cardinalchecks.get(0).size();
        } else {
            result = false;
            System.out.println("[Error]BoardCrawlee.CardinalityChecking is null");
        }

        for (Elements eles : cardinalchecks) {
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

    private static List<MatchEventWorker> ParsingDocIntoMatchWorker(Elements onboardChildUrls,
                                                                    Elements matchNos, Elements matchTeams,
                                                                    Elements statuses) throws ParseException {
        return ParsingDocIntoMatchWorker(onboardChildUrls,matchNos,matchTeams,statuses,null);
    }

    private static List<MatchEventWorker> ParsingDocIntoMatchWorker(Elements onboardChildUrls,
                                                                    Elements matchNos, Elements matchTeams,
                                                                    Elements statuses, MatchTestCONSTANTS.TestType test_type)
            throws ParseException {

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

            MatchEventWorker crleWorker = null;

            if(test_type == null) {
                crleWorker = new MatchEventWorker(matchId, matchNoIte.next(),
                        matchStatIte.next(), matchTeamIte.next());
            } else {
                crleWorker = new MatchEventWorker(matchId, matchNoIte.next(),
                        matchStatIte.next(), matchTeamIte.next(),test_type);
            }
            workerList.add(crleWorker);
        }

        return workerList;
    }

    private static List<String> livingWorkerMatchIDs = new ArrayList<String>();
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


    //TODO at somewhere appropriate, call this function
    public synchronized static void DetachWorker(MatchEventWorker worker) {
        livingWorkerMatchIDs.remove(worker.getMatchId());
    }

    public synchronized static List<MatchEventWorker> GenerateTestWorker (MatchTestCONSTANTS.TestType type, String testBoardHtml){
        List<MatchEventWorker> outputs = null;

        Document doc = JsoupHelper.GetDocumentFromStr(testBoardHtml);
        try {
            outputs = GetTestWorkers(type,doc);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputs;
    }
}
