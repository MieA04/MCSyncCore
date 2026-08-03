package org.miea04.core.tasks.parameter;

import java.nio.file.Path;
import java.util.Objects;

/**
 * PathParams
 *
 * @author MieMie
 */
public record PathParams(Path path) implements TaskParams {
    public PathParams {
        Objects.requireNonNull(path, "path");
    }
}