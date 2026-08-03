package org.miea04.core.tasks.task;

import org.miea04.core.StartMode;
import org.miea04.core.config.PathConfig;
import org.miea04.core.tasks.parameter.EmptyParams;
import org.miea04.core.tasks.parameter.PathParams;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.function.Consumer;

/**
 * StartupCheckTask
 *
 * @author MieMie
 */

public class StartupCheckTask implements Task<EmptyParams> {
    private static final EnumMap<PathConfig.PathKey, Consumer<Path>> HANDLERS = new EnumMap<>(PathConfig.PathKey.class);

    private final StartMode startMode;

    public StartupCheckTask(StartMode startMode) {
        this.startMode = startMode;
    }

    static {
        HANDLERS.put(
                PathConfig.PathKey.DEFAULT_CONFIG_FILE_PATH,
                path -> new CreateDefaultConfig()
                        .start(new PathParams(path))
        );
    }

    @Override
    public Class<Void> start(EmptyParams params) {
        pathCheck();
        return null;
    }

    private void pathCheck() {
        for (PathConfig.PathEntry entry : PathConfig.all()) {
            if (!entry.fileMode().matches(startMode)) {
                continue;
            }

            if (entry.flagType() == PathConfig.FlagType.DIR) {
                entry.createIfMissing();
                continue;
            }

            Consumer<Path> handler = HANDLERS.get(entry.key());

            if (handler != null) {
                handler.accept(entry.path());
            }
        }
    }
}
