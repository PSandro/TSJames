package eu.psandro.tsjames.bot.model;


import java.util.Optional;
import java.util.concurrent.Future;

public abstract class ConfigManager {

    public abstract boolean createSources();

    public abstract boolean existsConfig(ConfigFile configFile);

    public abstract void saveConfig(ConfigFile configFile);

    public abstract <T extends ConfigFile> Optional<Future<T>> readConfigTo(T configFile);

}
