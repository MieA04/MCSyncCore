package org.miea04.core;

import org.miea04.core.logs.StyleFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MCSync
 *
 * @author MieMie
 */
public class MCSync {

    private static final Logger log = LoggerFactory.getLogger(MCSync.class);

    private void initLogManager() {
        StyleFormatter.initLogging();
    }

    public void start(String parameter) {
        log.info("MCSync starting...");



        log.info("MCSync start successfully.");
    }
}
