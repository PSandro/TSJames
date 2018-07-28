package eu.psandro.tsjames.bot.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public final class FileConfigManager extends ConfigManager {

    private static final File FOLDER = new File("config");
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private final JsonParser parser = new JsonParser();


    public FileConfigManager() {
    }

    @Override
    public boolean createSources() {
        if (!FOLDER.exists()) {
            this.executor.execute(() -> FOLDER.mkdir());
            return false;
        }
        return true;
    }

    @Override
    public boolean existsConfig(ConfigFile configFile) {
        return this.getPath(configFile).toFile().exists();
    }


    @Override
    public synchronized void saveConfig(ConfigFile configFile) {
        this.executor.execute(() -> {
            final Path path = FileConfigManager.this.getPath(configFile);
            final String rawData = configFile.pack().toString();
            try {
                Files.write(path, rawData.getBytes(CHARSET));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public synchronized <T extends ConfigFile> Optional<Future<T>> readConfigTo(T configFile) {
        final Path path = FileConfigManager.this.getPath(configFile);
        if (!path.toFile().exists()) {
            return Optional.empty();
        }

        return Optional.of(this.executor.submit(() -> {

            final String rawData = new String(Files.readAllBytes(path), CHARSET);
            final JsonObject jsonObject = FileConfigManager.this.parser.parse(rawData).getAsJsonObject();
            configFile.read(jsonObject);
            return configFile;
        }));
    }


    private Path getPath(ConfigFile configFile) {
        final String fileName = configFile.getIndicator().concat(".json");
        return new File(FOLDER, fileName).toPath();
    }


}
