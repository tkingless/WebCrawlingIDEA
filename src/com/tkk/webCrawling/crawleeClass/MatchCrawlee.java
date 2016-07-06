package com.tkk.webCrawling.crawleeClass;

import com.tkk.webCrawling.MatchCONSTANTS;
import com.tkk.webCrawling.utils.DateTimeEntity;
import com.tkk.webCrawling.webCrawler.baseCrawler;

import com.tkk.webCrawling.utils.JsoupHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;

import org.apache.commons.io.*;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by tkingless on 6/26/16.
 */

//TODO not to use concurrencyMachine since there is no mass request to be sent
public class MatchCrawlee extends baseCrawlee{

    final static String HADhomeQuery = "//pool[@type=\"HAD\"]/@h";
    final static String HADdrawQuery = "//pool[@type=\"HAD\"]/@d";
    final static String HADawayQuery = "//pool[@type=\"HAD\"]/@a";
    final static String CornerTotalQuery = "//match/@ninety_mins_total_corner";
    final static String CornerLineQuery = "//pool[@type=\"CHL\"]/@line";
    final static String CornerHighQuery = "//pool[@type=\"CHL\"]/@h";
    final static String CornerLowQuery = "//pool[@type=\"CHL\"]/@l";
    final static String ExistMatchQuery = "//match";

    private String linkAddr;
    private String baseUrl = "http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=";
    private String matchID;
    Set<MatchCONSTANTS.InplayPoolType> poolType;

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
            Document doc = db.parse(xml);

            doc.getDocumentElement().normalize();
            //System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

            List<String> queries = Arrays.asList(HADhomeQuery,HADdrawQuery,HADawayQuery,
                    CornerTotalQuery,CornerLineQuery,CornerHighQuery,CornerLowQuery);

            if(CheckMatchXMLValid(ExistMatchQuery,doc)) {
                for (String str : queries) {
                    System.out.println(GetValueByQuery(str, doc));
                }
            } else {
                System.out.println("MatchCrawlee CheckMatchXMLValid() not valid.");
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
    (2) there is possibility no corner high low at all
    (3)
     */

    String GetValueByQuery(String aQuery, Document doc) {
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

    Boolean CheckMatchXMLValid(String aQuery, Document doc) throws XPathExpressionException {
        boolean valid;

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        XPathExpression existExpr = xpath.compile(aQuery);
        valid = (Boolean) existExpr.evaluate(doc, XPathConstants.BOOLEAN);

        return valid;
    }
}
