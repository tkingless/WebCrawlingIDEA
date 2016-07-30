package com.tkk.crawlee;

import com.tkk.MatchCONSTANTS;
import com.tkk.MatchCONSTANTS.*;
import com.tkk.utils.logTest;
import com.tkk.utils.DateTimeEntity;
import com.tkk.utils.MapComparator;
import com.tkk.webCrawler.baseCrawler;
import com.tkk.utils.JsoupHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;

import org.apache.commons.io.*;

import java.io.InputStream;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

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

    boolean matchXmlValid = false;

    public boolean isMatchXmlValid() {
        return matchXmlValid;
    }

    public MatchCrawlee(baseCrawler crlr, String aMatchID) {
        super(crlr);
        //TODO
        matchID = aMatchID;
        linkAddr = baseUrl + matchID;
    }

    public MatchCrawlee(String src) {
        strSource = src;
    }

    public void run() {
        logTest.logger.info("MatchCrawlee run() called");

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
                /*for (String str : queries) {
                    logTest.logger.info(GetValueByQuery(str));
                }*/
                recordTime = new DateTimeEntity();
            } else {
                logTest.logger.info("MatchCrawlee CheckXMLNodeValid() not valid.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    String GetValueByQuery(String aQuery) {
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        try {
            XPathExpression expr = xpath.compile(aQuery);

            Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);

            return node.getNodeValue();

        } catch (XPathExpressionException e) {
            e.printStackTrace();
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

        if (isMatchXmlValid())
            if (!poolType.isEmpty()) {
                for (InplayPoolType aType : poolType) {
                    String poolStatQuery = String.format("//pool[@type=\"%s\"]/@match_pool_status", MatchCONSTANTS.GetCapPoolType(aType));
                    if (!MatchCONSTANTS.GetPoolStatusStr(MatchPoolStatus.STATUS_CLOSED).equals(GetValueByQuery(poolStatQuery)))
                        allPoolClosed = false;
                    break;
                }
            }

        return allPoolClosed;
    }

    HashMap<String, String> ExtractPoolTypeBody(InplayPoolType type) throws XPathExpressionException {
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
        final String CornerTotalQuery = "//match/@ninety_mins_total_corner";
        final String CornerLineQuery = "//pool[@type=\"CHL\"]/@line";
        final String CornerHighQuery = "//pool[@type=\"CHL\"]/@h";
        final String CornerLowQuery = "//pool[@type=\"CHL\"]/@l";

        String totalVal = GetValueByQuery(CornerTotalQuery);
        String lineVal = GetValueByQuery(CornerLineQuery);
        String highVal = GetValueByQuery(CornerHighQuery);
        String lowVal = GetValueByQuery(CornerLowQuery);

        highVal = StrTrimAtChar(highVal);
        lowVal = StrTrimAtChar(lowVal);

        hmap.put("total", totalVal);
        hmap.put("line", lineVal);
        hmap.put("high", highVal);
        hmap.put("low", lowVal);
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

    public static boolean HasUpdate(MatchCrawlee oldCrle, MatchCrawlee newCrle) throws XPathExpressionException {
        boolean toUpdate = true;

        if (newCrle == null)
            return false;
        if (oldCrle == null)
            return true;

        if (newCrle.getRecordTime().GetTheInstant().getTime() <= oldCrle.getRecordTime().GetTheInstant().getTime())
            return false;

        if (oldCrle.getMatchStage() == newCrle.getMatchStage())
            if (oldCrle.getPoolType().equals(newCrle.getPoolType()))
                if (oldCrle.getScores().equals(newCrle.getScores())) {
                    toUpdate = false;
                    for (InplayPoolType aType : oldCrle.getPoolType()) {
                        if (MapComparator.CompareMapsDifferent(oldCrle.ExtractPoolTypeBody(aType)
                                , newCrle.ExtractPoolTypeBody(aType))) {
                            logTest.logger.info("oldCrle body: " + oldCrle.ExtractPoolTypeBody(aType));
                            logTest.logger.info("newCrle body: " + newCrle.ExtractPoolTypeBody(aType));
                            toUpdate = true;
                            break;
                        }
                    }
                }


        return toUpdate;
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();

        tmp.append("stage: ").append(matchStage).append("\n");
        tmp.append("score: ").append(scores).append("\n");

        for (InplayPoolType aType : this.getPoolType()) {
            try {
                tmp.append("Type, ").append(aType.toString()).append(": ");
                tmp.append(ExtractPoolTypeBody(aType).toString());
                tmp.append("\n");
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }

        return tmp.toString();
    }
}
