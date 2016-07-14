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
        STAGE_UNDEFINED
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

    public static MatchStage GetMatchStage(String str){

        String lowercase = str.toLowerCase();

        System.out.println("lowercase: " + lowercase);

        if(lowercase.equals("inplayesst_nobr"))
            return MatchStage.STAGE_ESST;
        if(lowercase.equals("firsthalf") )
            return MatchStage.STAGE_FIRST;
        if(lowercase.equals("halftimecompleted"))
            return MatchStage.STAGE_HALFTIME;
        if(lowercase.equals("secondhalf"))
            return MatchStage.STAGE_SECOND;
        else
            return MatchStage.STAGE_UNDEFINED;
    }
}
