package com.tkk.webCrawling.crawleeClass;

import com.tkk.webCrawling.utils.DateTimeEntity;
import com.tkk.webCrawling.webCrawler.MatchEventWorker;
import com.tkk.webCrawling.webCrawler.baseCrawler;
import com.tkk.webCrawling.utils.JsoupHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        searchNodes.put("MatchNo", "td[class$=\"cday ttgR2\"] > span > a[title$=\"All Odds\"]");
        searchNodes.put("MatchTeams", "td[class$=\"cteams ttgR2\"]");
        searchNodes.put("Status", "td[class$=\"cesst\"] > span");

        Elements onboardChildUrls = Jdoc.select(searchNodes.get("onboardChildUrls"));
        Elements matchNos = Jdoc.select(searchNodes.get("MatchNo"));
        Elements matchTeams = Jdoc.select(searchNodes.get("MatchTeams"));
        Elements statuses = Jdoc.select(searchNodes.get("Status"));

        //Get match indexes
        Pattern linkaddr = Pattern.compile("tmatchid=[0-9]{1,}");

        for (Element aRefUrl : onboardChildUrls) {

            Matcher matchid = linkaddr.matcher(aRefUrl.toString());

            while (matchid.find()) {
                String str = matchid.group();
                str = str.substring(str.lastIndexOf('=') + 1);
                System.out.println("GetChildNodes(), match indexes: " + str);
            }
        }

        //Get matchId, matchTeams
        for (Element matchNo : matchNos) {
            System.out.println("GetChildNodes(), match id: " + matchNo.text());
        }
        for (Element matchTeam : matchTeams) {
            System.out.println("GetChildNodes(), match team: " + matchTeam.text());
        }
        for (Element status : statuses) {
            ///System.out.println("GetChildNodes(), match status: " + status.text());

            if (status.text().contains("Expected In Play start selling time")) {
                String startTimeWeb = status.childNode(3).toString();
                System.out.println("GetChildNodes(), match status & raw date: " + startTimeWeb);

                Pattern dayPattern = Pattern.compile("[0-9]{2}/[0-9]{2}");
                Pattern timePattern = Pattern.compile("[0-9]{2}:[0-9]{2}");

                Matcher dayMatch = dayPattern.matcher(startTimeWeb);
                Matcher timeMatch = timePattern.matcher(startTimeWeb);

                dayMatch.find();
                timeMatch.find();

                StringBuilder dateTimeBuilder = new StringBuilder(timeMatch.group()).append(":00 ");
                dateTimeBuilder.append(dayMatch.group());
                dateTimeBuilder.append("/");
                dateTimeBuilder.append(DateTimeEntity.GetCurrentYear());

                DateTimeEntity matchStartTime = new DateTimeEntity(dateTimeBuilder.toString(), new SimpleDateFormat("HH:mm:ss dd/MM/yyyy"));

            }
        }

        cardinalityChecks.add(onboardChildUrls);
        cardinalityChecks.add(matchNos);
        cardinalityChecks.add(matchTeams);
        cardinalityChecks.add(statuses);

        if(CardinalityChecking())
        {
            System.out.println("[Iterator loop start:]");
            //TODO: iterate the elements
            Iterator<Element> matchidIte = matchNos.iterator();

            while (matchidIte.hasNext()){
                System.out.println(matchidIte.next().text());
            }
        }
        System.out.println("The size of matcheWorkers: " + matcheWorkers.size());

        ///DateTimeEntity Testing
        ///System.out.println("Today is : "+ DateTimeEntity.GetToday());
        ///System.out.println("and now time is : "+ DateTimeEntity.GetCurrentTime());
        ///DateTimeEntity dte = new DateTimeEntity();
        ///dte.SetTimezone(TimeZone.getTimeZone("Asia/Dubai"));

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

        return result;
    }

    void ParsingDocIntoMatchWorker() {
        //TODO
    }
}
