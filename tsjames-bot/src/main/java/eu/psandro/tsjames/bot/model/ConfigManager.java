package eu.psandro.tsjames.bot.model;


import java.util.concurrent.Future;

public abstract class ConfigManager {


    public abstract void saveConfig(ConfigFile configFile);

    public abstract <T extends ConfigFile> Future<T> readConfigTo(T configFile);

}
