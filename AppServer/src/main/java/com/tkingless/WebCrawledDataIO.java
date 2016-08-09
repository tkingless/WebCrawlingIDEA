package com.tkingless;

import com.tkingless.DBobject.MatchEventDAO;
import com.tkingless.utils.FileManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;

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

    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
    private String filePath;
    private MatchEventDAO workerDAO;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("WebCrawledDataIO init() called.");

        FileManager fileManager = new FileManager(WCDIOconstants.testEnvFileSerlvetABSpath + "/helloWorld.txt");

        try {
            fileManager.Append("you are the best!");
            fileManager.Close();
        } catch (IOException e) {
            logger.error("filemanager: ",e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
