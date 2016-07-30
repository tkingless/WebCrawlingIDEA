package com.tkk.WebCrawling.DBobject;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.mongodb.morphia.utils.IndexType.DESC;

/**
 * Created by tkingless on 7/29/16.
 */

@Entity("MatchEvents")
@Indexes({
        @Index(value = "MatchId", fields = @Field(value = "MatchdId", type = DESC), options = @IndexOptions(unique = true) ),
        @Index(value = "createdAt", fields = @Field(value = "createdAt", type = DESC))
})

public class MatchEventData {

    @Id
    private ObjectId id;
    private Integer MatchId;

    private String MatchKey;
    private HashMap<Date,String> stageUpdates;
    private HashMap<Date,String> scoreUpdate;
    private HashMap<Date,Integer> cornerTotUpdate;

    private String homeTeam;
    private String awayTeam;
    private List<String> poolTypes;

    private Date commence;
    private Date actualCommence;
    private Date endTime;
    private Date createdAt;
    private Date lastModifiedAt;

    public void setMatchId(Integer matchId) {
        MatchId = matchId;
    }

    public void setMatchKey(String matchKey) {
        MatchKey = matchKey;
    }

    public void setStageUpdates(HashMap<Date, String> stageUpdates) {
        this.stageUpdates = stageUpdates;
    }

    public void setScoreUpdate(HashMap<Date, String> scoreUpdate) {
        this.scoreUpdate = scoreUpdate;
    }

    public void setCornerTotUpdate(HashMap<Date, Integer> cornerTotUpdate) {
        this.cornerTotUpdate = cornerTotUpdate;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setPoolTypes(List<String> poolTypes) {
        this.poolTypes = poolTypes;
    }

    public void setCommence(Date commence) {
        this.commence = commence;
    }

    public void setActualCommence(Date actualCommence) {
        this.actualCommence = actualCommence;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public Integer getMatchId() {
        return MatchId;
    }

    public String getMatchKey() {
        return MatchKey;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public List<String> getPoolTypes() {
        return poolTypes;
    }

    public Date getCommence() {
        return commence;
    }

    public boolean equals(MatchEventData data){
        boolean eq = false;

        if(MatchId.equals(data.getMatchId()))
            if(MatchKey.equals(data.getMatchKey()))
                if(homeTeam.equals(data.getHomeTeam()))
                    if(awayTeam.equals(data.getAwayTeam()))
                        if(poolTypes.equals(data.getPoolTypes()))
                            if(commence.equals(data.getCommence())){
                                eq = true;
                            }

        return eq;
    }

}
