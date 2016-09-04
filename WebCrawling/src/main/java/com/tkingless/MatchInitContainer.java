package com.tkingless;

import com.tkingless.utils.logTest;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tkingkwun on 4/9/2016.
 */
public class MatchInitContainer {

    Elements onboardChildUrls,matchNos,matchTeams,homeTeams,awayTeams,statuses,leagues;

    //TODO do proxy to list of MatchCarrier

    List<MatchCarrier> carriers;

    public List<MatchCarrier> getCarriers() {
        return carriers;
    }

    public void setOnboardChildUrls(Elements onboardChildUrls) {
        this.onboardChildUrls = onboardChildUrls;
        cardinalityChecks.add(this.onboardChildUrls);
    }

    public void setMatchNos(Elements matchNos) {
        this.matchNos = matchNos;
        cardinalityChecks.add(this.matchNos);
    }

    public void setMatchTeams(Elements matchTeams) {
        this.matchTeams = matchTeams;

        specialTreatmentonTeam();
    }

    void specialTreatmentonTeam(){
        for(int i = 0; i < matchTeams.size(); i++){
            if(i%2 == 0){
                homeTeams.add(matchTeams.get(i));
            } else {
                awayTeams.add(matchTeams.get(i));
            }
        }

        cardinalityChecks.add(homeTeams);
        cardinalityChecks.add(awayTeams);
    }

    public void setStatuses(Elements statuses) {
        this.statuses = statuses;
        cardinalityChecks.add(this.statuses);
    }

    public void setLeagues(Elements leagues) {
        this.leagues = leagues;
        cardinalityChecks.add(this.leagues);
    }

    List<Elements> cardinalityChecks = new ArrayList<Elements>();

    public boolean CardinalityChecking() {
        boolean result = true;

        int cardinality = 0;

        if (!cardinalityChecks.isEmpty()) {
            cardinality = cardinalityChecks.get(0).size();
        } else {
            result = false;
            logTest.logger.info("[Error]BoardCrawlee.CardinalityChecking is null");
        }

        for (Elements eles : cardinalityChecks) {
            if (eles.size() != cardinality) {
                result = false;
                logTest.logger.info("[Error]inconsistent cardinality check number found, hint: ");
                logTest.logger.info(eles.text());
                break;
            }
        }

        //debug use
       /* for (Elements eles: cardinalityChecks){
            logTest.logger.info(eles.size());
        }*/

        return result;
    }

    public void FormularizeMatchCarrier() {

        carriers = new ArrayList<>();

        try {

            //logTest.logger.info("[Iterator loop start:]");
            Iterator<Element> matchNoIte = matchNos.iterator();
            Iterator<Element> homeTeamIte = homeTeams.iterator();
            Iterator<Element> awayTeamIte = awayTeams.iterator();
            Iterator<Element> matchStatIte = statuses.iterator();
            Iterator<Element> leagueIte = leagues.iterator();

            for (Element aRefUrl : onboardChildUrls) {

                carriers.add(new MatchCarrier(
                        Integer.parseInt(ExtractMatchId(aRefUrl)),
                        ExtractMatchNo(matchNoIte.next()),
                        homeTeamIte.next().text(),
                        awayTeamIte.next().text(),
                        matchStatIte.next().text(),
                        ExtractLeague(leagueIte.next())
                ));
            }
        } catch (Exception e){
            logTest.logger.error("FormularizeMatchCarrier error: ", e);
        }
    }

    String ExtractMatchId (Element ele){


        Pattern linkaddr = Pattern.compile("tmatchid=[0-9]{1,}");
        Matcher idMatcher = linkaddr.matcher(ele.toString());
        String matchId = "";

        if (idMatcher.find()) {
            matchId = idMatcher.group();
            matchId = matchId.substring(matchId.lastIndexOf('=') + 1);
            //logTest.logger.info("GetChildNodes(), match indexes: " + matchId);
        }

        return matchId;
    }

    private String ExtractMatchNo (Element matchKeyEle) {
        return matchKeyEle.text();
        //logTest.logger.info("GetChildNodes(), matchKey: " + matchKey);
    }

    private String ExtractLeague (Element leagueEle) {
        return leagueEle.attr("title");
    }

}
