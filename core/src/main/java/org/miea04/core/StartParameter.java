package org.miea04.core;

import org.miea04.core.model.DefaultServerConfig;

/**
 * StartParameter
 *
 * @author MieMie
 */
public class StartParameter{
    private String WORK_PATH;
    private StartMode SERVICE_MODE;
    private DefaultServerConfig.NodeType NODE_TYPE;
    private String SERVER_PORT;
    private String DELEGATE_HOST;

    private boolean matchParam(String paramName, String param){
        return switch (paramName) {
            case "WORK_PATH" -> {
                this.WORK_PATH = param;
                yield true;
            }
            case "SERVICE_MODE" -> {
                this.SERVICE_MODE = StartMode.mode(param);
                yield true;
            }
            case "NODE_TYPE" -> {
                this.NODE_TYPE = DefaultServerConfig.NodeType.type(param);
                yield true;
            }
            case "SERVER_PORT" -> {
                this.SERVER_PORT = param;
                yield true;
            }
            case "DELEGATE_HOST" -> {
                this.DELEGATE_HOST = param;
                yield true;
            }
            default -> false;
        };
    }

    public StartParameter build(String param){
        StartParameter startParameter = new StartParameter();

        String[] split = param.split("\\|");
        for (String s : split) {
            String[] res = s.split("@");
            boolean matched = startParameter.matchParam(res[0], res[1]);
            if (!matched) {
                throw new RuntimeException("unknown parameter: " + s);
            }
        }

        return startParameter;
    }

    public String getWorkPath() {
        return this.WORK_PATH;
    }

    public StartMode getServiceMode() {
        return this.SERVICE_MODE;
    }

    public DefaultServerConfig.NodeType getNodeType() {
        return NODE_TYPE;
    }

    public String getServerPort() {
        return SERVER_PORT;
    }

    public String getDelegateHost() {
        return DELEGATE_HOST;
    }

    @Override
    public String toString() {
        return "StartParameter{" +
                "WORK_PATH='" + WORK_PATH + '\'' +
                ", SERVICE_MODE=" + SERVICE_MODE +
                '}';
    }
}
