package org.miea04.core.config.codec;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.miea04.core.model.DefaultClientConfig;
import org.miea04.core.model.SyncDefaultClientConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientTomlCodecTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldWriteAndReadClientConfig() throws Exception {
        Path configPath = tempDir.resolve("client.toml");

        DefaultClientConfig firstServer = new DefaultClientConfig();
        firstServer.setHostName("Main Server");
        firstServer.setGameServerHost("game.example.com:25565");
        firstServer.setDelegatedServerHost("sync.example.com:2814");

        DefaultClientConfig secondServer = new DefaultClientConfig();
        secondServer.setHostName("Backup Server");
        secondServer.setGameServerHost("backup.example.com:25565");
        secondServer.setDelegatedServerHost("backup-sync.example.com:2814");

        SyncDefaultClientConfig original =
                new SyncDefaultClientConfig();

        original.setClientId("client-test-id");
        original.setTable(
                List.of(firstServer, secondServer)
        );

        ClientTomlCodec codec = new ClientTomlCodec();

        codec.write(configPath, original);

        assertTrue(
                Files.isRegularFile(configPath),
                "Codec 应创建 TOML 文件"
        );

        assertTrue(
                Files.size(configPath) > 0,
                "生成的 TOML 文件不应为空"
        );

        SyncDefaultClientConfig loaded =
                codec.read(configPath);

        assertNotNull(loaded);
        assertEquals(
                original.getClientId(),
                loaded.getClientId()
        );

        assertNotNull(loaded.getTable());
        assertEquals(2, loaded.getTable().size());

        assertClientServerEquals(
                firstServer,
                loaded.getTable().get(0)
        );

        assertClientServerEquals(
                secondServer,
                loaded.getTable().get(1)
        );
    }

    private static void assertClientServerEquals(
            DefaultClientConfig expected,
            DefaultClientConfig actual
    ) {
        assertNotNull(actual);

        assertEquals(
                expected.getHostName(),
                actual.getHostName()
        );

        assertEquals(
                expected.getGameServerHost(),
                actual.getGameServerHost()
        );

        assertEquals(
                expected.getDelegatedServerHost(),
                actual.getDelegatedServerHost()
        );
    }
}