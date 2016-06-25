package com.tkk.webCrawling.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by tkingless on 24/6/2016.
 */

//This class only utility.
public class JsoupHelper {

    static int timeout = 6000;

    static public Document GetDocumentFrom(String url) throws IOException {
        return Jsoup.connect(url).data("query", "Java").userAgent("Mozilla")
                .cookie("auth", "token").timeout(timeout).post();
    }
}
