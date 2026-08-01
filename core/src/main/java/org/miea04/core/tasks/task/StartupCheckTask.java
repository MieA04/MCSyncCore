package org.miea04.core.tasks.task;

import org.miea04.core.config.PathConfig;
import org.miea04.core.tasks.parameter.TaskParams;
import org.miea04.core.tasks.result.TaskResult;
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
public class StartupCheckTask implements Task {
    private static final Logger log = LoggerFactory.getLogger(StartupCheckTask.class);

    private Map<String, Boolean> reviewDirPath() {
        HashMap<String, Boolean> map = new HashMap<>();

        PathConfig.PATH_MAP.forEach((key, value) -> {
            map.put(key, Files.exists(Paths.get(value)));
        });

        return map;
    }

    private

    private void handleReviewResult(Map<String, Boolean> map) {
        for(String key : map.keySet()){
            if(map.get(key)) continue;

            Path path = Paths.get(PathConfig.PATH_MAP.get(key));

            if (Files.isDirectory(path)){
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            // TODO: 文件创建交给专门的任务

        }
    }

    @Override
    public TaskResult start(TaskParams taskParams) {
        handleReviewResult(reviewDirPath());
        return null;
    }
}
