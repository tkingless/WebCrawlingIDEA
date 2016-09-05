package com.tkingless.crawlee;

import com.tkingless.MatchCONSTANTS;
import com.tkingless.MatchCONSTANTS.*;
import com.tkingless.utils.logTest;
import com.tkingless.utils.DateTimeEntity;
import com.tkingless.utils.MapComparator;
import com.tkingless.webCrawler.baseCrawler;
import com.tkingless.utils.JsoupHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;

import org.apache.commons.io.*;

import java.io.InputStream;
import java.util.*;

/**
 * Created by tkingless on 6/26/16.
 */

public class MatchCrawlee extends baseCrawlee {

    private String linkAddr;
    private String baseUrl = "http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=";
    private String matchID;

    Set<InplayPoolType> poolType = EnumSet.noneOf(InplayPoolType.class);

    public Set<InplayPoolType> getPoolType() {
        return poolType;
    }

    public MatchCONSTANTS.MatchStage getMatchStage() {
        return matchStage;
    }

    MatchCONSTANTS.MatchStage matchStage;
    private Document doc;
    String strSource;

    private DateTimeEntity recordTime;

    public DateTimeEntity getRecordTime() {
        return recordTime;
    }

    public String getScores() {
        return scores;
    }

    private String scores;

    public String getTotalCorners() {
        return totalCorners;
    }

    private String totalCorners;

    boolean matchXmlValid = false;

    public boolean isMatchXmlValid() {
        return matchXmlValid;
    }

    public MatchCrawlee(baseCrawler crlr, String aMatchID) {
        super(crlr);
        matchID = aMatchID;
        linkAddr = baseUrl + matchID;
    }

    public MatchCrawlee(String src) {
        strSource = src;
    }

    public void run() {
        //logTest.logger.info("MatchCrawlee run() called");

        try {
            String source;

            if (strSource == null) {
                source = JsoupHelper.GetDocumentFrom(linkAddr).toString();
            } else {
                source = JsoupHelper.GetDocumentFromStr(strSource).toString();
            }
            //logTest.logger.info(source);
            InputStream xml = IOUtils.toInputStream(source, "UTF-8");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(xml);

            doc.getDocumentElement().normalize();
            //logTest.logger.info ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

            //List<String> queries = Arrays.asList(CornerTotalQuery,CornerLineQuery,CornerHighQuery,CornerLowQuery);

            final String ExistMatchQuery = "//match";
            if (CheckXMLNodeValid(ExistMatchQuery)) {
                matchXmlValid = true;
                ExtractMatchPools();
                ExtractStage();
                ExtractScores();
                ExtractTotalCorners();
                /*for (String str : queries) {
                    logTest.logger.info(GetValueByQuery(str));
                }*/
                recordTime = new DateTimeEntity();
            } else {
                logTest.logger.info("MatchCrawlee CheckXMLNodeValid() not valid.");
            }

        } catch (Exception e) {
            logTest.logger.error("Match Crawlee error 1: ",e);
        }
    }

    String GetValueByQuery(String aQuery) {
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        try {
            XPathExpression expr = xpath.compile(aQuery);

            Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);

            if(node == null){
                return "";
            }

            return node.getNodeValue();

        } catch (Exception e) {
            logTest.logger.error("Match Crawlee error 2: ",e);
        }

        return "";
    }

    Boolean CheckXMLNodeValid(String aQuery) throws XPathExpressionException {
        boolean valid;

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        XPathExpression existExpr = xpath.compile(aQuery);
        valid = (Boolean) existExpr.evaluate(doc, XPathConstants.BOOLEAN);

        return valid;
    }

    public Boolean isAllPoolClosed() {
        Boolean allPoolClosed = true;

        try {

            if (isMatchXmlValid())
                if (!poolType.isEmpty()) {
                    for (InplayPoolType aType : poolType) {
                        String poolStatQuery = String.format("//pool[@type=\"%s\"]/@match_pool_status", MatchCONSTANTS.GetCapPoolType(aType));
                        String result = GetValueByQuery(poolStatQuery);
                        if(!result.equals("")) {
                            if (!MatchCONSTANTS.GetPoolStatusStr(MatchPoolStatus.STATUS_CLOSED).equals(result)) {
                                allPoolClosed = false;
                                break;
                            }
                        }
                    }
                }
        } catch (Exception e){
            logTest.logger.error("isAllPoolClosed() error",e);
        }

        return allPoolClosed;
    }

    public HashMap<String, String> ExtractPoolTypeBody(InplayPoolType type) throws XPathExpressionException {
        HashMap<String, String> hmap = new HashMap<String, String>();

        String existQuery = String.format("//pool[@type=\"%s\"]", MatchCONSTANTS.GetCapPoolType(type));
        String poolStatQuery = String.format("//pool[@type=\"%s\"]/@match_pool_status", MatchCONSTANTS.GetCapPoolType(type));

        if (CheckXMLNodeValid(existQuery)) {
            hmap.put("Exist", "true");
            hmap.put("PoolStat", GetValueByQuery(poolStatQuery));
            switch (type) {
                case HAD:
                    ExplainHADpool(hmap);
                    break;
                case CHL:
                    ExplainCHIpool(hmap);
                    break;
                default:
                    //logTest.logger.info("[Error] undefined pool type: " + type.toString());
                    hmap.put("Error", "true");
                    break;
            }
        } else {
            hmap.put("Exist", "false");
        }
        return hmap;
    }

    //Different explain functions
    void ExplainHADpool(HashMap<String, String> hmap) {
        final String HADhomeQuery = "//pool[@type=\"HAD\"]/@h";
        final String HADdrawQuery = "//pool[@type=\"HAD\"]/@d";
        final String HADawayQuery = "//pool[@type=\"HAD\"]/@a";

        String homeVal = GetValueByQuery(HADhomeQuery);
        String drawVal = GetValueByQuery(HADdrawQuery);
        String awayVal = GetValueByQuery(HADawayQuery);

        homeVal = StrTrimAtChar(homeVal);
        drawVal = StrTrimAtChar(drawVal);
        awayVal = StrTrimAtChar(awayVal);

        hmap.put("home", homeVal);
        hmap.put("draw", drawVal);
        hmap.put("away", awayVal);
    }

    void ExplainCHIpool(HashMap<String, String> hmap) {
        final String CornerLineQuery = "//pool[@type=\"CHL\"]/@line";
        final String CornerHighQuery = "//pool[@type=\"CHL\"]/@h";
        final String CornerLowQuery = "//pool[@type=\"CHL\"]/@l";

        String lineVal = GetValueByQuery(CornerLineQuery);
        String highVal = GetValueByQuery(CornerHighQuery);
        String lowVal = GetValueByQuery(CornerLowQuery);

        List<String> lines = new ArrayList<>(Arrays.asList(lineVal.split("_")));
        List<String> highs = new ArrayList<>(Arrays.asList(highVal.split("_")));
        List<String> lows = new ArrayList<>(Arrays.asList(lowVal.split("_")));

        highs.forEach(high -> {
            high = StrTrimAtChar(high);
        });

        lows.forEach(low -> {
            low = StrTrimAtChar(low);
        });

        if(lines.size() == highs.size() && highs.size()== lows.size()){
            logTest.logger.info("ExplainCHIpool Cardinality checks right");
            for(int i=0 ; i<lines.size();i++){
                hmap.put("CHLline_"+(i+1),lines.get(i));
                hmap.put("CHLhigh_"+(i+1),highs.get(i));
                hmap.put("CHLlow_"+(i+1),lows.get(i));

            }
        }else {
            logTest.logger.error("ExplainCHIpool Cardinality checks wrong");
        }
    }

    String StrTrimAtChar(String str) {
        if (str.contains("@")) {
            str = str.substring(str.lastIndexOf('@') + 1);
        }
        return str;
    }

    //Helper functions
    private void ExtractMatchPools() {
        String poolsQuery = "//match/@inplay_pools";

        String poolsVal = GetValueByQuery(poolsQuery);

        for (InplayPoolType aType : InplayPoolType.values()) {
            if (poolsVal.contains(aType.toString())) {
                poolType.add(aType);
            }
        }
    }

    private void ExtractStage() {
        final String stageQuery = "//match/@match_stage";

        String stageVal = GetValueByQuery(stageQuery);
        matchStage = MatchCONSTANTS.GetMatchStage(stageVal);
    }

    private void ExtractScores() {
        final String scoreQuery = "//match/@score";
        scores = GetValueByQuery(scoreQuery);
    }

    private void ExtractTotalCorners() {
        final String CornerTotalQuery = "//match/@ninety_mins_total_corner";
        totalCorners = GetValueByQuery(CornerTotalQuery);
    }

    public static Set<UpdateDifferentiator> UpdateDifferentiator (MatchCrawlee oldCrle, MatchCrawlee newCrle){

        Set<UpdateDifferentiator> difftr = EnumSet.noneOf(UpdateDifferentiator.class);

        try {
            if (newCrle == null)
                return difftr;
            if (oldCrle == null) {
                difftr.addAll(EnumSet.of(UpdateDifferentiator.UPDATE_STAGE, UpdateDifferentiator.UPDATE_POOLS, UpdateDifferentiator.UPDATE_SCORES));

                for (InplayPoolType aType : newCrle.getPoolType()) {

                    switch (aType) {
                        case HAD:
                            difftr.add(UpdateDifferentiator.UPDATE_POOL_HAD);
                            break;
                        case CHL:
                            difftr.add(UpdateDifferentiator.UPDATE_CORNER);
                            difftr.add(UpdateDifferentiator.UPDATE_POOL_CHL);
                            break;
                        case NTS:
                            difftr.add(UpdateDifferentiator.UPDATE_POOL_NTS);
                            break;
                        case HIL:
                            difftr.add(UpdateDifferentiator.UPDATE_POOL_HIL);
                            break;
                        case TQL:
                            difftr.add(UpdateDifferentiator.UPDATE_POOL_TQL);
                            break;
                        case CRS:
                            difftr.add(UpdateDifferentiator.UPDATE_POOL_CRS);
                            break;
                        default:
                            logTest.logger.warn("[UpdateDifferentiator] this type " + aType.toString() + " is not catched yet");
                            break;
                    }
                }
                return difftr;
            }

            if (newCrle.getRecordTime().GetTheInstant().getTime() <= oldCrle.getRecordTime().GetTheInstant().getTime())
                return difftr;

            if (oldCrle.getMatchStage() != newCrle.getMatchStage())
                difftr.add(UpdateDifferentiator.UPDATE_STAGE);

            if (!oldCrle.getPoolType().equals(newCrle.getPoolType()))
                difftr.add(UpdateDifferentiator.UPDATE_POOLS);

            if (!oldCrle.getScores().equals(newCrle.getScores()))
                difftr.add(UpdateDifferentiator.UPDATE_SCORES);

            if (!oldCrle.getTotalCorners().equals(newCrle.getTotalCorners()))
                difftr.add(UpdateDifferentiator.UPDATE_CORNER);

            for (InplayPoolType aType : oldCrle.getPoolType()) {

                if (MapComparator.CompareMapsDifferent(oldCrle.ExtractPoolTypeBody(aType)
                        , newCrle.ExtractPoolTypeBody(aType))) {
                    logTest.logger.info("oldCrle " + aType + " body: " + oldCrle.ExtractPoolTypeBody(aType));
                    logTest.logger.info("newCrle " + aType + " body: " + newCrle.ExtractPoolTypeBody(aType));
                    //kludge
                    switch (aType) {
                        case HAD:
                            difftr.add(UpdateDifferentiator.UPDATE_POOL_HAD);
                            break;
                        case CHL:
                            difftr.add(UpdateDifferentiator.UPDATE_POOL_CHL);
                            break;
                        case NTS:
                            difftr.add(UpdateDifferentiator.UPDATE_POOL_NTS);
                            break;
                        case HIL:
                            difftr.add(UpdateDifferentiator.UPDATE_POOL_HIL);
                            break;
                        case TQL:
                            difftr.add(UpdateDifferentiator.UPDATE_POOL_TQL);
                            break;
                        case CRS:
                            difftr.add(UpdateDifferentiator.UPDATE_POOL_CRS);
                            break;
                        default:
                            logTest.logger.warn("[UpdateDifferentiator] this type " + aType.toString() + " is not catched yet");
                            break;
                    }
                }

            }
        } catch (Exception e){
            logTest.logger.error("UpdateDifferentiator error: ",e);
        }

        return difftr;
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();

        try {

            tmp.append("\n");
            tmp.append("stage: ").append(matchStage).append("\n");
            tmp.append("score: ").append(scores).append("\n");

            for (InplayPoolType aType : this.getPoolType()) {
                try {
                    tmp.append("Type, ").append(aType.toString()).append(": ");
                    tmp.append(ExtractPoolTypeBody(aType).toString());
                    tmp.append("\n");
                } catch (Exception e) {
                    logTest.logger.error("Match Crawlee to String error 3: ", e);
                }
            }
        } catch (Exception e){
            logTest.logger.error("To String error: ",e);
        }

        return tmp.toString();
    }
}
