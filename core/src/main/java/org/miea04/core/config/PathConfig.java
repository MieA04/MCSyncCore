package org.miea04.core.config;

import org.miea04.core.StartMode;
import org.miea04.core.StartParameter;
import org.miea04.core.util.PathUtil;

import javax.annotation.Nonnull;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
        DEFAULT_CONFIG_FILE_PATH
    }

    /**
     * 一条路径的完整元信息，不可变
     */
    public record PathEntry(Path key, String path, FlagType flagType, FileMode fileMode) {

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

    /** 配置注册表：每次 init 整体重建，O(1) 查找 */
    private static final Map<Path, PathEntry> REGISTRY = new EnumMap<>(Path.class);

    private PathConfig() {
    }

    public static void init(StartParameter sp) {
        String workPath = sp.getWorkPath();
        java.nio.file.Path path = Paths.get(workPath);
        java.nio.file.Path syncDir = path.resolve("MCSyncData");
        java.nio.file.Path modeDir = syncDir.resolve(currentModeLowerCase(sp));

        EnumMap<Path, PathEntry> fresh = new EnumMap<>(Path.class);
        fresh.put(Path.WORK_PATH, new PathEntry(Path.WORK_PATH, workPath, FlagType.DIR, FileMode.SHARE));
        fresh.put(Path.SYNC_PATH, new PathEntry(Path.SYNC_PATH, syncDir.toString(), FlagType.DIR, FileMode.SHARE));
        fresh.put(Path.GAME_MODS_PATH, new PathEntry(Path.GAME_MODS_PATH, path.resolve("mods").toString(), FlagType.DIR, FileMode.SHARE));
        fresh.put(Path.CLIENT_DIR, new PathEntry(Path.CLIENT_DIR, syncDir.resolve("client").toString(), FlagType.DIR, FileMode.CLIENT));
        fresh.put(Path.SERVER_DIR, new PathEntry(Path.SERVER_DIR, syncDir.resolve("server").toString(), FlagType.DIR, FileMode.SERVER));
        fresh.put(Path.LOGS_PATH, new PathEntry(Path.LOGS_PATH, modeDir.resolve("logs").toString(), FlagType.DIR, FileMode.SHARE));
        fresh.put(Path.DEFAULT_CONFIG_FILE_PATH, new PathEntry(Path.DEFAULT_CONFIG_FILE_PATH, modeDir.resolve("default.toml").toString(), FlagType.FILE, FileMode.matchMode()));

        REGISTRY.clear();
        REGISTRY.putAll(fresh);
    }

    /**
     * 全部路径配置（不可变快照）
     */
    public static List<PathEntry> all() {
        return List.copyOf(REGISTRY.values());
    }

    /**
     * 按路径标识 O(1) 获取配置
     *
     * @throws IllegalStateException 未初始化或不存在该路径
     */
    public static PathEntry get(Path key) {
        PathEntry entry = REGISTRY.get(key);
        if (entry == null) {
            throw new IllegalStateException("未初始化的路径配置: " + key);
        }
        return entry;
    }

    @Nonnull
    private static String currentModeLowerCase(StartParameter sp) {
        return sp.getServiceMode() == StartMode.CLIENT ?
                StartMode.CLIENT.value.toLowerCase() : StartMode.SERVER.value.toLowerCase();
    }
}
