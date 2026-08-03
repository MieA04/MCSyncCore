package org.miea04.core.tasks.task;

import org.miea04.core.config.Config;
import org.miea04.core.config.repository.DefaultConfigRepository;
import org.miea04.core.model.DefaultConfig;
import org.miea04.core.model.SyncDefaultClientConfig;
import org.miea04.core.model.SyncDefaultServerConfig;
import org.miea04.core.tasks.parameter.PathParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class CreateDefaultConfig implements Task<PathParams> {

    private final static Logger log = LoggerFactory.getLogger(CreateDefaultConfig.class);

    private final DefaultConfigRepository repository = new DefaultConfigRepository();

    @Override
    public Class<Void> start(PathParams params) {
        Path path = params.path().toAbsolutePath().normalize();

        if (Files.exists(path)) {
            validateExistingFile(path);
            return null;
        }

        log.info("Creating default config: {}", path);
        repository.save(path, createDefaultConfigTemplate());
        log.info("Default config created: {}", path);

        verifyCreated(path);
        return null;
    }

    private void validateExistingFile(Path path) {
        if (!Files.isRegularFile(path)) {
            throw new IllegalStateException(
                    "Config path is not a regular file: " + path
            );
        }

        try {
            if (Files.size(path) == 0) {
                throw new IllegalStateException(
                        "Config file is empty: " + path
                                + ". Delete this stale file and restart."
                );
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Cannot inspect config file: " + path,
                    e
            );
        }
    }

    private void verifyCreated(Path path) {
        if (!Files.isRegularFile(path)) {
            throw new IllegalStateException(
                    "Config file was not created: " + path
            );
        }

        try {
            if (Files.size(path) == 0) {
                throw new IllegalStateException(
                        "Created config file is empty: " + path
                );
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Cannot verify config file: " + path,
                    e
            );
        }
    }

    private DefaultConfig createDefaultConfigTemplate() {
        return switch (Config.getStartMode()) {
            case CLIENT ->
                    SyncDefaultClientConfig.createDefault();

            case SERVER ->
                    SyncDefaultServerConfig.createDefault();

            case NONE -> throw new IllegalStateException(
                    "Cannot create config in NONE mode"
            );
        };
    }
}