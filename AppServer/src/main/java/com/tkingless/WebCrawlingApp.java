package com.tkingless;

/**
 * Created by tkingless on 7/23/16.
 */

import com.tkingless.crawlee.BoardCrawlee;
import com.tkingless.utils.logTest;
import com.tkingless.webCrawler.HKJCcrawler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class WebCrawlingApp implements ServletContextListener {

    final static Logger logger = LogManager.getLogger(WebCrawlingApp.class);
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        logger.trace("contextInitialized() called");
        logTest.DoALog();

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new ScheduledHKJCcrlr(),0,30000, TimeUnit.MILLISECONDS);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Actually contextDestroyed() called but logger system turn off quicker");
        logger.trace("contextDestroyed() called");
        BoardCrawlee.TerminateAllLivingWorkers();
        scheduler.shutdown();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public class ScheduledHKJCcrlr implements Runnable{
        @Override
        public void run() {
            HKJCcrawler.GetInstance().run();
        }
    }
}
