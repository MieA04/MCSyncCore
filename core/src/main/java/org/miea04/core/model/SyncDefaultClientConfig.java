package org.miea04.core.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * SyncDefaultConfig
 *
 * @author MieMie
 */
public class SyncDefaultClientConfig implements Serializable, DefaultConfig {

    private String clientId;
    private List<DefaultClientConfig> table;

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
