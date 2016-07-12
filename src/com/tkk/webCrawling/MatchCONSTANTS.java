package com.tkk.webCrawling;

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
        STATE_ALREADY_REGISTERED
    }

    public enum MatchStage {
        STAGE_ESST,
        STAGE_FIRST,
        STAGE_HALFTIME,
        STAGE_SECOND,
    }

    public enum InplayPoolType {
        HAD,
        HIL,
        TQL,
        NTS,
        CHL,
        CRS
    }

    public static InplayPoolType GetInplayPoolType(String input) {

        String lowercase = input.toLowerCase();

        if (lowercase == "had")
            return InplayPoolType.HAD;
        else if (lowercase == "hil")
            return InplayPoolType.HIL;
        else if (lowercase == "tql")
            return InplayPoolType.TQL;
        else if (lowercase == "nts")
            return InplayPoolType.NTS;
        else if (lowercase == "chl")
            return InplayPoolType.CHL;
        else
            return InplayPoolType.CRS;
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
            default:
                return "CRS";
        }
    }
}
