package org.miea04.core.tasks.task;

import org.miea04.core.config.Config;
import org.miea04.core.model.DefaultConfig;
import org.miea04.core.model.SyncDefaultClientConfig;
import org.miea04.core.model.SyncDefaultServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.miea04.core.tasks.parameter.PathParams;

import java.nio.file.Path;

/**
 * CreateDefaultConfig
 *
 * @author MieMie
 */
public class CreateDefaultConfig implements Task<PathParams> {
    private static final Logger log = LoggerFactory.getLogger(CreateDefaultConfig.class);

    @Override
    public Class<Void> start(PathParams params) {
        Path path = params.path();

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
        // TODO: TOML解析库缺少写入功能，写入功能完成后再回来完成
    }
}
