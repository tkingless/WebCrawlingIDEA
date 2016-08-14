package com.tkingless.WebCrawling.DBobject;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;
import java.util.List;

import static org.mongodb.morphia.utils.IndexType.DESC;

/**
 * Created by tkingless on 7/29/16.
 */

//TODO: the MatchID index unique property is not working, test on Mongo DB

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
    @Reference("stageUpdates")
    private List<InPlayAttrUpdates> stageUpdates;
    @Reference("scoreUpdates")
    private List<InPlayAttrUpdates> scoreUpdates;
    @Reference("cornerTotUpdates")
    private List<InPlayAttrUpdates> cornerTotUpdates;


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

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setCommence(Date commence) {
        this.commence = commence;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public void setScoreUpdates(List<InPlayAttrUpdates> scoreUpdates) {
        this.scoreUpdates = scoreUpdates;
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

    public Date getEndTime() {
        return endTime;
    }

    public List<InPlayAttrUpdates> getStageUpdates() {
        return stageUpdates;
    }

    public List<InPlayAttrUpdates> getScoreUpdates() {
        return scoreUpdates;
    }

    public List<InPlayAttrUpdates> getCornerTotUpdates() {
        return cornerTotUpdates;
    }

    public boolean equals(MatchEventData data){
        boolean eq = false;

        if(MatchId.equals(data.getMatchId()))
            if(MatchKey.equals(data.getMatchKey()))
                if(homeTeam.equals(data.getHomeTeam()))
                    if(awayTeam.equals(data.getAwayTeam())) {
                        //if(poolTypes.equals(data.getPoolTypes()))
                        if (commence.equals(data.getCommence())) {
                            eq = true;
                        }
                    }

        return eq;
    }

}
