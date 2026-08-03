package org.miea04.core.config.codec;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Objects;

final class TomlWriteSupport {

    private TomlWriteSupport() {
    }

    static CommentedConfig createConfig() {
        return TomlFormat.newConfig(LinkedHashMap::new);
    }

    static void write(
            Path path,
            CommentedConfig config
    ) {
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(config, "config");

        Path target = path.toAbsolutePath().normalize();
        Path parent = target.getParent();

        try {
            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Cannot create config directory: " + parent,
                    e
            );
        }

        TomlWriter writer = new TomlWriter();

        // 禁止表中键值对缩进
        writer.setIndent("");

        writer.write(
                config,
                target,
                WritingMode.REPLACE,
                StandardCharsets.UTF_8
        );
    }
}