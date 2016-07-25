package com.tkk;

/**
 * Created by tkingless on 7/23/16.
 */

import com.tkk.crawlee.BoardCrawlee;
import com.tkk.webCrawler.HKJCcrawler;
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


        //seems need to joinThread, it happens to if this function run to ends, the sub threads from this thread
        //will automatically stops after while
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new ScheduledHKJCcrlr(),0,30000, TimeUnit.MILLISECONDS);


        System.out.println("Program main runned to LAST line!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.trace("contextDestroyed() called");
        //TODO stop all living match workers
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
