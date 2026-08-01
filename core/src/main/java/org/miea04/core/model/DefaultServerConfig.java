package org.miea04.core.model;

import java.io.Serializable;

/**
 * DefaultServerConfig
 *
 * @author MieMie
 */
public class DefaultServerConfig implements Serializable {

    private String nodeId;
    private String nodeName;
    private NodeType nodeType;
    private String delegatedServerId;
    private String delegatedServerHost;

    public enum NodeType {
        COMPLETE("complete"),
        DELEGATED("delegated");

        final String value;

        NodeType(String value) {
            this.value = value;
        }

        public static NodeType type(String typeStr){
            if ("complete".equals(typeStr)) return COMPLETE;
            else if ("delegated".equals(typeStr)) return DELEGATED;
            else return null;
        }
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public String getDelegatedServerId() {
        return delegatedServerId;
    }

    public void setDelegatedServerId(String delegatedServerId) {
        this.delegatedServerId = delegatedServerId;
    }

    public String getDelegatedServerHost() {
        return delegatedServerHost;
    }

    public void setDelegatedServerHost(String delegatedServerHost) {
        this.delegatedServerHost = delegatedServerHost;
    }
}
