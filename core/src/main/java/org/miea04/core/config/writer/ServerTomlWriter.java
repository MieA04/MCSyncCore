package org.miea04.core.config.writer;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import org.miea04.core.model.DefaultServerConfig;
import org.miea04.core.model.SyncDefaultServerConfig;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;

public final class ServerTomlWriter implements TomlConfigWriter<SyncDefaultServerConfig> {

    @Override
    public void write(
            Path path,
            SyncDefaultServerConfig config
    ) {
        DefaultServerConfig value = Objects.requireNonNull(config.getTable(), "server config table");

        try (
                CommentedFileConfig toml = CommentedFileConfig.builder(path).sync().build()
        ) {

            toml.clear();
            toml.set("schemaVersion", 1);

            CommentedConfig node = toml.createSubConfig();
            node.set("id", value.getNodeId());
            node.set("name", value.getNodeName());
            node.set(
                    "type",
                    value.getNodeType()
                            .name()
                            .toLowerCase(Locale.ROOT)
            );

            CommentedConfig delegate = toml.createSubConfig();
            delegate.set("id", value.getDelegatedServerId());
            delegate.set(
                    "host",
                    value.getDelegatedServerHost()
            );

            toml.set("node", node);
            toml.set("delegate", delegate);

            toml.save();
        }
    }
}