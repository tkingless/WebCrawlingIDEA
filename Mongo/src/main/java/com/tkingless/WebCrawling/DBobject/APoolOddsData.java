package com.tkingless.WebCrawling.DBobject;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;

import static org.mongodb.morphia.utils.IndexType.DESC;

/**
 * Created by tsangkk on 6/27/16.
 */

@Entity("InPlayOddsUpdates")
@Indexes({
        @Index(value = "MatchId", fields = @Field(value = "MatchId", type = DESC)),
        @Index(value = "recorded", fields = @Field(value = "recorded", type = DESC))
})

public class APoolOddsData {

    @Id
    private ObjectId id;
    @Property("MatchId")
    private Integer MatchId;
    private String type;

    private Double HADhomeOdd;
    private Double HADdrawOdd;
    private Double HADawayOdd;
    private String CHLline_1;
    private Double CHLhigh_1;
    private Double CHLlow_1;
    private String CHLline_2;
    private Double CHLhigh_2;
    private Double CHLlow_2;

    private String poolStatus;

    @Property("recorded")
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

    public String getCHLline_1() {
        return CHLline_1;
    }

    public void setCHLline_1(String CHLline_1) {
        this.CHLline_1 = CHLline_1;
    }

    public Double getCHLhigh_1() {
        return CHLhigh_1;
    }

    public void setCHLhigh_1(Double CHLhigh_1) {
        this.CHLhigh_1 = CHLhigh_1;
    }

    public Double getCHLlow_1() {
        return CHLlow_1;
    }

    public void setCHLlow_1(Double CHLlow_1) {
        this.CHLlow_1 = CHLlow_1;
    }

    public String getCHLline_2() {
        return CHLline_2;
    }

    public void setCHLline_2(String CHLline_2) {
        this.CHLline_2 = CHLline_2;
    }

    public Double getCHLhigh_2() {
        return CHLhigh_2;
    }

    public void setCHLhigh_2(Double CHLhigh_2) {
        this.CHLhigh_2 = CHLhigh_2;
    }

    public Double getCHLlow_2() {
        return CHLlow_2;
    }

    public void setCHLlow_2(Double CHLlow_2) {
        this.CHLlow_2 = CHLlow_2;
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
