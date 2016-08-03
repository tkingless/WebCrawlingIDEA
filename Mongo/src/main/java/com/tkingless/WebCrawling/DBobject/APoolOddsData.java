package com.tkingless.WebCrawling.DBobject;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;

import static org.mongodb.morphia.utils.IndexType.DESC;

/**
 * Created by tsangkk on 6/27/16.
 */

@Entity("AllOdds")
@Indexes({
        @Index(value = "MatchId", fields = @Field(value = "MatchdId", type = DESC)),
        @Index(value = "recorded", fields = @Field(value = "recorded", type = DESC))
})

public class APoolOddsData {

    @Id
    private ObjectId id;
    private Integer MatchId;
    private String type;

    private Double HADhomeOdd;
    private Double HADdrawOdd;
    private Double HADawayOdd;
    private Double CHLline;
    private Double CHLhigh;
    private Double CHLlow;

    private String poolStatus;

    private Date recorded;

    //cannot define constructor due to use of Morphia pattern while defining this POJO

    public Integer getMatchId() {
        return MatchId;
    }

    public void setMatchId(int matchId) {
        MatchId = matchId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getHADhomeOdd() {
        return HADhomeOdd;
    }

    public void setHADhomeOdd(Double HADhomeOdd) {
        this.HADhomeOdd = HADhomeOdd;
    }

    public Double getHADdrawOdd() {
        return HADdrawOdd;
    }

    public void setHADdrawOdd(Double HADdrawOdd) {
        this.HADdrawOdd = HADdrawOdd;
    }

    public Double getHADawayOdd() {
        return HADawayOdd;
    }

    public void setHADawayOdd(Double HADawayOdd) {
        this.HADawayOdd = HADawayOdd;
    }

    public Double getCHLline() {
        return CHLline;
    }

    public void setCHLline(Double CHLline) {
        this.CHLline = CHLline;
    }

    public Double getCHLhigh() {
        return CHLhigh;
    }

    public void setCHLhigh(Double CHLhigh) {
        this.CHLhigh = CHLhigh;
    }

    public Double getCHLlow() {
        return CHLlow;
    }

    public void setCHLlow(Double CHLlow) {
        this.CHLlow = CHLlow;
    }

    public String getPoolStatus() {
        return poolStatus;
    }

    public void setPoolStatus(String poolStatus) {
        this.poolStatus = poolStatus;
    }

    public Date getRecorded() {
        return recorded;
    }

    public void setRecorded(Date recorded) {
        this.recorded = recorded;
    }
}
