package eu.psandro.tsjames.bot.query;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import eu.psandro.tsjames.bot.controller.ConsoleIO;
import eu.psandro.tsjames.bot.io.ManagedConnection;
import eu.psandro.tsjames.bot.model.ConfigManager;
import eu.psandro.tsjames.bot.model.TeamSpeakConfig;
import eu.psandro.tsjames.bot.query.command.CommandManager;
import eu.psandro.tsjames.bot.query.command.PingCommand;
import eu.psandro.tsjames.model.PermissionFetcher;
import lombok.Getter;
import lombok.NonNull;
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
public final class TeamSpeakConnection extends ManagedConnection {

    @Getter
    @Setter
    private TeamSpeakConfig teamSpeakConfig = new TeamSpeakConfig();

    private final ConsoleIO console;
    private final ConfigManager configManager;
    private final PermissionFetcher permissionFetcher;

    private TS3Query ts3Query;
    private TS3Api ts3Api;

    private TeamSpeakActionHandler actionHandler;

    public TeamSpeakConnection(@NonNull ConsoleIO consoleIO, @NonNull ConfigManager configManager, @NonNull PermissionFetcher permissionFetcher) {
        this.console = consoleIO;
        this.configManager = configManager;
        this.permissionFetcher = permissionFetcher;
    }


    @Override
    public boolean init() {
        final Optional<Future<TeamSpeakConfig>> tsConfig = this.configManager.readConfigTo(this.teamSpeakConfig);
        if (tsConfig.isPresent()) {
            try {
                this.teamSpeakConfig = tsConfig.get().get(2, TimeUnit.SECONDS);
                final TS3Config ts3Config = new TS3Config().setHost(this.teamSpeakConfig.getHost()).setQueryPort(this.teamSpeakConfig.getPort());
                this.ts3Query = new TS3Query(ts3Config);
                return true;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                this.console.printLine("Timeout while reading TeamSpeakConfig File");
            }

        } else {
            this.console.printLine("Please configure the teamspeak settings. See \"ts\".");
            return false;
        }
        return false;
    }

    @Override
    public boolean establish() throws IOException {

        if (this.isRunning()) {
            this.console.printLine("Closing TeamSpeakConnection...");
            this.close();
        }

        if (this.ts3Query == null) {
            this.console.printLine("Initializing...");
            if (!this.init()) return false;
        }
        this.console.printLine("Establishing TeamSpeakConnection...");
        this.ts3Query.connect();
        this.ts3Api = this.ts3Query.getApi();
        this.ts3Api.login(this.teamSpeakConfig.getUsername(), this.teamSpeakConfig.getPassword());
        this.ts3Api.selectVirtualServerById(this.teamSpeakConfig.getVServerId());
        this.ts3Api.setNickname(this.teamSpeakConfig.getNickname());
        this.console.printLine("TeamSpeakConnection established! Enabling now...");
        this.initActionHandler();
        this.enableActionHandling();
        this.console.printLine("TeamSpeak action handling enabled!");
        //this.ts3Api.sendServerMessage(Messages.JAMES_WELCOME);
        return true;
    }

    private void initActionHandler() {
        if (this.ts3Api == null) return;
        if (this.actionHandler == null) {
            final CommandManager commandManager = new CommandManager();
            commandManager.registerCommand(new PingCommand(this.permissionFetcher));
            this.actionHandler = new TeamSpeakActionHandler(this.ts3Api, commandManager, false);
            this.ts3Api.registerAllEvents();
            this.ts3Api.addTS3Listeners(this.actionHandler);
        }
    }

    private void enableActionHandling() {
        if (this.actionHandler == null) return;
        this.actionHandler.enable();
    }

    private void disableActionHandling() {
        if (this.actionHandler == null) return;
        this.actionHandler.disable();
    }


    @Override
    public boolean isRunning() {
        return this.ts3Query != null && this.ts3Query.isConnected();
    }

    @Override
    public void close() throws IOException {
        this.disableActionHandling();
        this.actionHandler = null;
        if (this.ts3Api != null) {
            //this.ts3Api.sendServerMessage(Messages.JAMES_QUIT);
            this.ts3Api.logout();
            this.ts3Api = null;
        }
        if (this.ts3Query != null) {
            this.ts3Query.exit();
            this.ts3Query = null;
        }
    }
}
