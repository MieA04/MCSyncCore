package org.miea04.core;

import org.miea04.core.model.DefaultServerConfig;

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
        Integer serverPort,
        String delegateHost
) {
    public RuntimeOptions {
        workPath = Objects.requireNonNull(workPath, "workPath")
                .toAbsolutePath()
                .normalize();

        Objects.requireNonNull(serviceMode, "serviceMode");

        if (serviceMode == StartMode.NONE) {
            throw new IllegalArgumentException("serviceMode cannot be NONE");
        }

        if (serverPort != null && (serverPort < 1 || serverPort > 65535)) {
            throw new IllegalArgumentException(
                    "serverPort must be between 1 and 65535"
            );
        }
    }
}
