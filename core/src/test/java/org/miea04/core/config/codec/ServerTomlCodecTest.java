package org.miea04.core.config.codec;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.miea04.core.model.DefaultServerConfig;
import org.miea04.core.model.SyncDefaultServerConfig;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ServerTomlCodecTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldWriteAndReadServerConfig() throws Exception {
        Path configPath = tempDir.resolve("server.toml");

        DefaultServerConfig server =
                new DefaultServerConfig();

        server.setNodeId("node-test-id");
        server.setNodeName("Singapore Node");
        server.setNodeType(
                DefaultServerConfig.NodeType.COMPLETE
        );
        server.setDelegatedServerId("delegate-test-id");
        server.setDelegatedServerHost(
                "127.0.0.1:2814"
        );

        SyncDefaultServerConfig original =
                new SyncDefaultServerConfig();

        original.setTable(server);

        ServerTomlCodec codec =
                new ServerTomlCodec();

        codec.write(configPath, original);

        assertTrue(
                Files.isRegularFile(configPath),
                "Codec 应创建 TOML 文件"
        );

        assertTrue(
                Files.size(configPath) > 0,
                "生成的 TOML 文件不应为空"
        );

        SyncDefaultServerConfig loaded =
                codec.read(configPath);

        assertNotNull(loaded);
        assertNotNull(loaded.getTable());

        DefaultServerConfig loadedServer =
                loaded.getTable();

        assertEquals(
                server.getNodeId(),
                loadedServer.getNodeId()
        );

        assertEquals(
                server.getNodeName(),
                loadedServer.getNodeName()
        );

        assertEquals(
                server.getNodeType(),
                loadedServer.getNodeType()
        );

        assertEquals(
                server.getDelegatedServerId(),
                loadedServer.getDelegatedServerId()
        );

        assertEquals(
                server.getDelegatedServerHost(),
                loadedServer.getDelegatedServerHost()
        );
    }
}