package com.tkingless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by tsangkk on 8/8/16.
 */
public class WebCrawledDataIO implements ServletContextListener {

    final static Logger logger = LogManager.getLogger(WebCrawledDataIO.class);

    //TODO can write a file into the filesharing folder
    //TODO can connect to DB to get data
    //TODO can call CSV, file manager
    //TODO can have working threads, again....my gosh

    //ref: http://balusc.omnifaces.org/2007/07/fileservlet.html

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
