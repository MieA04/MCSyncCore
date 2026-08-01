package org.miea04.core.toml;

import org.junit.jupiter.api.Test;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JTomlTest
 *
 * @author MieMie
 */
class JTomlTest {

    @Test
    void parseSimpleToml() {
        TomlParseResult result = Toml.parse("""
                name = "MCSync"
                version = 21
                """);

        assertFalse(result.hasErrors(), () -> result.errors().toString());
        assertEquals("MCSync", result.getString("name"));
        assertEquals(21L, result.getLong("version"));
    }

    @Test
    void parseSectionToml() {
        TomlParseResult result = Toml.parse("""
                [sync]
                enabled = true
                mode = "CLIENT"
                path = "D:/program/Java"
                """);

        assertFalse(result.hasErrors(), () -> result.errors().toString());
        assertTrue(result.getBoolean("sync.enabled"));
        assertEquals("CLIENT", result.getString("sync.mode"));
        assertEquals("D:/program/Java", result.getString("sync.path"));
    }
}
