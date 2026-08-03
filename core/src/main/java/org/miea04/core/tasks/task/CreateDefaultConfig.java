package org.miea04.core.tasks.task;

import org.miea04.core.config.Config;
import org.miea04.core.config.repository.DefaultConfigRepository;
import org.miea04.core.model.DefaultConfig;
import org.miea04.core.model.SyncDefaultClientConfig;
import org.miea04.core.model.SyncDefaultServerConfig;
import org.miea04.core.tasks.parameter.PathParams;

public final class CreateDefaultConfig implements Task<PathParams> {

    private final DefaultConfigRepository repository = new DefaultConfigRepository();

    @Override
    public Class<Void> start(PathParams params) {
        DefaultConfig defaultConfig = createDefaultConfigTemplate();

        repository.save(params.path(), defaultConfig);

        return null;
    }

    private DefaultConfig createDefaultConfigTemplate() {
        return switch (Config.getStartMode()) {
            case CLIENT ->
                    SyncDefaultClientConfig.createDefault();

            case SERVER ->
                    SyncDefaultServerConfig.createDefault();

            case NONE -> throw new IllegalStateException(
                    "Cannot create config in NONE mode"
            );
        };
    }
}