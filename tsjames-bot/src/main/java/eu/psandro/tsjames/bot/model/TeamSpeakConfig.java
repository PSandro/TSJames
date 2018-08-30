package eu.psandro.tsjames.bot.model;

import com.google.gson.JsonObject;
import eu.psandro.tsjames.model.file.ConfigFile;
import lombok.Getter;
import lombok.Setter;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public final class TeamSpeakConfig extends ConfigFile {

    @Getter
    @Setter
    private String host, username, password, nickname;
    @Getter
    @Setter
    private int port, vServerId;

    public TeamSpeakConfig() {
        super(TeamSpeakConfig.class.getSimpleName());
    }


    @Override
    public void read(JsonObject jsonObject) {
        if (jsonObject.has("host")) {
            this.host = jsonObject.get("host").getAsString();
        }
        if (jsonObject.has("username")) {
            this.username = jsonObject.get("username").getAsString();
        }
        if (jsonObject.has("password")) {
            this.password = jsonObject.get("password").getAsString();
        }
        if (jsonObject.has("nickname")) {
            this.nickname = jsonObject.get("nickname").getAsString();
        }
        if (jsonObject.has("port")) {
            this.port = jsonObject.get("port").getAsInt();
        }
        if (jsonObject.has("vId")) {
            this.vServerId = jsonObject.get("vId").getAsInt();
        }
    }

    @Override
    public JsonObject pack() {
        final JsonObject json = new JsonObject();
        json.addProperty("host", this.host);
        json.addProperty("username", this.username);
        json.addProperty("password", this.password);
        json.addProperty("nickname", this.nickname);
        json.addProperty("port", this.port);
        json.addProperty("vId", this.vServerId);
        return json;
    }

}
