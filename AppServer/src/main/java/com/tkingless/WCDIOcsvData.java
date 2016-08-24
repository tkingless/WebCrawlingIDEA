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
    String corner;
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

    public String getCorner() {
        return corner;
    }

    public void setCorner(String corner) {
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

    public Document ToBson() {
        Document bson = new Document();

        try {
            if (recorded != null) {
                bson.append("recorded", recorded);
            }
            if (matchId != null) {
                bson.append("MatchId", matchId);
            }
            if (stage != null) {
                bson.append("stage", stage);
            }
            if (homeScore != null) {
                bson.append("homeTeamScore", homeScore);
            }
            if (awayScore != null) {
                bson.append("awayTeamScore", awayScore);
            }
            if (corner != null) {
                bson.append("cornerCount", corner);
            }
            if (HADhomeOdd != null) {
                bson.append("HADhomeOdd", HADhomeOdd);
            }
            if (HADdrawOdd != null) {
                bson.append("HADdrawOdd", HADdrawOdd);
            }
            if (HADawayOdd != null) {
                bson.append("HADawayOdd", HADawayOdd);
            }
            if (HADpoolStatus != null) {
                bson.append("HADpoolStatus", HADpoolStatus);
            }
            if (CHLline != null) {
                bson.append("CHLline", CHLline);
            }
            if (CHLhigh != null) {
                bson.append("CHLhigh", CHLhigh);
            }
            if (CHLlow != null) {
                bson.append("CHLlow", CHLlow);
            }
            if (CHLpoolStatus != null) {
                bson.append("CHLpoolStatus", CHLpoolStatus);
            }
        } catch (Exception e) {
            WebCrawledDataIO.logger.error("to bson error", e);
        }

        return bson;
    }

    public static void InitializeRecordHead(List<DateDocumentObj> updateHistory, WCDIOcsvData head) {
        try {
            Iterator<DateDocumentObj> cursor = updateHistory.iterator();

            head.setMatchId(updateHistory.get(0).getDoc().getInteger("MatchId"));

            while (cursor.hasNext()) {
                Document doc = cursor.next().getDoc();
                SetHeadByType(doc, head);

            }

            WebCrawledDataIO.logger.debug("InitailizeRecordHead, head: " + head.toString());
        } catch (Exception e) {
            WebCrawledDataIO.logger.error("WCDIOcsvData error", e);
        }
    }

    public static void SetHeadByType(Document doc, WCDIOcsvData head) {
        String type = doc.getString("type");

        if (type.equals("HAD")) {
            head.setHADawayOdd(doc.getDouble("HADawayOdd"));
            head.setHADdrawOdd(doc.getDouble("HADdrawOdd"));
            head.setHADhomeOdd(doc.getDouble("HADhomeOdd"));
            head.setHADpoolStatus(doc.getString("poolStatus"));
        } else if (type.equals("CHL")) {
            head.setCHLline(doc.getString("CHLline"));
            head.setCHLhigh(doc.getDouble("CHLhigh"));
            head.setCHLlow(doc.getDouble("CHLlow"));
            head.setCHLpoolStatus(doc.getString("poolStatus"));
        } else if (type.equals("stage")) {
            head.setStage(doc.getString("val"));
        } else if (type.equals("score")) {
            String scores = doc.getString("val");
            String[] splitScores = scores.split(" : ");

                    /*for(String str : splitScores) {
                        WebCrawledDataIO.logger.trace("split score str: " + str);
                    }*/
            head.setHomeScore(Integer.parseInt(splitScores[0]));
            head.setAwayScore(Integer.parseInt(splitScores[1]));

        } else if (type.equals("corner")) {
            head.setCorner(doc.getString("val"));
        }
        head.setRecorded(doc.getDate("recorded"));
    }

    public static void ParseInFromDocument(WCDIOcsvData data, Document doc) {

        if (data == null)
            data = new WCDIOcsvData();

        try {
            if (doc.containsKey("recorded")) {
                data.setRecorded(doc.getDate("recorded"));
            }
            if (doc.containsKey("MatchId")) {
                data.setMatchId(doc.getInteger("MatchId"));
            }
            if (doc.containsKey("stage")) {
                data.setStage(doc.getString("stage"));
            }
            if (doc.containsKey("homeTeamScore")) {
                data.setHomeScore(doc.getInteger("homeTeamScore"));
            }
            if (doc.containsKey("awayTeamScore")) {
                data.setAwayScore(doc.getInteger("awayTeamScore"));
            }
            if (doc.containsKey("cornerCount")) {
                data.setCorner(doc.getString("cornerCount"));
            }
            if (doc.containsKey("HADhomeOdd")) {
                data.setHADhomeOdd(doc.getDouble("HADhomeOdd"));
            }
            if (doc.containsKey("HADdrawOdd")) {
                data.setHADdrawOdd(doc.getDouble("HADdrawOdd"));
            }
            if (doc.containsKey("HADawayOdd")) {
                data.setHADdrawOdd(doc.getDouble("HADawayOdd"));
            }
            if (doc.containsKey("HADpoolStatus")) {
                data.setHADpoolStatus(doc.getString("HADpoolStatus"));
            }
            if (doc.containsKey("CHLline")) {
                data.setCHLline(doc.getString("CHLline"));
            }
            if (doc.containsKey("CHLhigh")) {
                data.setCHLhigh(doc.getDouble("CHLhigh"));
            }
            if (doc.containsKey("CHLlow")) {
                data.setCHLlow(doc.getDouble("CHLlow"));
            }
            if (doc.containsKey("CHLpoolStatus")) {
                data.setCHLpoolStatus(doc.getString("CHLpoolStatus"));
            }
        } catch (Exception e) {
            WebCrawledDataIO.logger.error("ParseInFromDocument error", e);
        }


    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();

        try {

            tmp.append("\n");
            if (recorded != null) {
                tmp.append("recorded: " + recorded);
                tmp.append("\n");
            }
            if (matchId != null) {
                tmp.append("MatchId: " + matchId);
                tmp.append("\n");
            }
            if (stage != null) {
                tmp.append("stage: " + stage);
                tmp.append("\n");
            }
            if (homeScore != null) {
                tmp.append("homeTeamScore: " + homeScore);
                tmp.append("\n");
            }
            if (awayScore != null) {
                tmp.append("awayTeamScore: " + awayScore);
                tmp.append("\n");
            }
            if (corner != null) {
                tmp.append("cornerCount " + corner);
                tmp.append("\n");
            }
            if (HADhomeOdd != null) {
                tmp.append("HADhomeOdd " + HADhomeOdd);
                tmp.append("\n");
            }
            if (HADdrawOdd != null) {
                tmp.append("HADdrawOdd " + HADdrawOdd);
                tmp.append("\n");
            }
            if (HADawayOdd != null) {
                tmp.append("HADawayOdd " + HADawayOdd);
                tmp.append("\n");
            }
            if (HADpoolStatus != null) {
                tmp.append("HADpoolStatus " + HADpoolStatus);
                tmp.append("\n");
            }
            if (CHLline != null) {
                tmp.append("CHLline " + CHLline);
                tmp.append("\n");
            }
            if (CHLhigh != null) {
                tmp.append("CHLhigh " + CHLhigh);
                tmp.append("\n");
            }
            if (CHLlow != null) {
                tmp.append("CHLlow " + CHLlow);
                tmp.append("\n");
            }
            if (CHLpoolStatus != null) {
                tmp.append("CHLpoolStatus " + CHLpoolStatus);
                tmp.append("\n");
            }

        } catch (Exception e){
            WebCrawledDataIO.logger.error("To String error: ",e);
        }

        return tmp.toString();
    }
}
