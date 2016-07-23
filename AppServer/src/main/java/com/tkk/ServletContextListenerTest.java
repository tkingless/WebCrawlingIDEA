package com.tkk;

/**
 * Created by tkingless on 7/23/16.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.tkk.logTest;


public class ServletContextListenerTest implements ServletContextListener {

    final static Logger logger = LogManager.getLogger(ServletContextListenerTest.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        logger.trace("contextInitialized() called");
        logTest.DoALog();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.trace("contextDestroyed() called");

    }
}
