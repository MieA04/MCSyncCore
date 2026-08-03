package org.miea04.core.util;

import org.miea04.core.model.RuntimeOptions;
import org.miea04.core.StartMode;
import org.miea04.core.model.DefaultServerConfig;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/*
 * StartParameterParser
 *
 * @author MieMie
 */

/**
 * 启动参数解析器。
 */
public final class StartParameterParser {

    private StartParameterParser() {
    }

    public static RuntimeOptions parse(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Start parameter is empty");
        }

        Map<String, String> values = parseValues(input);

        Path workPath = Path.of(required(values, "WORK_PATH"));

        StartMode serviceMode = StartMode.mode(required(values, "SERVICE_MODE"));

        if (serviceMode == StartMode.NONE) {
            throw new IllegalArgumentException(
                    "Unknown SERVICE_MODE: " + values.get("SERVICE_MODE")
            );
        }

        DefaultServerConfig.NodeType nodeType = parseNodeType(values.get("NODE_TYPE"));

        String delegateHost = emptyToNull(values.get("DELEGATE_HOST"));

        validateCombination(serviceMode, nodeType, delegateHost);

        return new RuntimeOptions(
                workPath,
                serviceMode,
                nodeType,
                delegateHost
        );
    }

    private static void validateCombination(
            StartMode serviceMode,
            DefaultServerConfig.NodeType nodeType,
            String delegateHost
    ) {
        if (serviceMode != StartMode.SERVER) {
            return;
        }

        if (nodeType == null) {
            throw new IllegalArgumentException(
                    "NODE_TYPE is required in SERVER mode"
            );
        }

        if (delegateHost == null) {
            throw new IllegalArgumentException(
                    "DELEGATE_HOST is required in SERVER mode"
            );
        }
    }

    private static Map<String, String> parseValues(String input) {
        Map<String, String> values = new HashMap<>();

        for (String item : input.split("\\|")) {
            String[] pair = item.split("@", 2);

            if (pair.length != 2
                    || pair[0].isBlank()
                    || pair[1].isBlank()) {
                throw new IllegalArgumentException(
                        "Invalid start parameter: " + item
                );
            }

            String key = pair[0].trim();
            String value = pair[1].trim();

            validateKey(key);

            if (values.putIfAbsent(key, value) != null) {
                throw new IllegalArgumentException(
                        "Duplicate start parameter: " + key
                );
            }
        }

        return values;
    }

    private static void validateKey(String key) {
        switch (key) {
            case "WORK_PATH",
                 "SERVICE_MODE",
                 "NODE_TYPE",
                 "DELEGATE_HOST" -> {}
            default -> throw new IllegalArgumentException(
                    "Unknown start parameter: " + key
            );
        }
    }

    private static String required(
            Map<String, String> values,
            String key
    ) {
        String value = values.get(key);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Missing required parameter: " + key
            );
        }

        return value;
    }

    private static DefaultServerConfig.NodeType parseNodeType(
            String value
    ) {
        if (value == null || value.isBlank()) return null;

        DefaultServerConfig.NodeType type = DefaultServerConfig.NodeType.type(value);

        if (type == null) throw new IllegalArgumentException("Unknown NODE_TYPE: " + value);

        return type;
    }

    private static String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}