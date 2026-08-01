package org.miea04.core.model;

import java.io.Serializable;

/**
 * DefaultConfig
 *
 * @author MieMie
 */
public class DefaultClientConfig implements Serializable {

    private String hostName;
    private String gameServerHost;
    private String delegatedServerHost;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getGameServerHost() {
        return gameServerHost;
    }

    public void setGameServerHost(String gameServerHost) {
        this.gameServerHost = gameServerHost;
    }

    public String getDelegatedServerHost() {
        return delegatedServerHost;
    }

    public void setDelegatedServerHost(String delegatedServerHost) {
        this.delegatedServerHost = delegatedServerHost;
    }
}
