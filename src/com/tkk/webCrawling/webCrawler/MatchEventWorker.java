package com.tkk.webCrawling.webCrawler;

import com.tkk.webCrawling.crawleeClass.BoardCrawlee;
import com.tkk.webCrawling.utils.DateTimeEntity;
import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tkingless on 6/26/16.
 */

//this is class is to be long-live thread worker to log happening event
public class MatchEventWorker extends baseCrawler {


    //TODO move out these enum to a static class
    enum MatchState {
        STATE_INITIALIZATION,
        STATE_FUTURE_MATCH,
        //only at state of pre_registered, the thread is launched
        STATE_PRE_REGISTERED,
        STATE_MATCH_START,
        STATE_MATCH_LOGGING,
        STATE_MATCH_ENDED,
        STATE_INITIALIZATION_FAILURE,
        STATE_ALREADY_REGISTERED
    }

    enum MatchStage {
        STAGE_ESST,
        STAGE_FIRST,
        STAGE_HALFTIME,
        STAGE_SECOND,
    }
    Set<MatchStage> onMatchingStages = EnumSet.of(MatchStage.STAGE_FIRST,MatchStage.STAGE_HALFTIME,MatchStage.STAGE_SECOND);

    static final String threadName = "MatchEventWorker-thread";

    public String getMatchId() {
        return matchId;
    }

    //The unique id for this worker
    String matchId;
    long scanPeriod = 0;
    long preRegperiod = 1000 * 60 * 5;
    final long matchIntervalLength = 1000 * 60 * 120;

    DateTimeEntity commenceTime;
    DateTimeEntity endTime;
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



    public boolean IsLive() {
        return thread.isAlive();
    }

    public void LaunchProcess() {
        //TODO, if fullfill criteria, start a monitoring thread
    }

    void EmitRequest() {
        //TODO grab data periodically
    }

    public void run() {
        try {
            ////System.out.println("MatchEventWorker run() called");
            Proc();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    Set<MatchState> terminateStates = EnumSet.of(MatchState.STATE_MATCH_ENDED,
            MatchState.STATE_FUTURE_MATCH,
            MatchState.STATE_INITIALIZATION_FAILURE,
            MatchState.STATE_ALREADY_REGISTERED);

    //TODO split this larger in a more cleaner readable way
    //The main loop function
    void Proc() {

        while (!terminateStates.contains(status)) {

            if (status == MatchState.STATE_INITIALIZATION) {
                System.out.println("Threadname: " + threadName + matchId + " STATE_INITIALIZATION");
                OnStateInitialization();

            } else if (status == MatchState.STATE_PRE_REGISTERED) {
                System.out.println("Threadname: " + threadName + matchId + " STATE_PRE_REGISTERED");
                OnStatePreRegistered();

            } else if (status == MatchState.STATE_MATCH_START) {
                //TODO listen to the allodds xml, wait the MATCH_STAGE to "firsthalf", then
                //TODO (DB feature) update the actual match start time
                //TODO set the scanPeriod shorter
                //TODO (DB feature) init relevant DB objects
                System.out.println("Threadname: " + threadName + matchId + " STATE_MATCH_START");
                status = MatchState.STATE_MATCH_ENDED;

            } else if (status == MatchState.STATE_MATCH_LOGGING){
                System.out.println("Threadname: " + threadName + matchId + " STATE_MATCH_LOGGING");
                status = MatchState.STATE_MATCH_ENDED;

            } else if (status == MatchState.STATE_FUTURE_MATCH){
                //TODO (DB feature) add the match registration to DB

            }

            //System.out.println("Threadname: " + threadName + matchId);

           /* try {
                sleep(scanPeriod);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }

        System.out.println("[LOG] Threadname: " + threadName + matchId + " Proc() escaped, and the state is: " + status.toString());
    }

    public void Kill() {
        //TODO: make sure detached from any pointing, and thread on this object is stopped()
        BoardCrawlee.DetachWorker(this);
    }

    void registerOnDB() {
        //TODO
    }

    void registerOnlocal() {

    }



    /*
    Constructor functions
     */

    void ExtractMatcdKey(Element matchKeyEle) {
        matchKey = matchKeyEle.text();
        System.out.println("GetChildNodes(), matchKey: " + matchKey);
    }

    private boolean noDBcommenceTimeHistory = false;
    void ExtractStatus(Element statusEle) throws ParseException {
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
        }else {

            //TODO (DB feature) try to load futureRecord to get the commenceTime

            if(commenceTime == null) {
                System.out.println("[Warning] commenceTime is null, set commenceTime to now()");
                commenceTime = new DateTimeEntity();
                noDBcommenceTimeHistory = true;
            }

            if (statusEle.text().contains("1st Half In Progress")) {
                //TODO something
                stage = MatchStage.STAGE_FIRST;
            } else if (statusEle.text().contains("Half Time")) {
                //TODO something
                stage = MatchStage.STAGE_HALFTIME;
            } else if (statusEle.text().contains("2nd Half In Progress")) {
                //TODO something
                stage = MatchStage.STAGE_SECOND;
            }
        }
    }

    void ExtractTeams(Element teamsEle) {
        matchTeams = teamsEle.text();
        System.out.println("GetChildNodes(), matchTeams: " + matchTeams);
    }

    /*
    Constructor functions: end
     */

//=============================================================================================

    /*
    OnState actions()
     */
    void OnStateInitialization(){
        long timediff = 0;
        if (!noDBcommenceTimeHistory){
            timediff = commenceTime.CalTimeIntervalDiff(new DateTimeEntity());
            System.out.println("timediff: " + timediff + " match id: " + matchId);
        }

        //only pass considered cases
        if(timediff > 0 && stage == MatchStage.STAGE_ESST){
            if(timediff < preRegperiod){
                status = MatchState.STATE_PRE_REGISTERED;
            }else if (timediff > preRegperiod){
                status = MatchState.STATE_FUTURE_MATCH;
            }
        }else if(timediff <= 0){
            if (onMatchingStages.contains(stage)){
                status = MatchState.STATE_PRE_REGISTERED;
                scanPeriod = 0;
            }
        }
        else{
            status = MatchState.STATE_INITIALIZATION_FAILURE;
        }
    }

    void OnStatePreRegistered () {
        if(BoardCrawlee.IsRegisteredByID(this)){
            status = MatchState.STATE_ALREADY_REGISTERED;
            return;
        }
        if(stage == MatchStage.STAGE_ESST){
            long longwait = commenceTime.CalTimeIntervalDiff(new DateTimeEntity()) + 1000 * 15;
            scanPeriod = longwait;
            //TODO (DB feature) update the event to DB
            status = MatchState.STATE_MATCH_START;

        }else
        if(noDBcommenceTimeHistory && onMatchingStages.contains(stage)){
            //TODO (DB feature) insert the instant event to DB
            System.out.println("TODO: Threadname: " + threadName + matchId + " register to DB");

            long shortwait = 1000;
            scanPeriod = shortwait;
            status = MatchState.STATE_MATCH_LOGGING;
        }
        BoardCrawlee.RegisterWorker(this);
    }
    /*
    OnState actions(): end
     */
}
