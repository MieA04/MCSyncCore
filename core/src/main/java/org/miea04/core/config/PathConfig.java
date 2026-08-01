package org.miea04.core.config;

import org.miea04.core.StartMode;
import org.miea04.core.StartParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * PathConfig
 *
 * @author MieMie
 */

@MCSyncConfig
public class PathConfig {
    public static String WORK_PATH;
    public static String SYNC_PATH;
    public static String GAME_MODS_PATH;
    public static String LOGS_PATH;
    public static String CLIENT_DIR;
    public static String SERVER_DIR;
    public static String DEFAULT_CONFIG_FILE_PATH;

    public static final Map<String, String> PATH_MAP = new HashMap<>();

    private static void buildPathMap() {
        PATH_MAP.put("WORK_PATH", WORK_PATH);
        PATH_MAP.put("SYNC_PATH", SYNC_PATH);
        PATH_MAP.put("GAME_MODS_PATH", GAME_MODS_PATH);
        PATH_MAP.put("LOGS_PATH", LOGS_PATH);
        PATH_MAP.put("CLIENT_DIR", CLIENT_DIR);
        PATH_MAP.put("SERVER_DIR", SERVER_DIR);
        PATH_MAP.put("DEFAULT_CONFIG_FILE_PATH", DEFAULT_CONFIG_FILE_PATH);
    }

    public static void init(StartParameter sp) {
        splicePath(sp);
        buildPathMap();
    }

    private static void splicePath(StartParameter sp) {
        WORK_PATH = sp.getWorkPath();
        SYNC_PATH = sp.getWorkPath() + "/MCSyncData";
        GAME_MODS_PATH = sp.getWorkPath() + "/mods";
        CLIENT_DIR = SYNC_PATH + "/client";
        SERVER_DIR = SYNC_PATH + "/server";
        LOGS_PATH = SYNC_PATH + (
                        sp.getServiceMode() == StartMode.CLIENT?
                            StartMode.CLIENT.value.toLowerCase() : StartMode.SERVER.value.toLowerCase()
                ) + "/logs";
        DEFAULT_CONFIG_FILE_PATH = SYNC_PATH + "/config.toml";
    }
}
