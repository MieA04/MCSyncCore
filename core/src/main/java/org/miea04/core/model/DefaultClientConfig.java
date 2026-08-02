package org.miea04.core.model;

import java.io.Serializable;

/**
 * DefaultClientConfig
 *
 * @author MieMie
 */
public class DefaultClientConfig implements Serializable {

    private String hostName;
    private String gameServerHost;
    private String delegatedServerHost;

    public static DefaultClientConfig defaultInstance() {
        DefaultClientConfig config = new DefaultClientConfig();
        config.setHostName("");
        config.setGameServerHost("");
        config.setDelegatedServerHost("");
        return config;
    }

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
