package org.miea04.core.config.codec;

import org.miea04.core.model.DefaultConfig;

import java.nio.file.Path;

public interface ConfigCodec<T extends DefaultConfig> {

   T read(Path path);

   void write(Path path, T config);
}