package com.tkk.webCrawling.webCrawler;

import com.tkk.webCrawling.utils.DateTimeEntity;
import org.jsoup.nodes.Element;

import java.io.InterruptedIOException;
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
        STATE_FUTURE_MATCH,
        //only at state of pre_registered, the thread is launched
        STATE_PRE_REGISTERED,
        STATE_MATCH_START,
        STATE_MATCH_LOGGING,
        STATE_MATCH_ENDED
    }

    enum MatchStage {
        STAGE_ESST,
        STAGE_FIRST,
        STAGE_HALFTIME,
        STAGE_SECOND,
    }

    static final String threadName = "MatchEventWorker-thread";

    String baseUrl = "http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=";
    //The unique id for this worker
    String matchId;
    long scanPeriod = 5000;
    long preRegperiod = 1000 * 60 * 5;

    DateTimeEntity commenceTime;
    MatchState status;
    MatchStage stage;
    //the secondary key to be used
    String matchKey;
    String matchTeams;


    public MatchEventWorker(String aMatchId, Element matchKeyEle, Element statusEle, Element teamsEle) {
        super(CrawlerKeyBinding.MatchEvent, threadName + "-" + aMatchId);
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

        System.out.println("MatcherEventWorker finished constructer.");

        this.StartRun();
    }

    void ExtractMatcdKey(Element matchKeyEle) {
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
            stage = MatchStage.STAGE_ESST;
        }else if (statusEle.text().contains("1st Half In Progress")) {
            //TODO something
            stage = MatchStage.STAGE_FIRST;
        }else if (statusEle.text().contains("Half Time")) {
            //TODO something
            stage = MatchStage.STAGE_HALFTIME;
        }else if (statusEle.text().contains("2nd Half In Progress")){
            //TODO something
            stage = MatchStage.STAGE_SECOND;
        }
    }

    void ExtractTeams(Element teamsEle) {
        matchTeams = teamsEle.text();
        System.out.println("GetChildNodes(), matchTeams: " + matchTeams);
    }

    public boolean IsLive() {
        //TODO
        return false;
    }

    public void LaunchProcess() {
        //TODO, if fullfill criteria, start a monitoring thread
    }

    void EmitRequest() {
        //TODO grab data periodically
    }

    public void run() {
        try {
            System.out.println("MatchEventWorker run() called");
            Proc();
        } catch (Exception e) {
            System.err.println(e);
        }
    }


    //The main loop function
    void Proc() {

        while (status != MatchState.STATE_MATCH_ENDED || status != MatchState.STATE_FUTURE_MATCH) {

            if (status == MatchState.STATE_INITIALIZATION) {
                //TODO
                //compare the match startime and check the match status,
                //////check if Match_stage==InPlayESST_nobr
                ////////if too long away from statrtime, set state to STATE_FUTURE_MATCH
                ////////else if within the preRegperiod, enter STATE_PRE_REGISTERED
                //if already in progress and no record before, set the startime to now, and state to STATE_PRE_REGISTERED
                System.out.println("Threadname: " + threadName + matchId + " STATE_INITIALIZATION");


                long timediff = commenceTime.CalTimeIntervalDiff(new DateTimeEntity());
                System.out.println("timediff: " + timediff + " id: " + matchId);
                status = MatchState.STATE_PRE_REGISTERED;
            } else if (status == MatchState.STATE_PRE_REGISTERED) {
                //TODO
                //check if the match qualified has been registered, ensure the registration of the match (about to start/already started) to DB and the static liveworkers
                //////if yes (def: both DB and local instance of boardcrawlee has record), set state to STATE_MATCH_START
                //////if no, register. Ensure boardcrawlee will not add same instance
                //note here is thread safe concern about static liveworker on class BoardCrawlee
                //
                //listen to the allodds xml, wait the MATCH_STAGE to "firsthalf", then
                System.out.println("Threadname: " + threadName + matchId + " STATE_PRE_REGISTERED");
                status = MatchState.STATE_MATCH_ENDED;
            } else if (status == MatchState.STATE_MATCH_START) {


                System.out.println("Threadname: " + threadName + matchId + " STATE_MATCH_START");
            } else if (status == MatchState.STATE_FUTURE_MATCH){
                //TODO add the match registration to DB
            }


            //System.out.println("Threadname: " + threadName + matchId);

           /* try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

    public void Kill() {
        //TODO: make sure detached from any pointing, and thread on this object is stopped()
    }
}
