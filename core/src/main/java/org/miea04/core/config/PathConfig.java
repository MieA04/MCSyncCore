package org.miea04.core.config;

import org.miea04.core.StartMode;
import org.miea04.core.StartParameter;
import org.miea04.core.util.PathUtil;

import javax.annotation.Nonnull;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * PathConfig
 *
 * @author MieMie
 */

@MCSyncConfig
public class PathConfig {

    public enum FlagType{
        DIR,
        FILE
    }

    public enum FileMode {
        CLIENT,
        SERVER,
        SHARE;

        public static FileMode matchMode(){
            if (Config.getStartMode() == StartMode.CLIENT) return CLIENT;
            else if (Config.getStartMode() == StartMode.SERVER) return SERVER;
            else return null;
        }

        /**
         * 当前启动模式下对应哪种文件模式
         */
        public boolean matchesCurrentMode(){
            StartMode mode = Config.getStartMode();
            return this == SHARE
                    || (this == CLIENT && mode == StartMode.CLIENT)
                    || (this == SERVER && mode == StartMode.SERVER);
        }
    }

    public enum Path {
        WORK_PATH,
        SYNC_PATH,
        GAME_MODS_PATH,
        LOGS_PATH,
        CLIENT_DIR,
        SERVER_DIR,
        DEFAULT_CONFIG_FILE_PATH;

        private String path;
        private FlagType flagType;
        private FileMode fileMode;

        public String getPath() {
            return path;
        }

        public FlagType getFlagType() {
            return flagType;
        }

        public FileMode getFlagMode() {
            return fileMode;
        }

        /**
         * 当前启动模式下该路径是否需要被处理（模式生效且路径不存在）
         */
        public boolean needHandle() {
            return fileMode != null && fileMode.matchesCurrentMode() && !Files.exists(Paths.get(path));
        }

        /**
         * 创建目录（仅 DIR 类型生效），FILE 类型由外部处理器负责
         */
        public void createIfMissing() {
            if (flagType == FlagType.DIR) {
                PathUtil.createDirectoriesQuietly(Paths.get(path));
            }
        }
    }

    public static void init(StartParameter sp) {
        Path workPath = Path.WORK_PATH;
        workPath.path = sp.getWorkPath();
        workPath.flagType = FlagType.DIR;
        workPath.fileMode = FileMode.SHARE;

        Path syncPath = Path.SYNC_PATH;
        syncPath.path = sp.getWorkPath() + "/MCSyncData";
        syncPath.flagType = FlagType.DIR;
        syncPath.fileMode = FileMode.SHARE;

        Path gameModsPath = Path.GAME_MODS_PATH;
        gameModsPath.path = sp.getWorkPath() + "/mods";
        gameModsPath.flagType = FlagType.DIR;
        gameModsPath.fileMode = FileMode.SHARE;

        Path clientDir = Path.CLIENT_DIR;
        clientDir.path = Path.SYNC_PATH.path + "/client";
        clientDir.flagType = FlagType.DIR;
        clientDir.fileMode = FileMode.CLIENT;

        Path serverDir = Path.SERVER_DIR;
        serverDir.path = Path.SYNC_PATH.path + "/server";
        serverDir.flagType = FlagType.DIR;
        serverDir.fileMode = FileMode.SERVER;

        Path logsPath = Path.LOGS_PATH;
        logsPath.path = Path.SYNC_PATH.path + "/" + currentModeLowerCase(sp) + "/logs";
        logsPath.flagType = FlagType.DIR;
        logsPath.fileMode = FileMode.SHARE;

        Path defaultConfigFilePath = Path.DEFAULT_CONFIG_FILE_PATH;
        defaultConfigFilePath.path =Path.SYNC_PATH.path + "/" + currentModeLowerCase(sp) + "/default.toml";
        defaultConfigFilePath.flagType = FlagType.FILE;
        defaultConfigFilePath.fileMode = FileMode.matchMode();
    }

    @Nonnull
    private static String currentModeLowerCase(StartParameter sp) {
        return sp.getServiceMode() == StartMode.CLIENT ?
                StartMode.CLIENT.value.toLowerCase() : StartMode.SERVER.value.toLowerCase();
    }
}
