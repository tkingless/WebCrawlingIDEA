package com.tkingless;

import java.util.Date;

/**
 * Created by tkingkwun on 4/9/2016.
 */
public class MatchCarrier {

    Integer MatchId;
    String MatchNo;
    String homeTeam;
    String awayTeam;
    String matchStatusText;
        MatchCONSTANTS.MatchStage stage;
        Date commenceTime;

    String league;

    public MatchCarrier(Integer matchId, String matchNo, String homeTeam, String awayTeam, String matchStatusText, String league) {
        MatchId = matchId;
        MatchNo = matchNo;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchStatusText = matchStatusText;
        this.league = league;
    }
}
