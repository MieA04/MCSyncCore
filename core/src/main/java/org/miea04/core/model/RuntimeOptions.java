package org.miea04.core.model;

import org.miea04.core.StartMode;

import java.nio.file.Path;
import java.util.Objects;

/*
 * RuntimeOptions
 *
 * @author MieMie
 */

/**
 * 单次启动的只读运行参数。
 */
public record RuntimeOptions(
        Path workPath,
        StartMode serviceMode,
        DefaultServerConfig.NodeType nodeType,
        String delegateHost
) {
    public RuntimeOptions {
        workPath = Objects.requireNonNull(workPath, "workPath")
                .toAbsolutePath()
                .normalize();

        Objects.requireNonNull(serviceMode, "serviceMode");

        if (serviceMode == StartMode.NONE) {
            throw new IllegalArgumentException(
                    "serviceMode cannot be NONE"
            );
        }
    }
}
