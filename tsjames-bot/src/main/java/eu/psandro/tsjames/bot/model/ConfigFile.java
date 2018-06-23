package eu.psandro.tsjames.bot.model;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class ConfigFile {

    @Getter
    private final String indicator;

    public abstract void read(JsonObject jsonObject);

    public abstract JsonObject pack();
}
