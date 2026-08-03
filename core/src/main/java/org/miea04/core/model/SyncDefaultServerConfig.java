package org.miea04.core.model;

import org.miea04.core.config.Config;

import java.io.Serializable;
import java.util.UUID;

/**
 * SyncDefaultServerConfig
 *
 * @author MieMie
 */
public class SyncDefaultServerConfig implements Serializable, DefaultConfig{

    private DefaultServerConfig table;

    public static SyncDefaultServerConfig createDefault() {
        DefaultServerConfig config = new DefaultServerConfig();

        config.setNodeId(UUID.randomUUID().toString());
        config.setNodeName("");
        config.setNodeType(Config.getNodeType());
        config.setDelegatedServerHost(Config.getDelegateHost());
        config.setDelegatedServerId("");

        return SyncDefaultServerConfig.of(config);
    }

    private static SyncDefaultServerConfig of(DefaultServerConfig table) {
        SyncDefaultServerConfig config = new SyncDefaultServerConfig();
        config.setTable(table);
        return config;
    }

    public DefaultServerConfig getTable() {
        return table;
    }

    public void setTable(DefaultServerConfig table) {
        this.table = table;
    }
}
