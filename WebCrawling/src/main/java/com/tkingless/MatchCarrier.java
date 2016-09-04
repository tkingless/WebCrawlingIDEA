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
    String matchStatusTime;
        Date commenceTime;

    String league;

    public MatchCarrier(Integer matchId, String matchNo, String homeTeam, String awayTeam, String matchStatusText, String matchInitTime, String league) {
        MatchId = matchId;
        MatchNo = matchNo;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchStatusText = matchStatusText;
        this.matchStatusTime = matchInitTime;
        this.league = league;
    }

    public Integer getMatchId() {
        return MatchId;
    }

    public String getMatchNo() {
        return MatchNo;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getMatchStatusText() {
        return matchStatusText;
    }

    public String getMatchStatusTime() {
        return matchStatusTime;
    }

    public MatchCONSTANTS.MatchStage getStage() {
        return stage;
    }

    public Date getCommenceTime() {
        return commenceTime;
    }

    public String getLeague() {
        return league;
    }
}
