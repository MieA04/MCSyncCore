package org.miea04.core.tasks.task;

import org.miea04.core.config.Config;
import org.miea04.core.config.PathConfig;
import org.miea04.core.model.DefaultConfig;
import org.miea04.core.model.SyncDefaultClientConfig;
import org.miea04.core.model.SyncDefaultServerConfig;
import org.miea04.core.tasks.parameter.EmptyParams;
import org.miea04.core.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * CreateDefaultConfig
 *
 * @author MieMie
 */
public class CreateDefaultConfig implements Task<EmptyParams> {
    private static final Logger log = LoggerFactory.getLogger(CreateDefaultConfig.class);

    @Override
    public Class<Void> start(EmptyParams params) {
        Path path = Paths.get(PathConfig.Path.DEFAULT_CONFIG_FILE_PATH.getPath());

        PathUtil.createFileIfAbsent(path);

        // 生成配置模板
        DefaultConfig defaultConfig = createDefaultConfigTemplate();

        writeConfigContent(path, defaultConfig);

        return null;
    }

    private DefaultConfig createDefaultConfigTemplate() {
        return switch (Config.getStartMode()) {
            case CLIENT -> SyncDefaultClientConfig.createDefault();
            case SERVER -> SyncDefaultServerConfig.createDefault();
            case NONE -> {
                String msg = "start mode type error, unknown type " + Config.getStartMode();
                log.error(msg);
                throw new RuntimeException(msg);
            }
        };
    }

    private void writeConfigContent(Path path, DefaultConfig defaultConfig){

    }
}
