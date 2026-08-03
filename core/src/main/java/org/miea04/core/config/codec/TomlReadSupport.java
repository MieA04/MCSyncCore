package org.miea04.core.config.codec;

import com.electronwill.nightconfig.core.Config;

import java.util.ArrayList;
import java.util.List;

final class TomlReadSupport {

    static final int SCHEMA_VERSION = 1;

    private TomlReadSupport() {
    }

    static void validateSchema(Config config) {
        Object rawValue = config.get("schemaVersion");

        if (!(rawValue instanceof Number number)) {
            throw new IllegalStateException(
                    "Missing or invalid schemaVersion"
            );
        }

        int version = number.intValue();

        if (version != SCHEMA_VERSION) {
            throw new IllegalStateException(
                    "Unsupported config schemaVersion: " + version
            );
        }
    }

    static String requireString(
            Config config,
            String path
    ) {
        Object value = config.get(path);

        if (!(value instanceof String stringValue)) {
            throw new IllegalStateException(
                    "Missing or invalid string config: " + path
            );
        }

        return stringValue;
    }

    static Config requireTable(
            Config config,
            String path
    ) {
        Object value = config.get(path);

        if (!(value instanceof Config table)) {
            throw new IllegalStateException(
                    "Missing or invalid config table: " + path
            );
        }

        return table;
    }

    static List<Config> requireTableList(
            Config config,
            String path
    ) {
        Object value = config.get(path);

        if (!(value instanceof List<?> list)) {
            throw new IllegalStateException(
                    "Missing or invalid config table list: " + path
            );
        }

        List<Config> result = new ArrayList<>(list.size());

        for (int index = 0; index < list.size(); index++) {
            Object element = list.get(index);

            if (!(element instanceof Config table)) {
                throw new IllegalStateException(
                        "Invalid table at " + path + "[" + index + "]"
                );
            }

            result.add(table);
        }

        return result;
    }
}