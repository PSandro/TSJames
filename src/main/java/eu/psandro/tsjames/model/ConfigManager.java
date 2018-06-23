package eu.psandro.tsjames.model;


import java.util.Optional;
import java.util.concurrent.Future;

public abstract class ConfigManager {


    public abstract void saveConfig(ConfigFile configFile);

    public abstract <T extends ConfigFile> Future<T> readConfigTo(T configFile);

}
