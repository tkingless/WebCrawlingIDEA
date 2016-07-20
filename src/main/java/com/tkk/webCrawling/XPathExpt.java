package com.tkk.webCrawling;

import com.tkk.test.TestCases.MatchCrawleeTestSample;
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
import java.util.List;

//Reference: http://stackoverflow.com/questions/2811001/how-to-read-xml-using-xpath-in-java

/**
 * Created by tkingless on 7/1/16.
 */
public class XPathExpt {

    public static void main(String[] args) throws XPathExpressionException {

        String uri = "http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=104049";

        try {
            //String source = JsoupHelper.GetDocumentFrom(uri).toString();
            String source = JsoupHelper.GetDocumentFromStr(MatchCrawleeTestSample.preReg103904firstHalf).toString();
            System.out.println(source);
            InputStream xml = IOUtils.toInputStream(source, "UTF-8");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xml);

            doc.getDocumentElement().normalize();
            //System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

           XPathFactory xpathFactory2 = XPathFactory.newInstance();
            XPath xpath2 = xpathFactory2.newXPath();
            String ExistBoolonQuery = "//match";
            XPathExpression existExpr2 = xpath2.compile(ExistBoolonQuery);
            Boolean nodeResult2 = (Boolean) existExpr2.evaluate(doc, XPathConstants.BOOLEAN);

            System.out.println("Boolon value:" + nodeResult2);

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            String ExistPoolQuery = "//pool[@type=\"CHI\"]";
            XPathExpression existExpr = xpath.compile(ExistPoolQuery);
            Boolean nodeResult = (Boolean) existExpr.evaluate(doc, XPathConstants.BOOLEAN);

            System.out.println("Pool value:" + nodeResult);

            String HADhomeQuery = "//pool[@type=\"HAD\"]/@h";
            String HADdrawQuery = "//pool[@type=\"HAD\"]/@d";
            String HADawayQuery = "//pool[@type=\"HAD\"]/@a";
            String poolsTypeQuery = "//match/@inplay_pools";
            String CornerTotalQuery = "//match/@ninety_mins_total_corner";
            String CornerLineQuery = "//pool[@type=\"CHL\"]/@line";
            String CornerHighQuery = "//pool[@type=\"CHL\"]/@h";
            String CornerLowQuery = "//pool[@type=\"CHL\"]/@l";

            String HighLowHQuery = "//pool[@type=\"HIL\"]/@h";
            String HighLowLQuery = "//pool[@type=\"HIL\"]/@l";
            String HighLowLineQuery = "//pool[@type=\"HIL\"]/@line";
            String HighLowStatQuery = "//pool[@type=\"HIL\"]/@line_status";
            String HighLowNoQuery = "//pool[@type=\"HIL\"]/@line_no";
            String HighLowMainlineQuery = "//pool[@type=\"HIL\"]/@mainline_flag";
            String NTSHomeQuery = "//pool[@type=\"NTS\"]/@h";
            String NTSAwayQuery = "//pool[@type=\"NTS\"]/@a";
            String HighNoQuery = "//pool[@type=\"NTS\"]/@n";
            //the number of goal to be in
            String HighInQuery = "//pool[@type=\"NTS\"]/@in";


            List<String> queries = Arrays.asList(HADhomeQuery,HADdrawQuery,HADawayQuery,
                    poolsTypeQuery,CornerTotalQuery,CornerLineQuery,CornerHighQuery,CornerLowQuery,
                    HighLowHQuery,HighLowLQuery,HighLowLineQuery,HighLowStatQuery,HighLowNoQuery,
                    HighLowMainlineQuery);

            for (String str : queries){
                System.out.println(GetValueByQuery(str,doc));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    static String GetValueByQuery(String aQuery, Document doc) {
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
}

