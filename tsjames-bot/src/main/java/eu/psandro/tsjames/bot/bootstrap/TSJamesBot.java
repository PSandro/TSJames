package eu.psandro.tsjames.bot.bootstrap;

import eu.psandro.tsjames.bootstrap.JamesConsoleBootstrap;
import eu.psandro.tsjames.controller.console.command.impl.CommandDB;
import eu.psandro.tsjames.bot.controller.command.CommandTS;
import eu.psandro.tsjames.controller.console.command.impl.CommandNetClient;
import eu.psandro.tsjames.io.AbstractNetClient;
import eu.psandro.tsjames.io.NetClientImpl;
import eu.psandro.tsjames.model.file.NetClientConfig;
import eu.psandro.tsjames.io.ManagedConnection;
import eu.psandro.tsjames.model.file.ConfigLoader;
import eu.psandro.tsjames.model.file.ConfigManager;
import eu.psandro.tsjames.model.database.DatabaseConnection;
import eu.psandro.tsjames.bot.query.TeamSpeakConnection;
import lombok.Getter;
import lombok.NonNull;


public final class TSJamesBot extends JamesConsoleBootstrap {

    @Getter
    private final ConfigManager configManager;

    @Getter
    private final ManagedConnection databaseConnection, teamSpeakConnection;
    final ConfigLoader<NetClientConfig> clientConfig;

    @Getter
    private NetClientImpl netClient = new NetClientImpl();


    protected TSJamesBot(@NonNull ConfigManager configManager) throws Exception {
        this.configManager = configManager;
        this.databaseConnection = new DatabaseConnection(super.getConsole(), configManager);
        this.teamSpeakConnection = new TeamSpeakConnection(super.getConsole(), configManager);
        this.clientConfig = new ConfigLoader<>(NetClientConfig.class, this.configManager);

        Runtime.getRuntime().addShutdownHook(new Thread(super::prepareShutdown));
    }


    @Override
    public boolean init() {
        super.init();
        super.getConsole().getCommandHandler()
                .registerCommand("net", new CommandNetClient(this.configManager, this.clientConfig.get(), this.netClient))
                .registerCommand("db", new CommandDB((DatabaseConnection) this.databaseConnection, this.configManager))
                .registerCommand("ts", new CommandTS(this));
        try {
            super.getConsole().printLine("ConfigManager init... ");
            super.getConsole().printLine(this.configManager.createSources() ?
                    "Config Source Found! " :
                    "Creating Config Source...");
            //Database Config read
            /**
            if (this.databaseConnection.init()) {
                this.databaseConnection.establish();
            } else {
                super.getConsole().printLine("Please configure Database -> see \"db\"");
            }
             **/
            //NetClient
            if (this.netClient.init()) {
                if (!clientConfig.load()) {
                    super.getConsole().printLine("Please configure NetClient -> see \"net\"");
                } else {
                    this.netClient.fillConnectionData(this.clientConfig.get());
                    this.netClient.establish();
                }
            } else {
                super.getConsole().printLine("Failed initializing NetClient");
            }
            if (this.teamSpeakConnection.init()) {
                this.teamSpeakConnection.establish();
            } else {
                super.getConsole().printLine("Please configure TeamSpeak -> see \"ts\"");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


}
