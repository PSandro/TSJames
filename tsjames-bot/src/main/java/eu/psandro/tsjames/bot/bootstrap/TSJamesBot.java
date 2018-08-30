package eu.psandro.tsjames.bot.bootstrap;

import eu.psandro.tsjames.bootstrap.JamesConsoleBootstrap;
import eu.psandro.tsjames.bot.controller.command.CommandDB;
import eu.psandro.tsjames.bot.controller.command.CommandTS;
import eu.psandro.tsjames.io.ManagedConnection;
import eu.psandro.tsjames.model.file.ConfigManager;
import eu.psandro.tsjames.model.database.DatabaseConnection;
import eu.psandro.tsjames.bot.query.TeamSpeakConnection;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;


public final class TSJamesBot extends JamesConsoleBootstrap {

    @Getter
    private final ConfigManager configManager;

    @Getter
    private final ManagedConnection databaseConnection, teamSpeakConnection;


    protected TSJamesBot(@NonNull ConfigManager configManager) {
        this.configManager = configManager;
        this.databaseConnection = new DatabaseConnection(super.getConsole(), configManager);
        this.teamSpeakConnection = new TeamSpeakConnection(super.getConsole(), configManager);

        Runtime.getRuntime().addShutdownHook(new Thread(super::prepareShutdown));
    }


    @Override
    public boolean init() {
        super.init();
        try {
            super.getConsole().printLine("ConfigManager init... ");
            super.getConsole().printLine(this.configManager.createSources() ?
                    "Config Source Found! " :
                    "Creating Config Source...");
            //Database Config read
            if (!this.databaseConnection.init() || !this.databaseConnection.establish()) {
                super.getConsole().printLine("Skipping further initialisation until db connection is established.");
                return true;
            }

            if (!this.teamSpeakConnection.init()) {
                return true;
            }
            this.teamSpeakConnection.establish();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        super.getConsole().getCommandHandler()
                .registerCommand("db", new CommandDB(this))
                .registerCommand("ts", new CommandTS(this));


        return true;
    }


}
