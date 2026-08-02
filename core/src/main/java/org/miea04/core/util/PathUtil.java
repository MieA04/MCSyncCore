package org.miea04.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * PathUtil
 *
 * @author MieMie
 */
public final class PathUtil {
    private static final Logger log = LoggerFactory.getLogger(PathUtil.class);

    private PathUtil() {
    }

    /**
     * 原子创建目录，已存在则忽略
     *
     * @param path 目录路径
     */
    public static void createDirectoriesQuietly(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new IllegalStateException("目录创建失败: " + path, e);
        }
    }

    /**
     * 原子创建文件，已存在返回 true（表示创建成功，然后记录日志）
     *
     * @param path 文件路径
     * @return 是否创建成功
     */
    public static boolean createFileIfAbsent(Path path) {
        try {
            Files.createFile(path);
            return true;
        } catch (FileAlreadyExistsException e) {
            log.info("File already exists: {}", path);
            return true;
        } catch (NoSuchFileException e) {
            throw new IllegalStateException("父目录不存在: " + path, e);
        } catch (IOException e) {
            throw new IllegalStateException("文件创建失败: " + path, e);
        }
    }
}
