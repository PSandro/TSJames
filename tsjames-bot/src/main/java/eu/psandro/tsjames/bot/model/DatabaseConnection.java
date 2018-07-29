package eu.psandro.tsjames.bot.model;

import eu.psandro.tsjames.bot.controller.ConsoleIO;
import eu.psandro.tsjames.bot.io.ManagedConnection;
import eu.psandro.tsjames.model.DatabaseAccessData;
import eu.psandro.tsjames.model.DatabaseManager;
import eu.psandro.tsjames.model.DatabaseManagerImpl;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public final class DatabaseConnection extends ManagedConnection {

    @Getter
    private DatabaseManager databaseManager = new DatabaseManagerImpl();

    @Getter
    @Setter
    private DatabaseConfig databaseConfig = new DatabaseConfig();

    private final ConsoleIO console;
    private final ConfigManager configManager;

    public DatabaseConnection(ConsoleIO consoleIO, ConfigManager configManager) {
        this.console = consoleIO;
        this.configManager = configManager;
    }

    @Override
    public boolean init() {
        final Optional<Future<DatabaseConfig>> dbConfig = this.configManager.readConfigTo(this.databaseConfig);
        if (dbConfig.isPresent()) {
            try {
                this.databaseConfig = dbConfig.get().get(2, TimeUnit.SECONDS);
                return true;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                this.console.printLine("Timeout while reading DatabaseConfig File");
            }

        } else {
            this.console.printLine("Please configure the database settings. See \"db\".");
            return false;
        }
        return false;
    }

    @Override
    public boolean establish() throws IOException {
        if (this.databaseManager.isOpen()) {
            this.console.printLine("Closing database connection...");
            this.databaseManager.close();
        }
        this.console.printLine("Establishing database connection...");
        final DatabaseAccessData databaseAccessData = this.databaseConfig.toDatabaseAccesData();
        if (!databaseAccessData.isFilled()) {
            this.console.printLine("The DatabaseAccesData is incomplete! Please fill using \"db\"");
            return false;
        }
        this.databaseManager.init(databaseAccessData);
        return true;
    }

    @Override
    public boolean isRunning() {
        return this.databaseManager.isOpen();
    }

    @Override
    public void close() throws IOException {
        this.databaseManager.close();
    }
}
