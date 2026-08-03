package org.miea04.core.config.codec;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import org.miea04.core.model.DefaultServerConfig;
import org.miea04.core.model.SyncDefaultServerConfig;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;

public final class ServerTomlCodec implements ConfigCodec<SyncDefaultServerConfig> {

    @Override
    public SyncDefaultServerConfig read(Path path) {
        Objects.requireNonNull(path, "path");

        try (
                CommentedFileConfig toml = CommentedFileConfig.builder(path).sync().build()
        ) {

            toml.load();
            TomlReadSupport.validateSchema(toml);

            Config node =
                    TomlReadSupport.requireTable(
                            toml,
                            "node"
                    );

            Config delegate =
                    TomlReadSupport.requireTable(
                            toml,
                            "delegate"
                    );

            String nodeTypeValue =
                    TomlReadSupport.requireString(
                            node,
                            "type"
                    );

            DefaultServerConfig.NodeType nodeType =
                    DefaultServerConfig.NodeType.type(
                            nodeTypeValue
                    );

            if (nodeType == null) {
                throw new IllegalStateException(
                        "Unknown node type: " + nodeTypeValue
                );
            }

            DefaultServerConfig server =
                    new DefaultServerConfig();

            server.setNodeId(
                    TomlReadSupport.requireString(
                            node,
                            "id"
                    )
            );

            server.setNodeName(
                    TomlReadSupport.requireString(
                            node,
                            "name"
                    )
            );

            server.setNodeType(nodeType);

            server.setDelegatedServerId(
                    TomlReadSupport.requireString(
                            delegate,
                            "id"
                    )
            );

            server.setDelegatedServerHost(
                    TomlReadSupport.requireString(
                            delegate,
                            "host"
                    )
            );

            SyncDefaultServerConfig result = new SyncDefaultServerConfig();

            result.setTable(server);
            return result;
        }
    }

    @Override
    public void write(
            Path path,
            SyncDefaultServerConfig config
    ) {
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(config, "config");

        DefaultServerConfig value =
                Objects.requireNonNull(
                        config.getTable(),
                        "server config table"
                );

        try (
                CommentedFileConfig toml = CommentedFileConfig.builder(path).sync().build()
        ) {

            toml.clear();

            toml.set(
                    "schemaVersion",
                    TomlReadSupport.SCHEMA_VERSION
            );

            CommentedConfig node =
                    toml.createSubConfig();

            node.set("id", value.getNodeId());
            node.set("name", value.getNodeName());

            node.set(
                    "type",
                    value.getNodeType()
                            .name()
                            .toLowerCase(Locale.ROOT)
            );

            CommentedConfig delegate =
                    toml.createSubConfig();

            delegate.set(
                    "id",
                    value.getDelegatedServerId()
            );

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