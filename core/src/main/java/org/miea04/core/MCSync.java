package org.miea04.core;

import org.miea04.core.config.Config;
import org.miea04.core.logs.StyleFormatter;
import org.miea04.core.tasks.result.TaskResult;
import org.miea04.core.tasks.parameter.StartupCheckParam;
import org.miea04.core.tasks.task.StartupCheckTask;

/**
 * MCSync
 *
 * @author MieMie
 */
public class MCSync {

    void startupCheck(StartParameter startParameter){
        TaskResult start = new StartupCheckTask().start(null);
    }

    void serviceInit(StartParameter sp) {
        Config.init(sp);
        startupCheck(sp);
    }

    public void start(String parameter) {
        StyleFormatter.initLogging();

        StartParameter sp = new StartParameter().build(parameter);

        serviceInit(sp);
    }

}
