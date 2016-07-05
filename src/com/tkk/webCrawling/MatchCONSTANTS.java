package com.tkk.webCrawling;

/**
 * Created by tsangkk on 7/5/16.
 */
public class MatchCONSTANTS {

    public enum MatchState {
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
        NTS,
        CHL,
        CRS
    }
}
