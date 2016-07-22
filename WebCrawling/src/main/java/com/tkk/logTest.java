package com.tkk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by tsangkk on 7/22/16.
 */
public class logTest {

    final static Logger logger = LogManager.getLogger(logTest.class);

    public static void main(String[] args) {

        logger.debug("This is debug");
        logger.trace("Entering application.");
        logger.error("Didn't do it.");

    }
}
