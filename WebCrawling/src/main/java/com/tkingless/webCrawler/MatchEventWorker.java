package com.tkingless.webCrawler;

import com.tkingless.DBManager;
import com.tkingless.DBobject.APoolOddsDAO;
import com.tkingless.DBobject.MatchEventDAO;
import com.tkingless.MatchCONSTANTS;
import com.tkingless.MatchTestCONSTANTS;
import com.tkingless.MongoDBparam;
import com.tkingless.WebCrawling.DBobject.DateValuePair;
import com.tkingless.crawlee.BoardCrawlee;
import com.tkingless.crawlee.MatchCrawlee;
import com.tkingless.utils.logTest;
import com.tkingless.utils.DateTimeEntity;
import com.tkingless.MatchCONSTANTS.*;

import org.jsoup.nodes.Element;

import javax.xml.xpath.XPathExpressionException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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

    private final Set<MatchStage> onMatchingStages = EnumSet.of(MatchStage.STAGE_FIRST, MatchStage.STAGE_HALFTIME, MatchStage.STAGE_SECOND);
    private static final String threadName = "MatchEventWorker-thread";

    //The unique id for this worker
    private String matchId;
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

    MatchEventDAO workerDAO;
    APoolOddsDAO crleOddsDAO;

    public MatchEventWorker(String aMatchId, Element matchKeyEle, Element statusEle, Element homeEle, Element awayEle, MatchTestCONSTANTS.TestType type) throws ParseException {
        super(CrawlerKeyBinding.MatchEvent, threadName + "-" + aMatchId);
        testTypeSwitch = type;

        if(type != null){
            workerDAO = new MatchEventDAO(DBManager.getInstance().getClient(),DBManager.getInstance().getMorphia(), MongoDBparam.webCrawlingTestDB);
            crleOddsDAO = new APoolOddsDAO(DBManager.getInstance().getClient(),DBManager.getInstance().getMorphia(), MongoDBparam.webCrawlingTestDB);
        }else{
            workerDAO = new MatchEventDAO(DBManager.getInstance().getClient(),DBManager.getInstance().getMorphia());
            crleOddsDAO = new APoolOddsDAO(DBManager.getInstance().getClient(),DBManager.getInstance().getMorphia());
        }

        workerTime = new DateTimeEntity();
        status = MatchStatus.STATE_INITIALIZATION;
        matchId = aMatchId;
        logTest.logger.info("MatchEventWorker constructed, matchId:" + matchId);
        ///logTest.logger.info("and allOddsLink: " + linkAddr);

        ExtractMatcdKey(matchKeyEle);
        ExtractTeams(homeEle,awayEle);

        if (type == MatchTestCONSTANTS.TestType.TYPE_PRE_REG) {
            preRegperiod = 1000 * 10;
            long rectTimestamp = (long) (workerTime.GetTheInstant().getTime() + 0.5 * preRegperiod);
            commenceTime = new DateTimeEntity(rectTimestamp);
            IdentifyStage(statusEle);
        } else {
            ExtractStatus(statusEle);
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (status == MatchStatus.STATE_MATCH_ENDED || status == MatchStatus.STATE_TERMINATED) {
            logTest.logger.info("Threadname: " + threadName + matchId + " STATE_MATCH_ENDED||STATE_TERMINATED");

            if(lastMatchCrle != null) {
                logTest.logger.debug("status is end|terminated, last crle: " + lastMatchCrle.toString());
            }

            if(status == MatchStatus.STATE_MATCH_ENDED){
                logTest.logger.debug("trace2");

                if(lastMatchCrle == null || lastMatchCrle.getRecordTime() == null){
                    logTest.logger.debug("trace3");
                    endTime = new DateTimeEntity();
                }else{
                    logTest.logger.debug("trace4");
                    endTime = lastMatchCrle.getRecordTime();
                }


                workerDAO.SetField(this,"endTime",endTime.GetTheInstant());
                logTest.logger.info("Actual end time: "+ endTime.toString());

            }
            logTest.logger.debug("trace5");
            BoardCrawlee.DetachWorker(this);
        }

        logTest.logger.info("[LOG] Threadname: " + threadName + matchId + " Proc() escaped, and the state is: " + status.toString());
    }

    /*
    Constructor functions
     */

    private void ExtractMatcdKey(Element matchKeyEle) {
        matchKey = matchKeyEle.text();
        //logTest.logger.info("GetChildNodes(), matchKey: " + matchKey);
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

                String day = dayMatch.group();
                String time = timeMatch.group();

                StringBuilder dateTimeBuilder = new StringBuilder(time).append(":00 ");
                dateTimeBuilder.append(day);
                dateTimeBuilder.append("/");
                dateTimeBuilder.append(DateTimeEntity.GetCurrentYear());


                //=========================check whether next year datetime
                DateTimeEntity checkIfNextYear = new DateTimeEntity(dateTimeBuilder.toString(), new SimpleDateFormat("HH:mm:ss dd/MM/yyyy"));

                if(checkIfNextYear.CalTimeIntervalDiff(new DateTimeEntity()) < 0){
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

                if(workerDAO.QueryDataFieldExists(this,"commence")){
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

    private void ExtractTeams(Element homeEle, Element awayEle) {
        homeTeam = homeEle.text();
        awayTeam = awayEle.text();
        logTest.logger.info("ExtractTeams(), matchTeams: " + homeTeam + " vs " + awayTeam);
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
        logTest.logger.debug("trace100");
        //init the match DB data
        if(workerDAO.QueryDataFieldExists(this,"stageUpdates"))
            updateDifftr.remove(UpdateDifferentiator.UPDATE_STAGE);
        if(workerDAO.QueryDataFieldExists(this,"scoreUpdates"))
            updateDifftr.remove(UpdateDifferentiator.UPDATE_SCORES);
        if(workerDAO.QueryDataFieldExists(this,"cornerTotUpdates"))
            updateDifftr.remove(UpdateDifferentiator.UPDATE_CORNER);
        logTest.logger.debug("trace101");
        UpdateDBByDifftr(updateDifftr,lastMatchCrle);
        logTest.logger.debug("trace102");
        if(lastMatchCrle == null){
            logTest.logger.debug("[Worker] lastMatchCrle is null");
        }
        logTest.logger.debug("trace103");
        actualCommence = lastMatchCrle.getRecordTime();
        logTest.logger.debug("trace104");
        workerDAO.SetField(this,"actualCommence",actualCommence.GetTheInstant());
        logTest.logger.debug("trace105");
        scanPeriod = 1000;
        updateDifftr.clear();
        logTest.logger.debug("trace106");
        status = MatchStatus.STATE_MATCH_LOGGING;
        logTest.logger.info("Threadname: " + threadName + matchId + "Entered STATE_MATCH_LOGGING");
    }

    private int endGameConfirmCnt =0;
    private int endGameConfirm = 25;
    private int invalidEndCnt =0;
    private int invalidEndConfirm = 150;

    void OnStateMatchLogging() throws XPathExpressionException {

        if(terminateStates.contains(status)) {
            return;
        }

        if(!lastMatchCrle.isMatchXmlValid()) {
            logTest.logger.debug("[ENDREASON]lastMatchCrle is not valid");
            invalidEndCnt++;
            if(invalidEndCnt > invalidEndConfirm) {
                status = MatchStatus.STATE_MATCH_ENDED;
                return;
            }
        }

        if(stage == MatchStage.STAGE_SECOND){
            if(lastMatchCrle.isAllPoolClosed()) {
                logTest.logger.debug("[ENDREASON]last crle shown all pool closed");
                endGameConfirmCnt++;

                if(endGameConfirmCnt > endGameConfirm) {
                    status = MatchStatus.STATE_MATCH_ENDED;
                    return;
                }
            }
        }

        if (!updateDifftr.isEmpty()) {
            UpdateDBByDifftr(updateDifftr,lastMatchCrle);
            logTest.logger.info("updateDifftr not empty, last crle is: \n" + lastMatchCrle.toString());
        }

    }

    void OnStateFuture(){
        workerDAO.RegisterMatchEventWorker(this);
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

    /*
    Subsidary functions(): end
     */


    /*
    DB functions()
     */

    Set<UpdateDifferentiator> updateDifftr = EnumSet.noneOf(UpdateDifferentiator.class);

    private void UpdateDBByDifftr(Set<UpdateDifferentiator> difftr, MatchCrawlee crle) throws XPathExpressionException {

        for (UpdateDifferentiator differentiator : difftr) {
            switch (differentiator) {
                case UPDATE_STAGE:
                    DateValuePair DVPstage = new DateValuePair();
                    DVPstage.setTime(crle.getRecordTime().GetTheInstant());
                    DVPstage.setVal(MatchCONSTANTS.GetMatchStageStr(crle.getMatchStage()));
                    workerDAO.AddItemToListField(this,"stageUpdates", DVPstage);
                    break;
                case UPDATE_POOLS:
                    workerDAO.SetField(this,"poolTypes",matchPools);
                    break;
                case UPDATE_SCORES:
                    DateValuePair DVPscore = new DateValuePair();
                    DVPscore.setTime(crle.getRecordTime().GetTheInstant());
                    DVPscore.setVal(crle.getScores());
                    workerDAO.AddItemToListField(this,"scoreUpdates",DVPscore);
                    break;
                case UPDATE_CORNER:
                    if(!crle.getTotalCorners().contains("-")) {
                        DateValuePair DVPcorner = new DateValuePair();
                        DVPcorner.setTime(crle.getRecordTime().GetTheInstant());
                        DVPcorner.setVal(crle.getTotalCorners());
                        workerDAO.AddItemToListField(this, "cornerTotUpdates", DVPcorner);
                    }
                    break;
                case UPDATE_POOL_HAD:
                    crleOddsDAO.InsertOddPoolUpdates(matchId,crle,InplayPoolType.HAD);
                    break;
                case UPDATE_POOL_CHL:
                    crleOddsDAO.InsertOddPoolUpdates(matchId,crle,InplayPoolType.CHL);
                    break;
                default:
                    logTest.logger.error("[Worker] UpdateDBByDifftr() undefined type");
                    break;
            }
        }

        logTest.logger.debug("trace107");
        difftr.clear();
        logTest.logger.debug("trace108");
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

        if (testTypeSwitch != null) {
            newMatchCrle = new MatchCrawlee(matchCrleTestTarget);
        }
        else {
            newMatchCrle = new MatchCrawlee(this, matchId);
        }

        newMatchCrle.run();

        if (!newMatchCrle.isMatchXmlValid()) {
            logTest.logger.error("[Error] the grabbed xml is not valid");
            invalidTolerate ++;

            if(invalidTolerate>maxInvalidTolerate) {
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

    //this function is not touching DB part
    private void UpdateWorkerFromCrle(MatchCrawlee crle) {

        if (matchPools == null) {
            matchPools = crle.getPoolType();
            logTest.logger.info("ONE AND ONLY ONCE, MATCHPOOLS RECORDED: " + matchPools.toString());
        }

        stage = crle.getMatchStage();
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
