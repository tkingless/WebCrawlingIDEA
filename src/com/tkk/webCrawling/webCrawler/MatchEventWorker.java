package com.tkk.webCrawling.webCrawler;

import com.tkk.webCrawling.utils.DateTimeEntity;
import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tkingless on 6/26/16.
 */

//this is class is to be long-live thread worker to log happening event
public class MatchEventWorker extends baseCrawler {

    enum MatchState {
        STATE_INITIALIZATION,
        //only at state of pre_registered, the thread is launched
        STATE_PRE_REGISTERED,
        STATE_MATCH_START,
        STATE_MATCH_LOGGING,
        STATE_MATCH_ENDED
    }

    static final String threadName = "MatchEventWorker-thread";

    String baseUrl = "http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=";
    //The unique id for this worker
    String matchId;
    long scanPeriod = 5000;

    DateTimeEntity commenceTime;
    MatchState status;
    //the secondary key to be used
    String matchKey;
    String matchTeams;


    public MatchEventWorker(String aMatchId, Element matchKeyEle, Element statusEle, Element teamsEle) {
        super(CrawlerKeyBinding.MatchEvent, threadName+"-"+aMatchId);
        status = MatchState.STATE_INITIALIZATION;
        matchId = aMatchId;
        System.out.println("MatchEventWorker constructed, matchId:" + matchId);
        ///System.out.println("and allOddsLink: " + linkAddr);

        ExtractMatcdKey(matchKeyEle);
        ExtractTeams(teamsEle);

        try {
            ExtractStatus(statusEle);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void ExtractMatcdKey (Element matchKeyEle) {
        matchKey = matchKeyEle.text();
        System.out.println("GetChildNodes(), matchKey: " + matchKey);
    }

    void ExtractStatus(Element statusEle) throws ParseException {
        //TODO: in some case, suppose the program failed to get the start time of the match event, mark current start time to
        //to the start time
        if (statusEle.text().contains("Expected In Play start selling time")) {
            String startTimeWeb = statusEle.childNode(3).toString();

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

            commenceTime = new DateTimeEntity(dateTimeBuilder.toString(), new SimpleDateFormat("HH:mm:ss dd/MM/yyyy"));
            System.out.println("ExtractStatus(),commenceTime: " + commenceTime.toString());
        }
    }

    void ExtractTeams(Element teamsEle) {
        matchTeams = teamsEle.text();
        System.out.println("GetChildNodes(), matchTeams: " + matchTeams);
    }

    public boolean IsLive(){
        //TODO
        return false;
    }

    public void LaunchProcess(){
        //TODO, if fullfill criteria, start a monitoring thread
    }

    public void SetupParam(){

    }

    void EmitRequest () {
        //TODO grab data periodically
    }

    //The main loop function
    void Proc(){
    //TODO
    }

    public void Kill(){
        //TODO: make sure detached from any pointing, and thread on this object is stopped()
    }
}
