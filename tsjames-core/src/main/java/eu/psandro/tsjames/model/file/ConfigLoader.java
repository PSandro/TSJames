package eu.psandro.tsjames.model.file;

import eu.psandro.tsjames.api.exception.JamesException;
import lombok.NonNull;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

/**
 * @author PSandro on 31.08.18
 * @project tsjames
 */
public class ConfigLoader<T extends ConfigFile> {

    @NonNull
    private final Class<T> configClazz;
    @NonNull
    private final ConfigManager configManager;

    private final T config;


    public ConfigLoader(Class<T> configClazz, ConfigManager configManager) throws Exception {
        if (!this.hasParameterlessConstructor(configClazz))
            throw new JamesException("ConfigFile needs to have a parameterless constructor for the ConfigLoader!");
        this.configClazz = configClazz;
        this.configManager = configManager;
        this.config = configClazz.getConstructor().newInstance();
    }

    public boolean load() throws InterruptedException, ExecutionException, TimeoutException {
        final Optional<Future<T>> config = this.configManager.readConfigTo(this.config);
        if (config.isPresent()) {
            config.get().get(2, TimeUnit.SECONDS);
            return true;
        } else {
            return false;
        }
    }

    public void loadAsync() {
        this.configManager.readConfigTo(this.config);
    }

    public final T get() {
        return this.config;
    }


    private boolean hasParameterlessConstructor(Class<?> clazz) {
        return Stream.of(clazz.getConstructors())
                .anyMatch((c) -> c.getParameterCount() == 0);
    }
}
