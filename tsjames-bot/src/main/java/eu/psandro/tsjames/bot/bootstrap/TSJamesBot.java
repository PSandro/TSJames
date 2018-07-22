package eu.psandro.tsjames.bot.bootstrap;

import eu.psandro.tsjames.bot.controller.CommandHandlerImpl;
import eu.psandro.tsjames.bot.controller.ConsoleIO;
import eu.psandro.tsjames.bot.model.ConfigManager;
import eu.psandro.tsjames.bot.model.DatabaseConfig;
import eu.psandro.tsjames.bot.view.Messages;
import eu.psandro.tsjames.model.DatabaseAccessData;
import eu.psandro.tsjames.model.DatabaseManager;
import eu.psandro.tsjames.model.DatabaseManagerImpl;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public final class TSJamesBot {

    @Getter
    private final ConfigManager configManager;

    private final ConsoleIO console = new ConsoleIO(System.in, System.out);

    @Getter
    private DatabaseManager databaseManager = new DatabaseManagerImpl();
    @Getter
    @Setter
    private DatabaseConfig databaseConfig = new DatabaseConfig();

    protected TSJamesBot(@NonNull ConfigManager configManager) {
        this.configManager = configManager;
        this.init();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                databaseManager.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    private void init() {
        //Console IO setup
        this.console.setCommandHandler(new CommandHandlerImpl(this));

        //Print startup...
        this.console.printLine(Messages.JAMES_ASCII_ART);
        this.console.printSpace(2);
        this.console.printLine("ConfigManager init... ");
        this.console.printLine(this.configManager.createSources() ?
                "Config Source Found! " :
                "Creating Config Source...");
        //Database Config read
        final Optional<Future<DatabaseConfig>> dbConfig = this.configManager.readConfigTo(this.databaseConfig);
        if (dbConfig.isPresent()) {
            try {
                this.databaseConfig = dbConfig.get().get(2, TimeUnit.SECONDS);
                //Database establish
                this.establishDatabaseConnection();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                this.console.printLine("Timeout while reading DatabaseConfig File");
            } catch (IOException e) {
                this.console.printLine("Could not establish database connection. Please check database config and retry. See \"db\".");
            }

        } else {
            this.console.printLine("Please configure the database settings. See \"db\".");
        }


        //Enable Console Input
        this.console.printSpace(1);
        this.console.start();
    }

    public void establishDatabaseConnection() throws IOException {
        if (this.databaseManager.isOpen()) {
            this.console.printLine("Closing database connection...");
            this.databaseManager.close();
        }
        this.console.printLine("Establishing database connection...");
        final DatabaseAccessData databaseAccessData = this.databaseConfig.toDatabaseAccesData();
        if (!databaseAccessData.isFilled()) {
            this.console.printLine("The DatabaseAccesData is incomplete! Please fill using \"db\"");
            throw new IOException("DatabaseAccessData is incomplete!");
        }
        this.databaseManager.init(databaseAccessData);
    }

    public void shutdown() {
        try {
            this.databaseManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }


}
