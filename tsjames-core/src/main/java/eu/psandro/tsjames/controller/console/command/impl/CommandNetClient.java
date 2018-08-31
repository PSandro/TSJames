package eu.psandro.tsjames.controller.console.command.impl;

import eu.psandro.tsjames.controller.console.command.Command;
import eu.psandro.tsjames.io.NetClientImpl;
import eu.psandro.tsjames.model.file.ConfigManager;
import eu.psandro.tsjames.model.file.NetClientConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author PSandro on 31.08.18
 * @project tsjames
 */
@RequiredArgsConstructor
public final class CommandNetClient extends Command {
    private static final String OPTIONS = "Commands:\n " + String.join("\n ",
            "set <user|key|host|port> <value>",
            "info - display the current settings",
            "status - get the status of the connection",
            "connect - (re)establish the connection",
            "save - save the configuration to the file",
            "load - load the configuration from the file") + "\n";

    @NonNull
    private final ConfigManager configManager;
    @NonNull
    private final NetClientConfig config;
    @NonNull
    private final NetClientImpl connection;


    @Override
    public String handleCommand(String[] args) {
        if (args.length <= 0) {
            return OPTIONS;
        } else {
            final String command = args[0].toLowerCase();
            if (command.equals("set")) {
                if (args.length != 3) return "net set <field> <value>";
                final String field = args[1];
                final String value = args[2];
                switch (field) {
                    case "user":
                        this.config.setUser(value);
                        return field + " set to " + value;
                    case "key":
                        this.config.setKey(value);
                        return field + " set to " + value;
                    case "host":
                        this.config.setHost(value);
                        return field + " set to " + value;
                    case "port":
                        final int port;
                        try {

                            port = Integer.valueOf(value);
                        } catch (NumberFormatException e) {
                            return "Given value is not an integer!";
                        }
                        this.config.setPort(port);
                        return field + " set to " + value;
                }
            } else if (command.equals("info")) {
                if (args.length != 1) return "net info";
                return "Info: \n"
                        + "Host: " + this.config.getHost() + "\n"
                        + "Port: " + this.config.getPort() + "\n"
                        + "User: " + this.config.getUser() + "\n";
            } else if (command.equals("status")) {
                if (args.length != 1) return "net status";
                if (this.connection == null) return "NetClient is not initialized!";
                return this.connection.isRunning() ? "NetClient is running!" : "NetClient is closed.";
            } else if (command.equals("connect")) {
                if (args.length != 1) return "net connect";
                if (this.connection == null) return "NetClient is not initialized!";
                try {
                    this.connection.establish();
                    return "db connection successfully established!";
                } catch (InterruptedException e) {
                    return "error while establishing db connection: " + e.getMessage();
                }
            } else if (command.equals("save")) {
                if (args.length != 1) return "net save";
                this.configManager.saveConfig(this.config);
                return "NetClientConfig saved!";
            } else if (command.equals("load")) {
                if (args.length != 1) return "net load";
                try {
                    this.configManager.readConfigTo(this.config).get().get(2, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
                this.connection.fillConnectionData(config);
                return "NetClientConfig loaded!";
            }
        }

        return "net subcommand not found.";
    }

    @Override
    public String getShortDescription() {
        return "Configure the NetClient Settings";
    }
}
