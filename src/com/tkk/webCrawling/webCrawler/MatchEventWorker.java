package com.tkk.webCrawling.webCrawler;

import com.tkk.webCrawling.crawlee.BoardCrawlee;
import com.tkk.webCrawling.utils.DateTimeEntity;
import com.tkk.webCrawling.MatchCONSTANTS.*;

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

    final Set<MatchStage> onMatchingStages = EnumSet.of(MatchStage.STAGE_FIRST, MatchStage.STAGE_HALFTIME, MatchStage.STAGE_SECOND);
    static final String threadName = "MatchEventWorker-thread";

    //The unique id for this worker
    String matchId;
    long scanPeriod = 0;
    long preRegperiod = 1000 * 60 * 5;
    final long matchIntervalLength = 1000 * 60 * 120;

    DateTimeEntity commenceTime;
    DateTimeEntity endTime;

    MatchStatus status;
    MatchStage stage;
    InplayPoolType matchPools;
    //the secondary key to be used
    String matchKey;
    String matchTeams;


    public MatchEventWorker(String aMatchId, Element matchKeyEle, Element statusEle, Element teamsEle) {
        super(CrawlerKeyBinding.MatchEvent, threadName + "-" + aMatchId);
        status = MatchStatus.STATE_INITIALIZATION;
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

    Set<MatchStatus> terminateStates = EnumSet.of(MatchStatus.STATE_MATCH_ENDED,
            MatchStatus.STATE_FUTURE_MATCH,
            MatchStatus.STATE_INITIALIZATION_FAILURE,
            MatchStatus.STATE_ALREADY_REGISTERED);

    //The main loop function
    void Proc() {

        while (!terminateStates.contains(status)) {

            switch (status) {

                case STATE_INITIALIZATION:
                    System.out.println("Threadname: " + threadName + matchId + " STATE_INITIALIZATION");
                    OnStateInitialization();
                    break;
                case STATE_PRE_REGISTERED:
                    System.out.println("Threadname: " + threadName + matchId + " STATE_PRE_REGISTERED");
                    //TODO check if crawlee return matchstate started, change to this state, call MatchCrawlee here
                    //TODO listen to the allodds xml, wait the MATCH_STAGE to "firsthalf"

                    OnStatePreRegistered();
                    break;
                case STATE_MATCH_START:
                    //TODO (DB feature) update the actual match start time
                    //TODO set the scanPeriod shorter
                    //TODO (DB feature) init relevant DB objects
                    System.out.println("Threadname: " + threadName + matchId + " STATE_MATCH_START");
                    status = MatchStatus.STATE_MATCH_ENDED;
                    break;
                case STATE_MATCH_LOGGING:
                    System.out.println("Threadname: " + threadName + matchId + " STATE_MATCH_LOGGING");
                    status = MatchStatus.STATE_MATCH_ENDED;
                    break;
                case STATE_FUTURE_MATCH:
                    //TODO (DB feature) check whether DB added the match/ or the match changed
                    //TODO (DB feature) add/update the match registration to DB
                    break;
                default:
                    System.out.println("Threadname: " + threadName + matchId + " unknown state");
                    break;
            }

            try {
                sleep(scanPeriod);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("[LOG] Threadname: " + threadName + matchId + " Proc() escaped, and the state is: " + status.toString());
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
        } else {

            //TODO (DB feature) try to load futureRecord to get the commenceTime

            if (commenceTime == null) {
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
    void OnStateInitialization() {

        if (BoardCrawlee.IsRegisteredByID(this)) {
            status = MatchStatus.STATE_ALREADY_REGISTERED;
            return;
        }

        long timediff = 0;
        if (!noDBcommenceTimeHistory) {
            timediff = commenceTime.CalTimeIntervalDiff(new DateTimeEntity());
            System.out.println("timediff: " + timediff + " match id: " + matchId);
        }

        //only pass considered cases
        if (timediff > 0) {
            if (stage == MatchStage.STAGE_ESST) {
                //Within the pre-wait interval
                if (timediff < preRegperiod) {
                    status = MatchStatus.STATE_PRE_REGISTERED;
                //Beyond the pre-wait interval
                } else if (timediff > preRegperiod) {
                    status = MatchStatus.STATE_FUTURE_MATCH;
                }
            } else {
                status = MatchStatus.STATE_INITIALIZATION_FAILURE;
            }
        } else if (timediff <= 0) {
            //case that before registration, the event already started
            if (onMatchingStages.contains(stage)) {
                status = MatchStatus.STATE_PRE_REGISTERED;
                scanPeriod = 0;
            }
            //there is possibility the match actual starting time is delayed a bit
            else if (stage == MatchStage.STAGE_ESST) {
                status = MatchStatus.STATE_PRE_REGISTERED;
            }
        }
    }

    void OnStatePreRegistered() {

        long timediff = commenceTime.CalTimeIntervalDiff(new DateTimeEntity());

        if (timediff > 0){
            if (stage == MatchStage.STAGE_ESST) {
                long longwait = timediff + 1000 * 15;
                scanPeriod = longwait;
                System.out.println("Threadname: " + threadName + matchId + " enter long wait in PRE reg state");

            }
        } else if (timediff <= 0){
            if( stage == MatchStage.STAGE_ESST) {
                scanPeriod = 1000 * 7;
                System.out.println("dry waiting for start state");
            }
        }

        if (onMatchingStages.contains(stage)){
            long shortwait = 1000;
            scanPeriod = shortwait;
            if (noDBcommenceTimeHistory){
                //TODO (DB feature) update the event to DB
            }
            status = MatchStatus.STATE_MATCH_START;
        }

        BoardCrawlee.RegisterWorker(this);
    }
    /*
    OnState actions(): end
     */


    /*
    Subsidary functions()
     */
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

    public void Kill() {
        //TODO: make sure detached from any pointing, and thread on this object is stopped()
        BoardCrawlee.DetachWorker(this);
    }

    void registerOnDB() {
        //TODO
    }

    void registerOnlocal() {

    }

    public String getMatchId() {
        return matchId;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public MatchStage getStage() {
        return stage;
    }

    public InplayPoolType getMatchPools() {
        return matchPools;
    }
    /*
    Subsidary functions(): end
     */


    /*
    DB functions()
     */
    boolean ShouldUpdateDB() {
        //TODO
        return false;
    }

    void UpdateDB() {
        //TODO
    }
    /*
    DB functions(): end
     */


    //Need to know what the crawlee should return for each state to the worker, so that worker know to coordinate
    /*
    MatchCrawlee functions()
     */

    /*
    MatchCrawlee functions(): end
     */




    /*
    Test case useful methods()
    */
    public void setMatchCrleTarget(String matchCrleTarget) {
        this.matchCrleTarget = matchCrleTarget;
    }
    String matchCrleTarget;
    /*
    Test case useful methods(): end
     */

}
