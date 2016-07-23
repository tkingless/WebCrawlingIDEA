package com.tkk;

/**
 * Created by tkingless on 7/23/16.
 */

import com.tkk.webCrawler.HKJCcrawler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class WebCrawlingApp implements ServletContextListener {

    final static Logger logger = LogManager.getLogger(WebCrawlingApp.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        logger.trace("contextInitialized() called");
        logTest.DoALog();

        HKJCcrawler hkjcCrlr = HKJCcrawler.GetInstance();
        hkjcCrlr.NewThreadRun();

        System.out.println("Program main runned to LAST line!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.trace("contextDestroyed() called");

    }
}
