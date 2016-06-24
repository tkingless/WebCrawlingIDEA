package com.tkk.webCrawling.crawleeClass;

import com.tkk.webCrawling.webCrawler.BaseCrawler;
import org.jsoup.nodes.Document;

import java.util.concurrent.Callable;

/**
 * Created by tkingkwun on 24/6/2016.
 */
public class baseCrawlee implements Callable<Document> {

    enum State {
        SUCCESS, FAILURE, QUEUE, TIME_OUT
    }

    private BaseCrawler crawlerBelonged;

    public BaseCrawler getCrawlerBelonged() {
        return crawlerBelonged;
    }

    protected Document Jdoc;

    public Document getJdoc() {
        return Jdoc;
    }

    public  baseCrawlee.State state;

    public baseCrawlee( BaseCrawler crawlerBelonged) {
        this.crawlerBelonged = crawlerBelonged;
        state = State.QUEUE;
    }

    public baseCrawlee() {
        state = State.QUEUE;
    }

    public Document call() {
        System.out.println("baseCrwalle call() called.");
       return Jdoc;
    }
}
