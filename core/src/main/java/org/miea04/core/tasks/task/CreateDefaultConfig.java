package org.miea04.core.tasks.task;

import org.miea04.core.StartMode;
import org.miea04.core.config.Config;
import org.miea04.core.config.PathConfig;
import org.miea04.core.model.*;
import org.miea04.core.tasks.parameter.EmptyParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * CreateDefaultConfig
 *
 * @author MieMie
 */
public class CreateDefaultConfig implements Task<EmptyParams> {
    private static final Logger log = LoggerFactory.getLogger(CreateDefaultConfig.class);

    private boolean createConfig(Path file){

        try {
            // 原子性创建，已存在则报错
            Files.createFile(file);
            log.info("Empty file creation successful.");
            return true;
        } catch (FileAlreadyExistsException e) {
            log.info(
                    "File already exists, directory check result is unavailable. Check if StartupCheckTask is trustworthy. Error message: {}",
                    e.getMessage()
            );
        } catch (NoSuchFileException e) {
            log.error(
                    "The parent directory of default.toml does not exist, directory check result is unavailable. Check if StartupCheckTask is trustworthy. Error message: {}",
                    e.getMessage()
            );
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return false;
    }

    @Override
    public Class<Void> start(EmptyParams params) {
        Path path = Paths.get(PathConfig.Path.DEFAULT_CONFIG_FILE_PATH.getValue());

        if (!createConfig(path)) {
            throw new RuntimeException("Default configuration file creation failed, startup aborted.");
        }

        DefaultConfig defaultConfig = createDefaultConfig();

        writeConfigContent(path, defaultConfig);

        return null;
    }

    private DefaultConfig createDefaultConfig() {
        if (Config.getStartMode() == StartMode.CLIENT) {
            DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
            defaultClientConfig.setHostName("");
            defaultClientConfig.setGameServerHost("");
            defaultClientConfig.setDelegatedServerHost("");

            SyncDefaultClientConfig syncDefaultClientConfig = new SyncDefaultClientConfig();
            ArrayList<DefaultClientConfig> defaultClientConfigs = new ArrayList<>();

            defaultClientConfigs.add(defaultClientConfig);
            syncDefaultClientConfig.setClientId(UUID.randomUUID().toString());
            syncDefaultClientConfig.setTable(defaultClientConfigs);

            return syncDefaultClientConfig;
        } else if (Config.getStartMode() == StartMode.SERVER) {
            DefaultServerConfig defaultServerConfig = new DefaultServerConfig();
            defaultServerConfig.setNodeId(UUID.randomUUID().toString());
            defaultServerConfig.setNodeName("");
            defaultServerConfig.setNodeType(Config.getNodeType());

            if (Config.getNodeType() == DefaultServerConfig.NodeType.COMPLETE){
                defaultServerConfig.setDelegatedServerHost("127.0.0.1:" + Config.getServerPort());
            }
            else {
                defaultServerConfig.setDelegatedServerHost("");
            }

            defaultServerConfig.setDelegatedServerId("");

            SyncDefaultServerConfig syncDefaultServerConfig = new SyncDefaultServerConfig();
            syncDefaultServerConfig.setTable(defaultServerConfig);
            return syncDefaultServerConfig;
        } else {
            String msg = "start mode type error, unknown type " + Config.getStartMode();
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }

    private void writeConfigContent(Path path, DefaultConfig defaultConfig){

    }
}
