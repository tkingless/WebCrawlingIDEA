package com.tkingless;

import org.bson.Document;

import javax.print.Doc;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tkingless on 8/13/16.
 */

public class WCDIOcsvData {

    Date recorded;
    Integer matchId;
    String stage;
    Integer homeScore;
    Integer awayScore;
    Integer corner;
    Double HADhomeOdd;
    Double HADdrawOdd;
    Double HADawayOdd;
    String HADpoolStatus;
    String CHLline;
    Double CHLhigh;
    Double CHLlow;
    String CHLpoolStatus;

    public Date getRecorded() {
        return recorded;
    }

    public void setRecorded(Date recorded) {
        this.recorded = recorded;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    public Integer getCorner() {
        return corner;
    }

    public void setCorner(Integer corner) {
        this.corner = corner;
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

    public String getHADpoolStatus() {
        return HADpoolStatus;
    }

    public void setHADpoolStatus(String HADpoolStatus) {
        this.HADpoolStatus = HADpoolStatus;
    }

    public String getCHLline() {
        return CHLline;
    }

    public void setCHLline(String CHLline) {
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

    public String getCHLpoolStatus() {
        return CHLpoolStatus;
    }

    public void setCHLpoolStatus(String CHLpoolStatus) {
        this.CHLpoolStatus = CHLpoolStatus;
    }

    public Document ToBson (){
        Document bson = new Document();

        return bson;
    }

    public static void InitializeRecordHead(List<DateDocumentObj> updateHistory, WCDIOcsvData head){
        try{
            Iterator<DateDocumentObj> cursor = updateHistory.iterator();

            head.setMatchId(updateHistory.get(0).getDoc().getInteger("MatchId"));

            while (cursor.hasNext()){
                Document doc = cursor.next().getDoc();
                String type = doc.getString("type");

                if( type.equals("HAD")){
                    head.setHADawayOdd(doc.getDouble("HADawayOdd"));
                    head.setHADdrawOdd(doc.getDouble("HADdrawOdd"));
                    head.setHADhomeOdd(doc.getDouble("HADhomeOdd"));
                    head.setHADpoolStatus(doc.getString("poolStatus"));
                }else if(type.equals("CHL")){

                }else if(type.equals("stage")){

                }else if(type.equals("score")){

                }else if(type.equals("corner")){

                }

                head.setRecorded(doc.getDate("recorded"));
            }


        } catch (Exception e){
            WebCrawledDataIO.logger.error("WCDIOcsvData error",e);
        }
    }
}
