package org.miea04.core.config.repository;

import org.miea04.core.StartMode;
import org.miea04.core.config.codec.ClientTomlCodec;
import org.miea04.core.config.codec.ServerTomlCodec;
import org.miea04.core.model.DefaultConfig;
import org.miea04.core.model.SyncDefaultClientConfig;
import org.miea04.core.model.SyncDefaultServerConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class DefaultConfigRepository {

    private final ClientTomlCodec clientCodec = new ClientTomlCodec();

    private final ServerTomlCodec serverCodec = new ServerTomlCodec();

    public DefaultConfig load(
            Path path,
            StartMode mode
    ) {
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(mode, "mode");

        if (!Files.isRegularFile(path)) {
            throw new IllegalStateException(
                    "Config file does not exist: " + path
            );
        }

        return switch (mode) {
            case CLIENT -> clientCodec.read(path);
            case SERVER -> serverCodec.read(path);

            case NONE -> throw new IllegalStateException(
                    "Cannot load config in NONE mode"
            );
        };
    }

    public void save(
            Path path,
            DefaultConfig config
    ) {
        Path target = path.toAbsolutePath().normalize();
        Path parent = target.getParent();

        try {
            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Cannot create config directory: " + parent,
                    e
            );
        }

        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(config, "config");

        if (config instanceof SyncDefaultClientConfig client) {
            clientCodec.write(path, client);
            return;
        }

        if (config instanceof SyncDefaultServerConfig server) {
            serverCodec.write(path, server);
            return;
        }

        throw new IllegalArgumentException("Unsupported config type: " + config.getClass().getName());
    }
}