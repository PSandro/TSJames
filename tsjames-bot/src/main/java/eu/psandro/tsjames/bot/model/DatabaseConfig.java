package eu.psandro.tsjames.bot.model;

import com.google.gson.JsonObject;
import eu.psandro.tsjames.model.DatabaseAccessData;
import lombok.Getter;
import lombok.Setter;

public final class DatabaseConfig extends ConfigFile {

    @Getter
    @Setter
    private String url, username, password;

    public DatabaseConfig() {
        super(DatabaseConfig.class.getSimpleName());
    }

    public void setUrl(String host, int port, String database) {
        this.url = String.format("jdbc:mysql://%s:%d/%s?autoReconnect=true&useSSL=false", host, port, database);
    }

    @Override
    public void read(JsonObject jsonObject) {
        if (jsonObject.has("url")) {
            this.url = jsonObject.get("url").getAsString();
        }
        if (jsonObject.has("username")) {
            this.username = jsonObject.get("username").getAsString();
        }
        if (jsonObject.has("password")) {
            this.password = jsonObject.get("password").getAsString();
        }
    }

    @Override
    public JsonObject pack() {
        final JsonObject json = new JsonObject();
        json.addProperty("url", this.url);
        json.addProperty("username", this.username);
        json.addProperty("password", this.password);
        return json;
    }

    public DatabaseAccessData toDatabaseAccesData() {
        return new DatabaseAccessData(this.url, this.username, this.password);
    }

}
