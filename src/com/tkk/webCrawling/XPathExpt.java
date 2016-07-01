package com.tkk.webCrawling;

import com.tkk.webCrawling.utils.JsoupHelper;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import org.apache.commons.io.*;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//Reference: http://stackoverflow.com/questions/2811001/how-to-read-xml-using-xpath-in-java

/**
 * Created by tkingless on 7/1/16.
 */
public class XPathExpt {

    public static void main(String[] args) {

        String uri = "http://bet.hkjc.com/football/getXML.aspx?pooltype=all&isLiveBetting=true&match=103867";

       /* URL url = new URL(uri);
        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml");

        InputStream xml = connection.getInputStream();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xml);

        System.out.println(doc.toString());*/

        try {
            String source = JsoupHelper.GetDocumentFrom(uri).toString();
            //System.out.println(source);
            InputStream xml = IOUtils.toInputStream(source,"UTF-8");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xml);

            doc.getDocumentElement().normalize();
            System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            XPathExpression expr = xpath.compile("//pool[@type=\"HAD\"]/@h");

            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++){
                System.out.println(nodes.item(i).getNodeValue());
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
}
