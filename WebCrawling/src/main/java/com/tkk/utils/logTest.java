
package com.tkk.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by tsangkk on 7/22/16.
 */
public class logTest {

    final public static Logger logger = LogManager.getLogger(logTest.class);

    public static void DoALog() {

        logger.info("logTest DoALog() called");
        logger.error("logTest DoALog() called");

    }
}
