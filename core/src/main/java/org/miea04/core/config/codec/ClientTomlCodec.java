package org.miea04.core.config.codec;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import org.miea04.core.model.DefaultClientConfig;
import org.miea04.core.model.SyncDefaultClientConfig;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ClientTomlCodec implements ConfigCodec<SyncDefaultClientConfig> {

    @Override
    public SyncDefaultClientConfig read(Path path) {Objects.requireNonNull(path, "path");

        try (
                CommentedFileConfig toml = CommentedFileConfig.builder(path).sync().build()
        ) {

            toml.load();
            TomlReadSupport.validateSchema(toml);

            SyncDefaultClientConfig result = new SyncDefaultClientConfig();

            result.setClientId(
                    TomlReadSupport.requireString(
                            toml,
                            "clientId"
                    )
            );

            List<Config> serverTables =
                    TomlReadSupport.requireTableList(
                            toml,
                            "servers"
                    );

            List<DefaultClientConfig> servers = new ArrayList<>(serverTables.size());

            for (Config serverTable : serverTables) {
                DefaultClientConfig server = new DefaultClientConfig();

                server.setHostName(
                        TomlReadSupport.requireString(
                                serverTable,
                                "name"
                        )
                );

                server.setGameServerHost(
                        TomlReadSupport.requireString(
                                serverTable,
                                "gameServerHost"
                        )
                );

                server.setDelegatedServerHost(
                        TomlReadSupport.requireString(
                                serverTable,
                                "delegatedServerHost"
                        )
                );

                servers.add(server);
            }

            result.setTable(List.copyOf(servers));
            return result;
        }
    }

    @Override
    public void write(
            Path path,
            SyncDefaultClientConfig config
    ) {
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(
                config.getTable(),
                "client config table"
        );

        try (
                CommentedFileConfig toml = CommentedFileConfig.builder(path).sync().build()
        ) {
            toml.clear();

            toml.set(
                    "schemaVersion",
                    TomlReadSupport.SCHEMA_VERSION
            );

            toml.set("clientId", config.getClientId());

            List<CommentedConfig> servers =
                    new ArrayList<>();

            for (DefaultClientConfig server : config.getTable()) {
                CommentedConfig table =
                        toml.createSubConfig();

                table.set("name", server.getHostName());

                table.set(
                        "gameServerHost",
                        server.getGameServerHost()
                );

                table.set(
                        "delegatedServerHost",
                        server.getDelegatedServerHost()
                );

                servers.add(table);
            }

            toml.set("servers", servers);
            toml.save();
        }
    }
}