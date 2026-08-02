package org.miea04.core;

import org.miea04.core.config.Config;
import org.miea04.core.logs.StyleFormatter;
import org.miea04.core.tasks.parameter.EmptyParams;
import org.miea04.core.tasks.task.StartupCheckTask;

/**
 * MCSync
 *
 * @author MieMie
 */
public class MCSync {
    void serviceInit() {
        new StartupCheckTask().start(new EmptyParams());
    }

    public void start(String parameter) {
        StyleFormatter.initLogging();

        StartParameter sp = new StartParameter().build(parameter);

        Config.init(sp);
        serviceInit();
    }
}
