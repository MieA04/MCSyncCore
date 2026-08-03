package org.miea04.core;

import org.miea04.core.config.Config;
import org.miea04.core.config.PathConfig;
import org.miea04.core.logs.StyleFormatter;
import org.miea04.core.model.RuntimeOptions;
import org.miea04.core.tasks.parameter.EmptyParams;
import org.miea04.core.tasks.task.StartupCheckTask;
import org.miea04.core.util.StartParameterParser;

/**
 * MCSync
 *
 * @author MieMie
 */
public class MCSync {

    void serviceInit(RuntimeOptions options) {
        new StartupCheckTask(options.serviceMode())
                .start(new EmptyParams());
    }

    public void start(String parameter) {
        StyleFormatter.initLogging();

        RuntimeOptions options =
                StartParameterParser.parse(parameter);

        Config.init(options);
        PathConfig.init(options);

        serviceInit(options);
    }
}
