package org.miea04.core.config;

import org.miea04.core.model.RuntimeOptions;
import org.miea04.core.StartMode;
import org.miea04.core.model.DefaultServerConfig;
import java.util.Objects;

/**
 * Config
 *
 * @author MieMie
 */
public final class Config {

    private static RuntimeOptions runtimeOptions;

    private Config() {}

    public static void init(RuntimeOptions options) {
        runtimeOptions = Objects.requireNonNull(
                options,
                "runtimeOptions"
        );
    }

    public static RuntimeOptions getRuntimeOptions() {
        if (runtimeOptions == null) {
            throw new IllegalStateException(
                    "Runtime options have not been initialized"
            );
        }

        return runtimeOptions;
    }

    public static StartMode getStartMode() {
        return getRuntimeOptions().serviceMode();
    }

    public static DefaultServerConfig.NodeType getNodeType() {
        return getRuntimeOptions().nodeType();
    }

    public static String getDelegateHost() {
        return getRuntimeOptions().delegateHost();
    }
}
