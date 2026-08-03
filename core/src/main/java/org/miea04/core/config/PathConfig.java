package org.miea04.core.config;

import org.miea04.core.RuntimeOptions;
import org.miea04.core.StartMode;
import org.miea04.core.util.PathUtil;

import java.nio.file.Files;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*
 * PathConfig
 *
 * @author MieMie
 */

/**
 * 当前运行实例的路径注册表。
 */
public final class PathConfig {

    public enum FlagType {
        DIR,
        FILE
    }

    public enum FileMode {
        CLIENT,
        SERVER,
        SHARE;

        public static FileMode from(StartMode mode) {
            return switch (mode) {
                case CLIENT -> CLIENT;
                case SERVER -> SERVER;
                case NONE -> throw new IllegalArgumentException(
                        "Unsupported start mode: " + mode
                );
            };
        }

        public boolean matches(StartMode mode) {
            return this == SHARE
                    || (this == CLIENT && mode == StartMode.CLIENT)
                    || (this == SERVER && mode == StartMode.SERVER);
        }
    }

    public enum PathKey {
        WORK_PATH,
        SYNC_PATH,
        GAME_MODS_PATH,
        LOGS_PATH,
        CLIENT_DIR,
        SERVER_DIR,
        DEFAULT_CONFIG_FILE_PATH
    }

    public record PathEntry(
            PathKey key,
            java.nio.file.Path path,
            FlagType flagType,
            FileMode fileMode
    ) {

        public boolean needHandle(StartMode currentMode) {
            return fileMode.matches(currentMode)
                    && Files.notExists(path);
        }

        public void createIfMissing() {
            if (flagType == FlagType.DIR) {
                PathUtil.createDirectoriesQuietly(path);
            }
        }
    }

    private static final Map<PathKey, PathEntry> REGISTRY =
            new EnumMap<>(PathKey.class);

    private PathConfig() {
    }

    public static void init(RuntimeOptions options) {
        java.nio.file.Path workPath = options.workPath();
        java.nio.file.Path syncDir =
                workPath.resolve("MCSyncData");

        String modeName = options.serviceMode()
                .name()
                .toLowerCase(Locale.ROOT);

        java.nio.file.Path modeDir =
                syncDir.resolve(modeName);

        EnumMap<PathKey, PathEntry> fresh =
                new EnumMap<>(PathKey.class);

        register(
                fresh,
                PathKey.WORK_PATH,
                workPath,
                FlagType.DIR,
                FileMode.SHARE
        );

        register(
                fresh,
                PathKey.SYNC_PATH,
                syncDir,
                FlagType.DIR,
                FileMode.SHARE
        );

        register(
                fresh,
                PathKey.GAME_MODS_PATH,
                workPath.resolve("mods"),
                FlagType.DIR,
                FileMode.SHARE
        );

        register(
                fresh,
                PathKey.CLIENT_DIR,
                syncDir.resolve("client"),
                FlagType.DIR,
                FileMode.CLIENT
        );

        register(
                fresh,
                PathKey.SERVER_DIR,
                syncDir.resolve("server"),
                FlagType.DIR,
                FileMode.SERVER
        );

        register(
                fresh,
                PathKey.LOGS_PATH,
                modeDir.resolve("logs"),
                FlagType.DIR,
                FileMode.SHARE
        );

        register(
                fresh,
                PathKey.DEFAULT_CONFIG_FILE_PATH,
                modeDir.resolve("default.toml"),
                FlagType.FILE,
                FileMode.from(options.serviceMode())
        );

        REGISTRY.clear();
        REGISTRY.putAll(fresh);
    }

    private static void register(
            Map<PathKey, PathEntry> registry,
            PathKey key,
            java.nio.file.Path path,
            FlagType flagType,
            FileMode fileMode
    ) {
        registry.put(
                key,
                new PathEntry(key, path, flagType, fileMode)
        );
    }

    public static List<PathEntry> all() {
        return List.copyOf(REGISTRY.values());
    }

    public static PathEntry get(PathKey key) {
        PathEntry entry = REGISTRY.get(key);

        if (entry == null) {
            throw new IllegalStateException(
                    "Uninitialized path: " + key
            );
        }

        return entry;
    }
}