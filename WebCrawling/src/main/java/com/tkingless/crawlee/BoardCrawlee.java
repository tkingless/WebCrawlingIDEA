package com.tkingless.crawlee;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import com.tkingless.MatchCarrier;
import com.tkingless.MatchInitContainer;
import com.tkingless.utils.logTest;
import com.tkingless.webCrawler.MatchEventWorker;
import com.tkingless.MatchTestCONSTANTS.TestType;
import com.tkingless.webCrawler.baseCrawler;
import com.tkingless.utils.JsoupHelper;
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

    public BoardCrawlee(baseCrawler crawlerBelonged, String testHtml) {
        super(crawlerBelonged);
        Jdoc = JsoupHelper.GetDocumentFromStr(testHtml);
    }


    //callable callbacks
    public Document call() {

        ////logTest.logger.info("BoardCrawlee call() for callable called");
        try {
            Process();
        } catch (IOException e) {
            logTest.logger.error("Board Crawlee error: ",e);

        } catch (ParseException e) {
            logTest.logger.error("Board Crawlee error: ",e);
        }
        return Jdoc;
    }

    public void Process() throws IOException, ParseException {
        if (Jdoc == null) {
            Jdoc = JsoupHelper.GetDocumentFrom(boardUrl);
            //logTest.logger.info("Jdoc is: ");
            //logTest.logger.info(Jdoc.toString());
        }
        GetChildNodes(null,Jdoc);
    }

    static List<MatchEventWorker> GetChildNodes(TestType test_type, Element jDoc) throws ParseException {

        MatchInitContainer initContainer = new MatchInitContainer();
        List<MatchEventWorker> workers = new ArrayList<MatchEventWorker>();

        initContainer.setOnboardChildUrls(jDoc.select("td[class$=cdAllIn] > a[href]"));
        initContainer.setMatchNos(jDoc.select("td[class$=\"cday ttgR2\"]"));
        initContainer.setMatchTeams(jDoc.select("td[class$=\"cteams ttgR2\"]>span>span[class$=teamname]"));
        initContainer.setLeagues(jDoc.select("td[class$=\"cflag ttgR2\"]>span>img"));
        initContainer.setStatuses(jDoc.select("td[class$=\"cesst\"] > span"));

        if (initContainer.CardinalityChecking()) {
                initContainer.FormularizeMatchCarrier();
                workers = ParsingDocIntoMatchWorker(initContainer, test_type);
        }

        return workers;
    }



    private static List<MatchEventWorker> ParsingDocIntoMatchWorker(MatchInitContainer initContainer, TestType test_type) throws ParseException {

        List<MatchEventWorker> workerList = new ArrayList<MatchEventWorker>();

        Iterator<MatchCarrier> carrierIterator = initContainer.getCarriers().iterator();

        while(carrierIterator.hasNext()){
            workerList.add(new MatchEventWorker(carrierIterator.next(),test_type));
        }

        return workerList;
    }

    private static List<String> livingWorkerMatchIDs = new ArrayList<String>();
    private static List<MatchEventWorker> livingWorkers = new ArrayList<MatchEventWorker>();

    public synchronized static boolean IsRegisteredByID(MatchEventWorker worker) {
        boolean isRegistered = false;
        String ID = worker.getMatchId();

        if (livingWorkerMatchIDs.contains(ID))
            isRegistered = true;

        return isRegistered;
    }

    public synchronized static void RegisterWorker(MatchEventWorker worker) {
        if (!livingWorkerMatchIDs.contains(worker.getMatchId())) {
            livingWorkerMatchIDs.add(worker.getMatchId());
            livingWorkers.add(worker);
            logTest.logger.info("[BoardCrawlee] worker, id: " +worker.getMatchId() + "has been registered");
        } else {
            logTest.logger.error("replicated local registration");
        }
    }

    public synchronized static void DetachWorker(MatchEventWorker worker) {
        if (livingWorkerMatchIDs.contains(worker.getMatchId())) {
            livingWorkerMatchIDs.remove(worker.getMatchId());
            livingWorkers.remove(worker);
            logTest.logger.info("[BoardCrawlee] livingworker, id: " +worker.getMatchId() + "has been released");
        } else
            logTest.logger.info("No such local registration before");
    }

    public synchronized static void TerminateAllLivingWorkers(){
        for (MatchEventWorker worker: livingWorkers){
            worker.Kill();
        }
    }

    public synchronized static List<MatchEventWorker> GenerateTestWorker(TestType type, String testBoardHtml) {
        List<MatchEventWorker> outputs = null;

        Document doc = JsoupHelper.GetDocumentFromStr(testBoardHtml);
        try {
            outputs = GetChildNodes(type, doc);
        } catch (ParseException e) {
            logTest.logger.error("Board Crawlee error: ",e);
        }

        return outputs;
    }


}


