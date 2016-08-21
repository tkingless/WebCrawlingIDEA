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

    //TODO can write a file into the filesharing folder
    //TODO can connect to DB to get data
    //TODO can call CSV, file manager
    //TODO can have working threads, again....my gosh

    private String filePath;
    private Document config;
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("WebCrawledDataIO init() called.");

        config = LoadConfigFile();

        filePath = (String) config.get("HostedFilesPath");
        FileManager fileManager;

        if(FileManager.CreateFolder(filePath)){
            fileManager = new FileManager(filePath + "/helloWorld.txt");

            try {
                fileManager.Append("you are the best!");
                fileManager.Close();
            } catch (IOException e) {
                logger.error("filemanager: ",e);
            }
        }

        LoadConfigFile();

        logger.info("[Important] The WDCIO config json file should be placed at: current path: " + (new File(".")).getAbsolutePath());

        if(FileManager.CheckFileExist("WCDIOconfig_sample.json")){
            System.out.println("found the json");
        } else {
            System.out.println("Not found the json");
        }


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

    private Document LoadConfigFile(){

        Document config = null;

        ClassLoader classLoader = getClass().getClassLoader();
        try {

            String jsonString = "";
            jsonString = IOUtils.toString(classLoader.getResourceAsStream("WCDIOconfig_sample.json"));
            config = Document.parse(jsonString);

        } catch (IOException e) {
            logger.error("Have you handled WCDIOconfig.json? ", e);
        }

        return config;
    }

    public class ScheduledWCDIOin implements Runnable{
        @Override
        public void run() {
            logger.info("WCDIOcsvIn run()");
            WCDIOcsvIn.GetInstance().run();
        }
    }

}
