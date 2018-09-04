package eu.psandro.tsjames.controller.console.command.impl;

import eu.psandro.tsjames.controller.console.command.Command;
import eu.psandro.tsjames.model.file.ConfigManager;
import eu.psandro.tsjames.model.file.DatabaseConfig;
import eu.psandro.tsjames.model.database.DatabaseConnection;
import eu.psandro.tsjames.model.database.DatabaseManager;

import java.io.IOException;

public final class CommandDB extends Command {

    private static final String OPTIONS = "Commands:\n " + String.join("\n ",
            "set <url|username|password> <value>",
            "get <url|username>",
            "status - get the status of the connection",
            "connect - (re)establish the connection",
            "buildURL <host> <port> <database>",
            "save - save the configuration to the file",
            "load - load the configuration from the file") + "\n";

    private final DatabaseConfig databaseConfig;
    private final DatabaseManager databaseManager;
    private final DatabaseConnection databaseConnection;
    private final ConfigManager configManager;

    public CommandDB(final DatabaseConnection databaseConnection, final ConfigManager configManager) {
        this.databaseConnection = databaseConnection;
        this.databaseConfig = databaseConnection.getDatabaseConfig();
        this.databaseManager = databaseConnection.getDatabaseManager();
        this.configManager = configManager;
    }


    @Override
    public String handleCommand(String[] args) {
        if (args.length <= 0) {
            return OPTIONS;
        } else {
            final String command = args[0].toLowerCase();
            if ("set".equals(command)) {
                if (args.length != 3) return "db set <field> <value>";
                final String field = args[1];
                final String value = args[2];
                switch (field) {
                    case "username":
                        this.databaseConfig.setUsername(value);
                        return field + " set to " + value;
                    case "password":
                        this.databaseConfig.setPassword(value);
                        return field + " set to " + value;
                    case "url":
                        this.databaseConfig.setUrl(value);
                        return field + " set to " + value;
                }
            } else if ("get".equals(command)) {
                if (args.length != 2) return "db get <field>";
                final String field = args[1].toLowerCase();
                switch (field) {
                    case "username":
                        return "Value: " + this.databaseConfig.getUsername();
                    case "url":
                        return "Value: " + this.databaseConfig.getUrl();
                }
                return "Field not found!";
            } else if ("build".equals(command)) {
                if (args.length != 4) return "buildURL <host> <port> <database>";
                final String host = args[1];
                final String database = args[3];
                final int port;
                try {
                    port = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    return "The <port> is not an integer!";
                }
                this.databaseConfig.setUrl(host, port, database);
                return "URL set!";

            } else if ("status".equals(command)) {
                if (args.length != 1) return "db status";
                return this.databaseManager.isOpen() ? "DB connection is up." : "DB connection is closed.";
            } else if ("connect".equals(command)) {
                if (args.length != 1) return "db connect";
                try {
                    this.databaseConnection.establish();
                    return "db connection successfully established!";
                } catch (IOException e) {
                    return "error while establishing db connection: " + e.getMessage();
                }

            } else if ("save".equals(command)) {
                if (args.length != 1) return "db save";
                this.configManager.saveConfig(this.databaseConfig);
                return "DatabaseConfig saved!";
            } else if ("load".equals(command)) {
                if (args.length != 1) return "db load";
                this.configManager.readConfigTo(this.databaseConfig);
                return "DatabaseConfig loaded!";
            }
        }

        return "db subcommand not found.";
    }

    @Override
    public String getShortDescription() {
        return "Configure the database settings.";
    }
}
