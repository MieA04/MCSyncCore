package org.miea04.core.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * SyncDefaultClientConfig
 *
 * @author MieMie
 */
public class SyncDefaultClientConfig implements Serializable, DefaultConfig {

    private String clientId;
    private List<DefaultClientConfig> table;

    public static SyncDefaultClientConfig createDefault() {
        SyncDefaultClientConfig config = new SyncDefaultClientConfig();
        config.setClientId(UUID.randomUUID().toString());
        config.setTable(List.of(DefaultClientConfig.defaultInstance()));
        return config;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<DefaultClientConfig> getTable() {
        return table;
    }

    public void setTable(List<DefaultClientConfig> table) {
        this.table = table;
    }
}
