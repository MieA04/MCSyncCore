package org.miea04.core.config.writer;

import org.miea04.core.model.DefaultConfig;

import java.nio.file.Path;

/**
 * TomlConfigWriter
 *
 * @author MieMie
 */
public interface TomlConfigWriter<T extends DefaultConfig> {
    void write(Path path, T config);
}
