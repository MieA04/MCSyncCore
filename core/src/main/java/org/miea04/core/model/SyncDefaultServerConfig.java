package org.miea04.core.model;

import java.io.Serializable;
import java.util.List;

/**
 * SyncDefaultServerConfig
 *
 * @author MieMie
 */
public class SyncDefaultServerConfig implements Serializable, DefaultConfig{

    private DefaultServerConfig table;

    public DefaultServerConfig getTable() {
        return table;
    }

    public void setTable(DefaultServerConfig table) {
        this.table = table;
    }
}
