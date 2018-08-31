package eu.psandro.tsjames.model.file;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author PSandro on 31.08.18
 * @project tsjames
 */

@Getter
@Setter
public final class NetClientConfig extends ConfigFile {

    @NonNull
    private String host, user, key;

    @NonNull
    private int port;

    public NetClientConfig() {
        super(NetClientConfig.class.getSimpleName());
    }


    @Override
    public void read(JsonObject jsonObject) {
        if (jsonObject.has("host")) {
            this.host = jsonObject.get("host").getAsString();
        }
        if (jsonObject.has("user")) {
            this.user = jsonObject.get("user").getAsString();
        }
        if (jsonObject.has("key")) {
            this.key = jsonObject.get("key").getAsString();
        }
        if (jsonObject.has("port")) {
            this.port = jsonObject.get("port").getAsInt();
        }
    }

    @Override
    public JsonObject pack() {
        final JsonObject json = new JsonObject();
        json.addProperty("host", this.host);
        json.addProperty("user", this.user);
        json.addProperty("key", this.key);
        json.addProperty("port", this.port);
        return json;
    }
}
