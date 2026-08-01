package org.miea04.core.config;

import org.miea04.core.StartMode;
import org.miea04.core.StartParameter;

import javax.annotation.Nonnull;

/**
 * PathConfig
 *
 * @author MieMie
 */

@MCSyncConfig
public class PathConfig {

    public enum Path {
        WORK_PATH,
        SYNC_PATH,
        GAME_MODS_PATH,
        LOGS_PATH,
        CLIENT_DIR,
        SERVER_DIR,
        DEFAULT_CONFIG_FILE_PATH;

        private String value;

        public String getValue() {
            return value;
        }
    }

    public static void init(StartParameter sp) {
        Path.WORK_PATH.value = sp.getWorkPath();
        Path.SYNC_PATH.value = sp.getWorkPath() + "/MCSyncData";
        Path.GAME_MODS_PATH.value = sp.getWorkPath() + "/mods";
        Path.CLIENT_DIR.value = Path.SYNC_PATH.value + "/client";
        Path.SERVER_DIR.value = Path.SYNC_PATH.value + "/server";
        Path.LOGS_PATH.value = Path.SYNC_PATH.value + currentModeLowerCase(sp) + "/logs";
        Path.DEFAULT_CONFIG_FILE_PATH.value =Path.SYNC_PATH.value + currentModeLowerCase(sp) + "/config.toml";
    }

    @Nonnull
    private static String currentModeLowerCase(StartParameter sp) {
        return sp.getServiceMode() == StartMode.CLIENT ?
                StartMode.CLIENT.value.toLowerCase() : StartMode.SERVER.value.toLowerCase();
    }
}
