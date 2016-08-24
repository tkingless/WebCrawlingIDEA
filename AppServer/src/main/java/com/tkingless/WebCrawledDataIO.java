package com.tkingless;

import com.tkingless.utils.FileManager;
import com.tkingless.webCrawler.HKJCcrawler;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by tsangkk on 8/8/16.
 *
 */
public class WebCrawledDataIO implements ServletContextListener {

    final static Logger logger = LogManager.getLogger(WebCrawledDataIO.class);

    private String filePath;
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("WebCrawledDataIO init() called.");

        //TODO csvout worker should delay sometime after csvin worker, sensibly, and lower frequency
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new ScheduledWCDIOin(),0,1000 * 5, TimeUnit.MILLISECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        try {
            scheduler.shutdown();
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

}
