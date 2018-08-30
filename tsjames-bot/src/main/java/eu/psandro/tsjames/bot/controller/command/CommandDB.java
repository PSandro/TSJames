package eu.psandro.tsjames.bot.controller.command;

import eu.psandro.tsjames.bot.bootstrap.TSJamesBot;
import eu.psandro.tsjames.model.file.DatabaseConfig;
import eu.psandro.tsjames.model.database.DatabaseConnection;
import eu.psandro.tsjames.model.database.DatabaseManager;

import java.io.IOException;

public final class CommandDB extends BotCommand {

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

    public CommandDB(TSJamesBot jamesBot) {
        super(jamesBot);
        this.databaseConnection = (DatabaseConnection) super.getJamesBot().getDatabaseConnection();
        this.databaseConfig = databaseConnection.getDatabaseConfig();
        this.databaseManager = databaseConnection.getDatabaseManager();
    }


    @Override
    public String handleCommand(String[] args) {
        if (args.length <= 0) {
            return OPTIONS;
        } else {
            final String command = args[0].toLowerCase();
            if (command.equals("set")) {
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
            } else if (command.equals("get")) {
                if (args.length != 2) return "db get <field>";
                final String field = args[1].toLowerCase();
                switch (field) {
                    case "username":
                        return "Value: " + this.databaseConfig.getUsername();
                    case "url":
                        return "Value: " + this.databaseConfig.getUrl();
                }
                return "Field not found!";
            } else if (command.equals("buildurl")) {
                if (args.length != 4) return "buildURL <host> <port> <database>";
                final String host = args[1], database = args[3];
                final int port;
                try {
                    port = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    return "The <port> is not an integer!";
                }
                this.databaseConfig.setUrl(host, port, database);
                return "URL set!";

            } else if (command.equals("status")) {
                if (args.length != 1) return "db status";
                return this.databaseManager.isOpen() ? "DB connection is up." : "DB connection is closed.";
            } else if (command.equals("connect")) {
                if (args.length != 1) return "db connect";
                try {
                    this.databaseConnection.establish();
                    return "db connection successfully established!";
                } catch (IOException e) {
                    return "error while establishing db connection: " + e.getMessage();
                }

            } else if (command.equals("save")) {
                if (args.length != 1) return "db save";
                super.getJamesBot().getConfigManager().saveConfig(this.databaseConfig);
                return "DatabaseConfig saved!";
            } else if (command.equals("load")) {
                if (args.length != 1) return "db load";
                super.getJamesBot().getConfigManager().readConfigTo(this.databaseConfig);
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
