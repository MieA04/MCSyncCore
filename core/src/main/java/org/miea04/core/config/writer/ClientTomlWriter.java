package org.miea04.core.config.writer;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import org.miea04.core.model.DefaultClientConfig;
import org.miea04.core.model.SyncDefaultClientConfig;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ClientTomlWriter
        implements TomlConfigWriter<SyncDefaultClientConfig> {

    @Override
    public void write(
            Path path,
            SyncDefaultClientConfig config
    ) {
        try (
                CommentedFileConfig toml = CommentedFileConfig.builder(path)
                             .sync()
                             .build()
        ) {

            toml.clear();
            toml.set("schemaVersion", 1);
            toml.set("clientId", config.getClientId());

            List<CommentedConfig> servers = new ArrayList<>();

            for (DefaultClientConfig server : config.getTable()) {
                CommentedConfig table = toml.createSubConfig();

                table.set("name", server.getHostName());
                table.set("gameServerHost", server.getGameServerHost());
                table.set("delegatedServerHost", server.getDelegatedServerHost());

                servers.add(table);
            }

            toml.set("servers", servers);
            toml.save();
        }
    }
}