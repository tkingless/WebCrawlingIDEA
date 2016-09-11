package com.tkingless.webCrawler;

import com.tkingless.*;
import com.tkingless.DBobject.APoolOddsDAO;
import com.tkingless.DBobject.MatchEventDAO;
import com.tkingless.WebCrawling.DBobject.InPlayAttrUpdates;
import com.tkingless.crawlee.BoardCrawlee;
import com.tkingless.crawlee.MatchCrawlee;
import com.tkingless.utils.logTest;
import com.tkingless.utils.DateTimeEntity;
import com.tkingless.MatchCONSTANTS.*;

import javax.xml.xpath.XPathExpressionException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tkingless.MatchCONSTANTS.MatchStatus.*;

/**
 * Created by tkingless on 6/26/16.
 */

//this is class is to be long-live thread worker to log happening event
public class MatchEventWorker extends baseCrawler {

    private final Set<MatchStage> onMatchingStages = EnumSet.of(MatchStage.STAGE_FIRST, MatchStage.STAGE_HALFTIME, MatchStage.STAGE_SECOND, MatchStage.STAGE_FULLTIME);
    private static final String threadName = "MatchEventWorker-thread";

    //The unique id for this worker
    private Integer matchId;
    private long scanPeriod = 0;
    private long preRegperiod = 1000 * 60 * 5;

    DateTimeEntity commenceTime;
    DateTimeEntity actualCommence;
    DateTimeEntity endTime;
    DateTimeEntity workerTime;

    MatchStatus status;
    MatchStage stage;
    Set<InplayPoolType> matchPools = null;
    //the secondary key to be used
    String matchKey;
    String homeTeam;
    String awayTeam;
    String league;

    MatchEventDAO workerDAO;
    APoolOddsDAO crleOddsDAO;

    public MatchEventWorker(MatchCarrier carrier, MatchTestCONSTANTS.TestType type) throws ParseException {
        super(CrawlerKeyBinding.MatchEvent, threadName + "-" + carrier.getMatchId());
        testTypeSwitch = type;

        if(type != null){
            workerDAO = new MatchEventDAO(DBManager.getInstance().getClient(),DBManager.getInstance().getMorphia(), MongoDBparam.webCrawlingTestDB);
            crleOddsDAO = new APoolOddsDAO(DBManager.getInstance().getClient(),DBManager.getInstance().getMorphia(), MongoDBparam.webCrawlingTestDB);
        }else{
            workerDAO = new MatchEventDAO(DBManager.getInstance().getDatastore());
            crleOddsDAO = new APoolOddsDAO(DBManager.getInstance().getDatastore());
        }

        workerTime = new DateTimeEntity();
        status = MatchStatus.STATE_INITIALIZATION;
        matchId = carrier.getMatchId();
        logTest.logger.info("MatchEventWorker constructed, matchId:" + matchId);
        ///logTest.logger.info("and allOddsLink: " + linkAddr);

        matchKey = carrier.getMatchNo();
        homeTeam = carrier.getHomeTeam();
        awayTeam = carrier.getAwayTeam();
        league = carrier.getLeague();

        if (type == MatchTestCONSTANTS.TestType.TYPE_PRE_REG) {
            preRegperiod = 1000 * 10;
            long rectTimestamp = (long) (workerTime.GetTheInstant().getTime() + 0.5 * preRegperiod);
            commenceTime = new DateTimeEntity(rectTimestamp);
            IdentifyStage(carrier.getMatchStatusText()); //this usage is for test case use only
        } else {
            ExtractStatus(carrier.getMatchStatusText(),carrier.getMatchStatusTime());
        }

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
                    logTest.logger.info("Threadname: " + threadName + matchId + " STATE_INITIALIZATION");
                    OnStateInitialization();
                    break;
                case STATE_PRE_REGISTERED:
                    logTest.logger.info("Threadname: " + threadName + matchId + " STATE_PRE_REGISTERED");
                    EmitRequest();
                    OnStatePreRegistered();
                    break;
                case STATE_MATCH_START:
                    logTest.logger.info("Threadname: " + threadName + matchId + " STATE_MATCH_START");
                    OnStateMatchStart();
                    break;
                case STATE_MATCH_LOGGING:
                    //logTest.logger.info("Threadname: " + threadName + matchId + " STATE_MATCH_LOGGING");
                    EmitRequest();
                    OnStateMatchLogging();
                    break;
                case STATE_FUTURE_MATCH:
                    logTest.logger.info("Threadname: " + threadName + matchId + " STATE_FUTURE_MATCH");
                    OnStateFuture();
                    break;
                default:
                    logTest.logger.info("Threadname: " + threadName + matchId + " unknown state");
                    break;
            }

            try {
                sleep(scanPeriod);
            } catch (Exception e) {
                logTest.logger.error("Proc() error", e);
            }
        }

        if (status == MatchStatus.STATE_MATCH_ENDED || status == MatchStatus.STATE_TERMINATED) {
            logTest.logger.info("Threadname: " + threadName + matchId + " STATE_MATCH_ENDED||STATE_TERMINATED");

            if(lastMatchCrle != null) {
                logTest.logger.debug("status is end|terminated, last crle: " + lastMatchCrle.toString());
            }

            if(status == MatchStatus.STATE_MATCH_ENDED){

                if(lastMatchCrle == null || lastMatchCrle.getRecordTime() == null){
                    endTime = new DateTimeEntity();
                }else{
                    endTime = lastMatchCrle.getRecordTime();
                }

                workerDAO.SetField(this,"endTime",endTime.GetTheInstant());
                logTest.logger.info("Actual end time: "+ endTime.toString());

            }
            BoardCrawlee.DetachWorker(this);
        }

        logTest.logger.info("[LOG] Threadname: " + threadName + matchId + " Proc() escaped, and the state is: " + status.toString());
    }

    /*
    Constructor functions
     */

    private boolean noDBcommenceTimeHistory = false;

    //This function now only concern about commenceTime
    private void ExtractStatus(String statusText,String startTimeWeb) throws ParseException {

        IdentifyStage(statusText);

        try {
            switch (stage) {
                case STAGE_ESST:

                    Pattern dayPattern = Pattern.compile("[0-9]{1,2}/[0-9]{1,2}");
                    Pattern timePattern = Pattern.compile("[0-9]{1,2}:[0-9]{1,2}");

                    Matcher dayMatch = dayPattern.matcher(startTimeWeb);
                    Matcher timeMatch = timePattern.matcher(startTimeWeb);

                    dayMatch.find();
                    timeMatch.find();

                    String day = dayMatch.group();
                    String time = timeMatch.group();

                    StringBuilder dateTimeBuilder = new StringBuilder(time).append(":00 ");
                    dateTimeBuilder.append(day);
                    dateTimeBuilder.append("/");
                    dateTimeBuilder.append(DateTimeEntity.GetCurrentYear());

                    //=========================check whether next year datetime
                    DateTimeEntity checkIfNextYear = new DateTimeEntity(dateTimeBuilder.toString(), new SimpleDateFormat("HH:mm:ss dd/MM/yyyy"));

                    if (checkIfNextYear.CalTimeIntervalDiff(new DateTimeEntity()) < 0) {
                        dateTimeBuilder = new StringBuilder(time).append(":00 ");
                        dateTimeBuilder.append(day);
                        dateTimeBuilder.append("/");
                        dateTimeBuilder.append(DateTimeEntity.GetYearPlus(1));
                    }
                    //=========================check whether next year datetime: end

                    commenceTime = new DateTimeEntity(dateTimeBuilder.toString(), new SimpleDateFormat("HH:mm:ss dd/MM/yyyy"));
                    logTest.logger.info("ExtractStatus(),commenceTime: " + commenceTime.toString());
                    break;
                case STAGE_FIRST:
                case STAGE_HALFTIME:
                case STAGE_SECOND:
                case STAGE_FULLTIME:

                    if (workerDAO.QueryDataFieldExists(this, "commence")) {
                        long timestampOfcommence = workerDAO.findByMatchId(matchId).getCommence().getTime();
                        commenceTime = new DateTimeEntity(timestampOfcommence);
                    }

                    if (commenceTime == null) {
                        logTest.logger.info("[Warning] commenceTime is null");
                        noDBcommenceTimeHistory = true;
                    }
                    break;
                default:
                    logTest.logger.info("[Error] no proper type input");
                    break;
            }
        } catch(Exception e){
            logTest.logger.error("ExtractStatus() error",e);

        }
    }

    private void IdentifyStage(String statusText) {
        if (statusText.contains("Expected In Play start selling time")) {
            stage = MatchStage.STAGE_ESST;
        } else if (statusText.contains("1st Half In Progress")) {
            stage = MatchStage.STAGE_FIRST;
        } else if (statusText.contains("Half Time")) {
            stage = MatchStage.STAGE_HALFTIME;
        } else if (statusText.contains("2nd Half In Progress")) {
            stage = MatchStage.STAGE_SECOND;
        } else if (statusText.contains("Full Time")) {
            stage = MatchStage.STAGE_FULLTIME;
        } else
        {
            logTest.logger.error("This is unknwon stage: " + statusText);
        }
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

        if(workerDAO.QueryDataFieldExists(this,"endTime")){
            logTest.logger.debug("match worker id: "+ matchId + "found ended time");
            status = MatchStatus.STATE_TERMINATED;
            return;
        }else{
            logTest.logger.debug("match worker id: "+ matchId + "not found ended time yet");
        }

        long timediff = 0;
        if (!noDBcommenceTimeHistory) {
            timediff = commenceTime.CalTimeIntervalDiff(new DateTimeEntity());
            logTest.logger.info("timediff: " + timediff + " match id: " + matchId);
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

                if(testTypeSwitch == MatchTestCONSTANTS.TestType.TYPE_MATCHING || testTypeSwitch == MatchTestCONSTANTS.TestType.TYPE_ENDED){
                    scanPeriod = 500;
                }
            }
            //there is possibility the match actual starting time is delayed a bit
            else if (stage == MatchStage.STAGE_ESST) {
                status = MatchStatus.STATE_PRE_REGISTERED;
            }
        }
    }

    void OnStatePreRegistered() {

        if(terminateStates.contains(status)) {
            return;
        }

        long timediff = 0;

        if(commenceTime != null) {
           timediff =commenceTime.CalTimeIntervalDiff(new DateTimeEntity());
        }

        if (timediff > 0) {
            if (stage == MatchStage.STAGE_ESST) {
                long longwait = timediff + 1000 * 3;
                scanPeriod = longwait;
                logTest.logger.info("Threadname: " + threadName + matchId + " enter long wait in PRE reg state");

            }
        } else if (timediff <= 0) {
            if (stage == MatchStage.STAGE_ESST) {
                scanPeriod = scanPeriod + 1000 * 2;
                logTest.logger.info("dry waiting for start state");

                if(timediff <= -1 * 1000 * 60 * 180 * 4){
                    logTest.logger.error("waited too long for start state, terminating...");
                    status = MatchStatus.STATE_TERMINATED;
                }
            }
        }

        if (onMatchingStages.contains(stage)) {
            long shortwait = 1000;
            scanPeriod = shortwait;
            if (noDBcommenceTimeHistory) {
                workerDAO.RegisterMatchEventWorker(this);
            }
            status = MatchStatus.STATE_MATCH_START;
        }

        //This is added later on as workaround for service started just at pre-reg time, the chance is supposed to be low though...
        if(stage == MatchStage.STAGE_ESST){
            if(!workerDAO.IsMatchRegisteredBefore(matchId)) {
                workerDAO.RegisterMatchEventWorker(this);
            }
        }

        if(!BoardCrawlee.IsRegisteredByID(this)) {
            registerOnlocal();
        }

    }

    void OnStateMatchStart() throws XPathExpressionException {

        if(terminateStates.contains(status)) {
            return;
        }
        //init the match DB data
        //TODO
        // In case re-entering logging event, workaround, do not immediately update these three already recorded,
        //of course if there is real update between shutdown and restart, no good....
        if(workerDAO.QueryDataFieldExists(this,"stageUpdates")) {
            logTest.logger.debug("OnStateMarchStart(), existing stageUpdate");
            updateDifftr.remove(UpdateDifferentiator.UPDATE_STAGE);
        }else{
            logTest.logger.debug("OnStateMarchStart(), not existing stageUpdate!!!!");
        }
        if(workerDAO.QueryDataFieldExists(this,"scoreUpdates")) {
            logTest.logger.debug("OnStateMarchStart(), existing scoreUpdates");
            updateDifftr.remove(UpdateDifferentiator.UPDATE_SCORES);
        }else{
            logTest.logger.debug("OnStateMarchStart(), not existing scoreUpdates!!!!");
        }
        if(workerDAO.QueryDataFieldExists(this,"cornerTotUpdates")) {
            logTest.logger.debug("OnStateMarchStart(), existing cornerTotUpdates");
            updateDifftr.remove(UpdateDifferentiator.UPDATE_CORNER);
        }else{
            logTest.logger.debug("OnStateMarchStart(), not existing cornerTotUpdates!!!!");
        }

        UpdateDBByDifftr(updateDifftr,lastMatchCrle);
        if(lastMatchCrle == null){
            logTest.logger.debug("[Worker] lastMatchCrle is null");
        }

        actualCommence = lastMatchCrle.getRecordTime();

        if(!workerDAO.QueryDataFieldExists(this,"actualCommence")) {
            workerDAO.SetField(this, "actualCommence", actualCommence.GetTheInstant());
        }

        scanPeriod = 1000;
        updateDifftr.clear();
        status = MatchStatus.STATE_MATCH_LOGGING;
        logTest.logger.info("Threadname: " + threadName + matchId + "Entered STATE_MATCH_LOGGING");
    }

    private int endGameConfirmCnt =0;
    private int endGameConfirm = 25;
    private int invalidEndCnt =0;
    private int invalidEndConfirm = 150;

    void OnStateMatchLogging() throws XPathExpressionException {

        try {

            if (terminateStates.contains(status)) {
                return;
            }

            if (!lastMatchCrle.isMatchXmlValid()) {
                logTest.logger.debug("[ENDREASON]lastMatchCrle is not valid");
                invalidEndCnt++;
                if (invalidEndCnt > invalidEndConfirm) {
                    status = MatchStatus.STATE_MATCH_ENDED;
                    return;
                }
            }

            if (stage == MatchStage.STAGE_SECOND || stage == MatchStage.STAGE_FULLTIME) {
                if (lastMatchCrle.isAllPoolClosed()) {
                    logTest.logger.debug("[ENDREASON]last crle shown all pool closed");
                    endGameConfirmCnt++;

                    if (endGameConfirmCnt > endGameConfirm) {
                        status = MatchStatus.STATE_MATCH_ENDED;
                        return;
                    }
                }
            }

            if (!updateDifftr.isEmpty()) {
                UpdateDBByDifftr(updateDifftr, lastMatchCrle);
                logTest.logger.info("updateDifftr not empty, last crle is: \n" + lastMatchCrle.toString());
            }

        }catch (Exception e){
            logTest.logger.error("MatchEventWorker error", e);
        }

    }

    void OnStateFuture(){
        workerDAO.RegisterMatchEventWorker(this);

        try{
            MatchCrawlee matchCrle = GetMatchCrleRun();

            if(matchCrle.getJsoupDoc() != null){
                if(!workerDAO.QueryDataFieldExists(this,"poolTypes")){
                    if(matchCrle.getPoolType() != null) {
                        workerDAO.SetField(this, "poolTypes", matchCrle.getPoolType());
                    }
                }
            }

        } catch (Exception e){
            logTest.logger.error("OnStateFuture() error",e);
        }

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
            ////logTest.logger.info("MatchEventWorker run() called");
            Proc();
        } catch (Exception e) {
            logTest.logger.error(e);
        }
    }

    public void Kill() {
        status = MatchStatus.STATE_TERMINATED;
    }

    void registerOnlocal() {
        BoardCrawlee.RegisterWorker(this);
    }

    public synchronized Integer getMatchId() {
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

    public String getMatchKey() {
        return matchKey;
    }

    //return worker constructed time
    public DateTimeEntity getWorkerTime() {
        return workerTime;
    }

    public DateTimeEntity getLastModifiedTime() {
        if(lastMatchCrle == null)
            return new DateTimeEntity();
        else
            return lastMatchCrle.getRecordTime();
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getLeague() { return  league;}



    /*
    Subsidary functions(): end
     */


    /*
    DB functions()
     */

    Set<UpdateDifferentiator> updateDifftr = EnumSet.noneOf(UpdateDifferentiator.class);

    private void UpdateDBByDifftr(Set<UpdateDifferentiator> difftr, MatchCrawlee crle) {

        try {

            if (crle == null) {
                logTest.logger.debug("crle input is null");
            }

            if (crle.getRecordTime() == null) {
                logTest.logger.debug("input crle.getRecordTime() is null");
            }

            if (crle.getMatchStage() == null) {
                logTest.logger.debug("input crle.getMatchStage() is null");
            }

            for (UpdateDifferentiator differentiator : difftr) {
                switch (differentiator) {
                    case UPDATE_STAGE:
                        InPlayAttrUpdates DVPstage = new InPlayAttrUpdates();
                        DVPstage.setMatchId(matchId);
                        DVPstage.setRecorded(crle.getRecordTime().GetTheInstant());
                        DVPstage.setVal(MatchCONSTANTS.GetMatchStageStr(crle.getMatchStage()));
                        DVPstage.setType("stage");
                        workerDAO.UpdateInplayStage(this,DVPstage);
                        break;
                    case UPDATE_POOLS:
                        logTest.logger.debug("[UpdateDBByDifftr] matchEvent pool to be updated");
                        workerDAO.SetField(this, "poolTypes", matchPools);
                        break;
                    case UPDATE_SCORES:
                        InPlayAttrUpdates DVPscore = new InPlayAttrUpdates();
                        DVPscore.setMatchId(matchId);
                        DVPscore.setRecorded(crle.getRecordTime().GetTheInstant());
                        DVPscore.setVal(crle.getScores());
                        DVPscore.setType("score");
                        workerDAO.UpdateInplayScore(this,DVPscore);
                        break;
                    case UPDATE_CORNER:
                        if (!crle.getTotalCorners().contains("-") || crle.getTotalCorners().isEmpty()) {
                            InPlayAttrUpdates DVPcorner = new InPlayAttrUpdates();
                            DVPcorner.setMatchId(matchId);
                            DVPcorner.setRecorded(crle.getRecordTime().GetTheInstant());
                            DVPcorner.setVal(crle.getTotalCorners());
                            DVPcorner.setType("corner");
                            workerDAO.UpdateInplayCorner(this,DVPcorner);
                        }
                        break;
                    case UPDATE_POOL_HAD:
                        crleOddsDAO.InsertOddPoolUpdates(matchId, crle, InplayPoolType.HAD);
                        break;
                    case UPDATE_POOL_CHL:
                        crleOddsDAO.InsertOddPoolUpdates(matchId, crle, InplayPoolType.CHL);
                        break;
                    default:
                        logTest.logger.warn("[Worker] UpdateDBByDifftr() undefined type");
                        break;
                }
            }
            difftr.clear();
        }catch (Exception e){
            logTest.logger.error("MatchEventworker error: ", e);
        }
    }


    /*
    DB functions(): end
     */


    //Need to know what the crawlee should return for each state to the worker, so that worker know to coordinate
    /*
    MatchCrawlee functions()
     */
    private MatchCrawlee lastMatchCrle = null;
    private int invalidTolerate = 0;
    private int maxInvalidTolerate = 150;

    private void EmitRequest() throws XPathExpressionException {
        MatchCrawlee newMatchCrle;

        newMatchCrle = GetMatchCrleRun();

        if (!newMatchCrle.isMatchXmlValid()) {
            logTest.logger.error("[Error] the grabbed xml is not valid");
            invalidTolerate ++;

            if(invalidTolerate>maxInvalidTolerate) {

                //somtimes, the match xml just close too quickly, so add a consideration to exemplify it as ended match
                if(workerDAO.QueryDataFieldExists(this,"scoreUpdates"))
                    if(workerDAO.QueryDataFieldExists(this,"stageUpdates"))
                        if(workerDAO.QueryDataFieldExists(this,"actualCommence")){
                            logTest.logger.debug("[nullpo] this exceptional case happened");
                            status = MatchStatus.STATE_MATCH_ENDED;
                            return;
                        }

                status = MatchStatus.STATE_TERMINATED;
            }
            return;
        }

        updateDifftr = MatchCrawlee.UpdateDifferentiator(lastMatchCrle,newMatchCrle);

        if (!updateDifftr.isEmpty()) {
            UpdateWorkerFromCrle(newMatchCrle);
            lastMatchCrle = newMatchCrle;
        }

    }

    MatchCrawlee GetMatchCrleRun (){

        MatchCrawlee newMatchCrle;

        if (testTypeSwitch != null) {
            newMatchCrle = new MatchCrawlee(matchCrleTestTarget);
        }
        else {
            newMatchCrle = new MatchCrawlee(this, matchId.toString());
        }

        newMatchCrle.run();

        return newMatchCrle;
    }

    //this function is not touching DB part
    private void UpdateWorkerFromCrle(MatchCrawlee crle) {

        try {
            if (matchPools == null) {
                matchPools = crle.getPoolType();
                logTest.logger.info("ONE AND ONLY ONCE, MATCHPOOLS RECORDED: " + matchPools.toString());
            }

            if (crle.getMatchStage() != null) {
                stage = crle.getMatchStage();
            } else {
                logTest.logger.error("[getStage] crle get Match has null pointer exception");
            }
        } catch (Exception e){
            logTest.logger.error("[UpdateWorkerFromCrle] error",e);
        }
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
