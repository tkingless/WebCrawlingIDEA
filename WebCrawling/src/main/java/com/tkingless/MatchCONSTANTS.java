package com.tkingless;

import com.tkingless.utils.logTest;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tsangkk on 7/5/16.
 */
public class MatchCONSTANTS {

    public enum MatchStatus {
        STATE_INITIALIZATION,
        STATE_FUTURE_MATCH,
        //only at state of pre_registered, the thread is launched
        STATE_PRE_REGISTERED,
        STATE_MATCH_START,
        STATE_MATCH_LOGGING,
        STATE_MATCH_ENDED,
        STATE_INITIALIZATION_FAILURE,
        STATE_ALREADY_REGISTERED,
        STATE_TERMINATED
    }

    public enum MatchStage {
        STAGE_ESST,
        STAGE_FIRST,
        STAGE_HALFTIME,
        STAGE_SECOND,
        STAGE_FULLTIME,
        STAGE_UNDEFINED
    }

    public enum InplayPoolType {
        HAD,
        HIL,
        TQL,
        NTS,
        CHL,
        CRS,
        UNDEFINED
    }

    public enum UpdateDifferentiator {
        UPDATE_STAGE,
        UPDATE_POOLS,
        UPDATE_SCORES,
        UPDATE_CORNER,
        UPDATE_POOL_HAD,
        UPDATE_POOL_HIL,
        UPDATE_POOL_TQL,
        UPDATE_POOL_NTS,
        UPDATE_POOL_CHL,
        UPDATE_POOL_CRS
    }

    public static InplayPoolType GetInplayPoolType(String input) {

        String lowercase = input.toLowerCase();

        if (lowercase.equals("had"))
            return InplayPoolType.HAD;
        else if (lowercase.equals("hil"))
            return InplayPoolType.HIL;
        else if (lowercase.equals("tql"))
            return InplayPoolType.TQL;
        else if (lowercase.equals("nts"))
            return InplayPoolType.NTS;
        else if (lowercase.equals("chl"))
            return InplayPoolType.CHL;
        else if(lowercase.equals("crs"))
            return InplayPoolType.CRS;
        else
            return InplayPoolType.UNDEFINED;
    }

    public static String GetCapPoolType(InplayPoolType type){

        switch (type){
            case HAD:
                return "HAD";
            case HIL:
                return "HIL";
            case TQL:
                return "TQL";
            case NTS:
                return "NTS";
            case CHL:
                return "CHL";
            case CRS:
                return "CRS";
            default:
                return "undefined";
        }
    }

    public static Set<InplayPoolType> GetInplayPoolTypeSet(List<String> pools){
        Set<InplayPoolType> poolLists = EnumSet.noneOf(InplayPoolType.class);

        for(String pool : pools){
            poolLists.add(GetInplayPoolType(pool));
        }

        return poolLists;
    }

    public static MatchStage GetMatchStage(String str){

        String lowercase = str.toLowerCase();

        if(lowercase.equals("inplayesst_nobr"))
            return MatchStage.STAGE_ESST;
        if(lowercase.equals("firsthalf") )
            return MatchStage.STAGE_FIRST;
        if(lowercase.equals("halftimecompleted"))
            return MatchStage.STAGE_HALFTIME;
        if(lowercase.equals("secondhalf"))
            return MatchStage.STAGE_SECOND;
        else {
            logTest.logger.error("MATCHCONSTANTS.GetMatchStage() unknown string: " + str);
            return MatchStage.STAGE_UNDEFINED;
        }
    }

    public static String GetMatchStageStr(MatchStage stage){

        switch (stage) {
            case STAGE_ESST:
                return "inplayesst_nobr";
            case STAGE_FIRST:
                return "firsthalf";
            case STAGE_HALFTIME:
                return "halftimecompleted";
            case STAGE_SECOND:
                return "secondhalf";
            case STAGE_UNDEFINED:
                default:
                    logTest.logger.error("MatchCONSTANTS.GetMatchStageStr() error: " + stage.toString());
                return "unknown";
        }
    }

    public enum MatchPoolStatus {
        STATUS_START_SELL,
        STATUS_SUSPENDED,
        STATUS_CLOSED,
        STATUS_UNDEFINED
    }

    public static MatchPoolStatus GetMatchPoolStatus (String str){
        String lowercase = str.toLowerCase();

        if(lowercase.equals("start-sell"))
            return MatchPoolStatus.STATUS_START_SELL;
        if(lowercase.equals("suspended"))
            return MatchPoolStatus.STATUS_SUSPENDED;
        if(lowercase.equals("bettingclosed"))
            return MatchPoolStatus.STATUS_CLOSED;
        else
            return MatchPoolStatus.STATUS_UNDEFINED;
    }

    public static String GetPoolStatusStr(MatchPoolStatus stat){

        switch (stat){
            case STATUS_START_SELL:
                return "start-sell";
            case STATUS_SUSPENDED:
                return "suspended";
            case STATUS_CLOSED:
                return "bettingclosed";
            default:
                return "undefined";
        }
    }
}
