package com.tkingless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by tsangkk on 8/8/16.
 *
 */
public class WebCrawledDataIO implements ServletContextListener {

    final static Logger logger = LogManager.getLogger(WebCrawledDataIO.class);

    private ScheduledExecutorService Inscheduler;
    private ScheduledExecutorService Outscheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("WebCrawledDataIO init() called.");

        Inscheduler = Executors.newSingleThreadScheduledExecutor();
        Inscheduler.scheduleAtFixedRate(new ScheduledWCDIOin(),0,1000 * 5, TimeUnit.MILLISECONDS);
        Outscheduler = Executors.newSingleThreadScheduledExecutor();
        Outscheduler.scheduleAtFixedRate(new ScheduledWCDIOout(),2500,1000 * 5, TimeUnit.MILLISECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        try {
            Inscheduler.shutdown();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public class ScheduledWCDIOin implements Runnable{
        @Override
        public void run() {
            WCDIOcsvIn.GetInstance().run();
        }
    }

    public class ScheduledWCDIOout implements Runnable{
        @Override
        public void run() {
            WCDIOcsvOut csvOut = new WCDIOcsvOut();
            csvOut.run();
        }
    }

}
