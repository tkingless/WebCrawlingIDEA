package com.tkk.webCrawling.crawlee;

import com.tkk.webCrawling.webCrawler.baseCrawler;
import org.jsoup.nodes.Document;

import java.util.concurrent.Callable;

public class baseCrawlee implements Callable<Document> {

    enum State {
        SUCCESS, FAILURE, QUEUE, TIME_OUT
    }

    protected baseCrawler crawlerBelonged;

    public baseCrawler getCrawlerBelonged() {
        return crawlerBelonged;
    }

    protected Document Jdoc;

    public Document getJdoc() {
        return Jdoc;
    }

    public  baseCrawlee.State state;

    public baseCrawlee( baseCrawler crawlerBelonged) {
        this.crawlerBelonged = crawlerBelonged;
        state = State.QUEUE;
    }

    public baseCrawlee() {
        state = State.QUEUE;
    }

    public Document call() {
        System.out.println("baseCrawlee call() called.");
       return Jdoc;
    }
}
