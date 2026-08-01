package org.miea04.core.tasks.task;

import org.miea04.core.config.PathConfig;
import org.miea04.core.tasks.parameter.EmptyParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * StartupCheckTask
 *
 * @author MieMie
 */
public class StartupCheckTask implements Task<EmptyParams> {
    private static final Logger log = LoggerFactory.getLogger(StartupCheckTask.class);

    private void matchFileHandle(PathConfig.Path path){
        switch (path) {
            case DEFAULT_CONFIG_FILE_PATH -> {
                new CreateDefaultConfig().start(new EmptyParams());
            }
            default -> {
            }
        }
    }

    private Map<PathConfig.Path, Boolean> reviewDirPath() {
        HashMap<PathConfig.Path, Boolean> map = new HashMap<>();

        for (PathConfig.Path path : PathConfig.Path.values()) {
            map.put(path, Files.exists(Paths.get(path.getValue())));
        }

        return map;
    }

    private void handleReviewResult(Map<PathConfig.Path, Boolean> map) {
        for (PathConfig.Path path : map.keySet()) {
            if (map.get(path)) continue;

            Path realPath = Paths.get(path.getValue());

            if (Files.isDirectory(realPath)){
                try {
                    Files.createDirectories(realPath);
                } catch (IOException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            matchFileHandle(path);
        }
    }

    @Override
    public Class<Void> start(EmptyParams params) {
        handleReviewResult(reviewDirPath());
        return null;
    }
}
