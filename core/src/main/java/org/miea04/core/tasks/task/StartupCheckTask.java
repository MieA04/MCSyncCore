package org.miea04.core.tasks.task;

import org.miea04.core.config.PathConfig;
import org.miea04.core.tasks.parameter.EmptyParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.function.Consumer;

/**
 * StartupCheckTask
 *
 * @author MieMie
 */
public class StartupCheckTask implements Task<EmptyParams> {
    private static final EnumMap<PathConfig.Path, Consumer<Path>> HANDLERS = new EnumMap<>(PathConfig.Path.class);

    static {
        HANDLERS.put(
                PathConfig.Path.DEFAULT_CONFIG_FILE_PATH,
                path -> new CreateDefaultConfig().start(new EmptyParams())
        );
    }

    @Override
    public Class<Void> start(EmptyParams params) {
        pathCheck();
        return null;
    }

    private static void pathCheck() {
        for (PathConfig.Path path : PathConfig.Path.values()) {
            if (!path.needHandle()) continue;

            path.createIfMissing();

            Consumer<Path> handler = HANDLERS.get(path);
            if (handler != null) handler.accept(Paths.get(path.getPath()));
        }
    }
}
