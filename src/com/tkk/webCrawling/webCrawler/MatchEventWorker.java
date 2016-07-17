package com.tkk.webCrawling.webCrawler;

import com.tkk.webCrawling.TestCases.MatchTestCONSTANTS;
import com.tkk.webCrawling.crawlee.BoardCrawlee;
import com.tkk.webCrawling.crawlee.MatchCrawlee;
import com.tkk.webCrawling.utils.DateTimeEntity;
import com.tkk.webCrawling.MatchCONSTANTS.*;

import org.jsoup.nodes.Element;

import javax.xml.xpath.XPathExpressionException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tkk.webCrawling.MatchCONSTANTS.MatchStatus.*;

/**
 * Created by tkingless on 6/26/16.
 */

//this is class is to be long-live thread worker to log happening event
public class MatchEventWorker extends baseCrawler {

    private final Set<MatchStage> onMatchingStages = EnumSet.of(MatchStage.STAGE_FIRST, MatchStage.STAGE_HALFTIME, MatchStage.STAGE_SECOND);
    private static final String threadName = "MatchEventWorker-thread";

    //The unique id for this worker
    private String matchId;
    private long scanPeriod = 0;
    private long preRegperiod = 1000 * 60 * 5;

    DateTimeEntity commenceTime;
    DateTimeEntity actualCommence;
    DateTimeEntity endTime;

    MatchStatus status;
    MatchStage stage;
    Set<InplayPoolType> matchPools = null;
    //the secondary key to be used
    String matchKey;
    String matchTeams;

    public MatchEventWorker(String aMatchId, Element matchKeyEle, Element statusEle, Element teamsEle, MatchTestCONSTANTS.TestType type) throws ParseException {
        super(CrawlerKeyBinding.MatchEvent, threadName + "-" + aMatchId);
        status = MatchStatus.STATE_INITIALIZATION;
        matchId = aMatchId;
        System.out.println("MatchEventWorker constructed, matchId:" + matchId);
        ///System.out.println("and allOddsLink: " + linkAddr);

        ExtractMatcdKey(matchKeyEle);
        ExtractTeams(teamsEle);

        if (type == MatchTestCONSTANTS.TestType.TYPE_PRE_REG) {
            testTypeSwitch = type;
            preRegperiod = 1000 * 10;
            long rectTimestamp = (long) ((new DateTimeEntity()).GetTheInstant().getTime() + 0.5 * preRegperiod);
            commenceTime = new DateTimeEntity(rectTimestamp);
            IdentifyStage(statusEle);
        } else {
            ExtractStatus(statusEle);
        }

        System.out.println("MatcherEventWorker finished constructer.");

        //do not use run(), to create worker threads
        this.NewThreadRun();
    }

    private Set<MatchStatus> terminateStates = EnumSet.of(STATE_MATCH_ENDED,
            STATE_INITIALIZATION_FAILURE, STATE_ALREADY_REGISTERED, STATE_TERMINATED);

    //The main loop function
    private void Proc() throws XPathExpressionException {

        while (!terminateStates.contains(status)) {
            switch (status) {
                case STATE_INITIALIZATION:
                    System.out.println("Threadname: " + threadName + matchId + " STATE_INITIALIZATION");
                    OnStateInitialization();
                    break;
                case STATE_PRE_REGISTERED:
                    System.out.println("Threadname: " + threadName + matchId + " STATE_PRE_REGISTERED");
                    EmitRequest();
                    OnStatePreRegistered();
                    break;
                case STATE_MATCH_START:
                    System.out.println("Threadname: " + threadName + matchId + " STATE_MATCH_START");
                    OnStateMatchStart();
                    break;
                case STATE_MATCH_LOGGING:
                    System.out.println("Threadname: " + threadName + matchId + " STATE_MATCH_LOGGING");
                    EmitRequest();
                    OnStateMatchLogging();
                    break;
                case STATE_FUTURE_MATCH:
                    System.out.println("Threadname: " + threadName + matchId + " STATE_FUTURE_MATCH");
                    OnStateFuture();
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

        if (status == MatchStatus.STATE_MATCH_ENDED || status == MatchStatus.STATE_TERMINATED) {
            if(status == MatchStatus.STATE_MATCH_ENDED){
                //TODO (DB feature) mark the actual end time
                endTime = new DateTimeEntity();
            }
            BoardCrawlee.DetachWorker(this);
        }

        System.out.println("[LOG] Threadname: " + threadName + matchId + " Proc() escaped, and the state is: " + status.toString());
    }

    /*
    Constructor functions
     */

    private void ExtractMatcdKey(Element matchKeyEle) {
        matchKey = matchKeyEle.text();
        System.out.println("GetChildNodes(), matchKey: " + matchKey);
    }

    private boolean noDBcommenceTimeHistory = false;

    //This function now only concern about commenceTime
    private void ExtractStatus(Element statusEle) throws ParseException {

        IdentifyStage(statusEle);

        switch (stage) {
            case STAGE_ESST:
                String startTimeWeb = statusEle.childNode(3).toString();

                Pattern dayPattern = Pattern.compile("[0-9]{1,2}/[0-9]{1,2}");
                Pattern timePattern = Pattern.compile("[0-9]{1,2}:[0-9]{1,2}");

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
                break;
            case STAGE_FIRST:
            case STAGE_HALFTIME:
            case STAGE_SECOND:
                //TODO (DB feature) try to load futureRecord to get the commenceTime

                if (commenceTime == null) {
                    System.out.println("[Warning] commenceTime is null, set commenceTime to now()");
                    commenceTime = new DateTimeEntity();
                    noDBcommenceTimeHistory = true;
                }
                break;
            default:
                System.out.println("[Error] no proper type input");
                break;
        }
    }

    private void IdentifyStage(Element statusEle) {
        if (statusEle.text().contains("Expected In Play start selling time")) {
            stage = MatchStage.STAGE_ESST;
        } else if (statusEle.text().contains("1st Half In Progress")) {
            stage = MatchStage.STAGE_FIRST;
        } else if (statusEle.text().contains("Half Time")) {
            stage = MatchStage.STAGE_HALFTIME;
        } else if (statusEle.text().contains("2nd Half In Progress")) {
            stage = MatchStage.STAGE_SECOND;
        }
    }

    private void ExtractTeams(Element teamsEle) {
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
            status = STATE_ALREADY_REGISTERED;
            return;
        }

        //TODO (DB feature) if the event ended, set status to ENDED

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
                status = STATE_INITIALIZATION_FAILURE;
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

        if (timediff > 0) {
            if (stage == MatchStage.STAGE_ESST) {
                //TODO finetune this longwait, sometime it can be even an half hour long!
                long longwait = timediff + 1000 * 3;
                scanPeriod = longwait;
                System.out.println("Threadname: " + threadName + matchId + " enter long wait in PRE reg state");

            }
        } else if (timediff <= 0) {
            if (stage == MatchStage.STAGE_ESST) {
                scanPeriod = 1000 * 2;
                System.out.println("dry waiting for start state");
            }
        }

        if (onMatchingStages.contains(stage)) {
            long shortwait = 1000;
            scanPeriod = shortwait;
            if (noDBcommenceTimeHistory) {
                //TODO (DB feature) update the event to DB
            }
            status = MatchStatus.STATE_MATCH_START;
        }

        registerOnlocal();
    }

    void OnStateMatchStart() {
        //TODO (DB feature) update the actual match start time and odds
        //TODO (DB feature) init relevant DB objects

        actualCommence = lastMatchCrle.getRecordTime();
        scanPeriod = 1000;

        status = MatchStatus.STATE_MATCH_LOGGING;
    }

    void OnStateMatchLogging() {

        if (shouldUpdateDB) {
            UpdateDB();
            System.out.println(lastMatchCrle.toString());
            shouldUpdateDB = false;
        }

        if(!lastMatchCrle.isMatchXmlValid())
            status = MatchStatus.STATE_MATCH_ENDED;

        if(stage == MatchStage.STAGE_HALFTIME){
            if(lastMatchCrle.isAllPoolClosed())
                status = MatchStatus.STATE_MATCH_ENDED;
        }

    }

    void OnStateFuture(){
        //TODO (DB feature) check whether DB added the match/ or the match changed
        //TODO (DB feature) add/update the match registration to DB
        status = STATE_TERMINATED;
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

    public void run() {
        try {
            ////System.out.println("MatchEventWorker run() called");
            Proc();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void Kill() {
        status = MatchStatus.STATE_TERMINATED;
    }

    void registerOnDB() {
        //TODO (DB feature)
    }

    void registerOnlocal() {
        BoardCrawlee.RegisterWorker(this);
    }

    public synchronized String getMatchId() {
        return matchId;
    }

    public synchronized MatchStatus getStatus() {
        return status;
    }

    public synchronized MatchStage getStage() {
        return stage;
    }

    public synchronized Set<InplayPoolType> getMatchPools() {
        return matchPools;
    }

    public DateTimeEntity getCommenceTime() {
        return commenceTime;
    }
    /*
    Subsidary functions(): end
     */


    /*
    DB functions()
     */

    boolean shouldUpdateDB = false;

    void UpdateDB() {
        //TODO (DB feature)
    }
    /*
    DB functions(): end
     */


    //Need to know what the crawlee should return for each state to the worker, so that worker know to coordinate
    /*
    MatchCrawlee functions()
     */
    private MatchCrawlee lastMatchCrle = null;

    private void EmitRequest() throws XPathExpressionException {
        MatchCrawlee newMatchCrle;

        if (testTypeSwitch == MatchTestCONSTANTS.TestType.TYPE_PRE_REG) {
            newMatchCrle = new MatchCrawlee(matchCrleTestTarget);
        } else {
            newMatchCrle = new MatchCrawlee(this, matchId);
        }

        newMatchCrle.run();

        if (!newMatchCrle.isMatchXmlValid()) {
            System.out.println("[Error] the grabbed xml is not valid");
            status = MatchStatus.STATE_TERMINATED;
            return;
        }

        if (MatchCrawlee.HasUpdate(lastMatchCrle, newMatchCrle)) {
            lastMatchCrle = newMatchCrle;
            UpdateWorkerFromCrle(newMatchCrle);
            shouldUpdateDB = true;
        }

    }

    private void UpdateWorkerFromCrle(MatchCrawlee crle) {

        if (matchPools == null) {
            //TODO (DB feature) update the pooltypes
            matchPools = crle.getPoolType();
            System.out.println("ONE AND ONLY ONCE, MATCHPOOLS RECORDED: " + matchPools.toString());
        }

        stage = crle.getMatchStage();
        //TODO other things (DB feature)
    }
    /*
    MatchCrawlee functions(): end
     */


    /*
    Test case useful methods()
    */
    public synchronized void setMatchCrleTestTarget(String matchCrleTestTarget) {
        this.matchCrleTestTarget = matchCrleTestTarget;
    }

    String matchCrleTestTarget;
    MatchTestCONSTANTS.TestType testTypeSwitch = null;
    /*
    Test case useful methods(): end
     */

}
