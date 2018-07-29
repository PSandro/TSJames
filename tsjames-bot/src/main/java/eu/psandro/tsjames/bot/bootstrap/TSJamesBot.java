package eu.psandro.tsjames.bot.bootstrap;

import eu.psandro.tsjames.bot.controller.CommandHandlerImpl;
import eu.psandro.tsjames.bot.controller.ConsoleIO;
import eu.psandro.tsjames.bot.io.ManagedConnection;
import eu.psandro.tsjames.bot.model.ConfigManager;
import eu.psandro.tsjames.bot.model.DatabaseConnection;
import eu.psandro.tsjames.bot.query.TeamSpeakConnection;
import eu.psandro.tsjames.bot.view.Messages;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;


public final class TSJamesBot {

    @Getter
    private final ConfigManager configManager;

    private final ConsoleIO console = new ConsoleIO(System.in, System.out);

    @Getter
    private final ManagedConnection databaseConnection, teamSpeakConnection;


    protected TSJamesBot(@NonNull ConfigManager configManager) throws IOException {
        this.configManager = configManager;
        this.databaseConnection = new DatabaseConnection(console, configManager);
        this.teamSpeakConnection = new TeamSpeakConnection(console, configManager, ((DatabaseConnection) this.databaseConnection).getDatabaseManager().getPermissionFetcher());
        this.init();
        this.postInit();

        Runtime.getRuntime().addShutdownHook(new Thread(this::prepareShutdown));
    }


    private void init() throws IOException {
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
        if (!this.databaseConnection.init() || !this.databaseConnection.establish()) {
            this.console.printLine("Skipping further initialisation until db connection is established.");
            return;
        }
        if (!this.teamSpeakConnection.init()) {
            return;
        }
        this.teamSpeakConnection.establish();


    }

    private void postInit() {
        //Enable Console Input
        this.console.printSpace(1);
        this.console.start();
    }

    public void prepareShutdown() {
        this.console.printLine("bye...");
        try {
            this.databaseConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        this.prepareShutdown();
        System.exit(0);
    }


}
