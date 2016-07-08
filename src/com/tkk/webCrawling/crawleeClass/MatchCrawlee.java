package com.tkk.webCrawling.crawleeClass;

import com.tkk.webCrawling.MatchCONSTANTS;
import com.tkk.webCrawling.MatchCONSTANTS.*;
import com.tkk.webCrawling.utils.DateTimeEntity;
import com.tkk.webCrawling.webCrawler.baseCrawler;
import com.tkk.webCrawling.utils.JsoupHelper;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by tkingless on 6/26/16.
 */

//TODO not to use concurrencyMachine since there is no mass request to be sent
public class MatchCrawlee extends baseCrawlee{

    private String linkAddr;
    private String baseUrl = "http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=";
    private String matchID;
    Set<InplayPoolType> poolType;
    private Document doc;

    DateTimeEntity recordTime;

    public MatchCrawlee(baseCrawler crlr,String aMatchID){
        super(crlr);
        //TODO
        matchID = aMatchID;
        linkAddr = baseUrl + matchID;
    }

    public void run() {
        System.out.println("MatchCrawlee run() called");

        try {
            String source = JsoupHelper.GetDocumentFrom(linkAddr).toString();
            //System.out.println(source);
            InputStream xml = IOUtils.toInputStream(source, "UTF-8");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(xml);

            doc.getDocumentElement().normalize();
            //System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

            //List<String> queries = Arrays.asList(CornerTotalQuery,CornerLineQuery,CornerHighQuery,CornerLowQuery);

            final String ExistMatchQuery = "//match";
            if(CheckXMLNodeValid(ExistMatchQuery)) {
                matchXmlValid = true;
                /*for (String str : queries) {
                    System.out.println(GetValueByQuery(str));
                }*/
            } else {
                System.out.println("MatchCrawlee CheckXMLNodeValid() not valid.");
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

    /*Considerations
    http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=103913
     */

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

    boolean matchXmlValid = false;

    public boolean isMatchXmlValid() {
        return matchXmlValid;
    }

    Boolean CheckXMLNodeValid(String aQuery) throws XPathExpressionException {
        boolean valid;

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        XPathExpression existExpr = xpath.compile(aQuery);
        valid = (Boolean) existExpr.evaluate(doc, XPathConstants.BOOLEAN);

        return valid;
    }

    HashMap<String,String> ExtractPoolTypeBody (InplayPoolType type) throws XPathExpressionException {
        HashMap<String,String> hmap = new HashMap<String, String>();

        String existQuery = String.format("//pool[@type=\"%s\"]", MatchCONSTANTS.GetCapPoolType(type));

        System.out.println("Checking existQuery: " + existQuery);

        if(CheckXMLNodeValid(existQuery)){
            hmap.put("Exist","true");
        } else {
            hmap.put("Exist","false");
        }
        return hmap;
    }

    //Different explain functions
    void ExplainHADpool(HashMap<String,String> hmap){
        final String HADhomeQuery = "//pool[@type=\"HAD\"]/@h";
        final String HADdrawQuery = "//pool[@type=\"HAD\"]/@d";
        final String HADawayQuery = "//pool[@type=\"HAD\"]/@a";

        String homeVal = GetValueByQuery(HADhomeQuery);
        String drawVal = GetValueByQuery(HADdrawQuery);
        String awayVal = GetValueByQuery(HADawayQuery);

        homeVal = StrTrimAtChar(homeVal);
        drawVal = StrTrimAtChar(drawVal);
        awayVal = StrTrimAtChar(awayVal);

        hmap.put("home",homeVal);
        hmap.put("draw",drawVal);
        hmap.put("away",awayVal);
    }

    void ExplainCHIpool(HashMap<String,String> hmap){
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
        hmap.put("high",highVal);
        hmap.put("low",lowVal);
    }

    String StrTrimAtChar (String str){
        if(str.contains("@")){
            str = str.substring(str.lastIndexOf('@')+1);
        }
        return str;
    }
}
