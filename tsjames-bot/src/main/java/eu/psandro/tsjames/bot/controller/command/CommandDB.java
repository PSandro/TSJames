package eu.psandro.tsjames.bot.controller.command;

import eu.psandro.tsjames.bot.bootstrap.TSJamesBot;
import eu.psandro.tsjames.bot.controller.Command;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CommandDB extends Command {

    private static final String OPTIONS = "Commands:\n " + Stream.of(
            "set <url|username|password> <value>",
            "get <url|username>",
            "status - get the status of the connection",
            "connect - (re)establish the connection",
            "buildURL <host> <port> <database>",
            "save - save the configuration to the file",
            "load - load the configuration from the file").collect(Collectors.joining("\n ")) + "\n";

    public CommandDB(TSJamesBot jamesBot) {
        super(jamesBot);
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
                        super.getJamesBot().getDatabaseConfig().setUsername(value);
                        return field + " set to " + value;
                    case "password":
                        super.getJamesBot().getDatabaseConfig().setPassword(value);
                        return field + " set to " + value;
                    case "url":
                        super.getJamesBot().getDatabaseConfig().setUrl(value);
                        return field + " set to " + value;
                }
            } else if (command.equals("get")) {
                if (args.length != 2) return "db get <field>";
                final String field = args[1].toLowerCase();
                switch (field) {
                    case "username":
                        return "Value: " + super.getJamesBot().getDatabaseConfig().getUsername();
                    case "url":
                        return "Value: " + super.getJamesBot().getDatabaseConfig().getUrl();
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
                super.getJamesBot().getDatabaseConfig().setUrl(host, port, database);
                return "URL set!";

            } else if (command.equals("status")) {
                if (args.length != 1) return "db status";
                return super.getJamesBot().getDatabaseManager().isOpen() ? "DB connection is up." : "DB connection is closed.";
            } else if (command.equals("connect")) {
                if (args.length != 1) return "db connect";
                try {
                    super.getJamesBot().establishDatabaseConnection();
                    return "db connection successfully established!";
                } catch (IOException e) {
                    return "error while establishing db connection: " + e.getMessage();
                }

            } else if (command.equals("save")) {
                if (args.length != 1) return "db save";
                super.getJamesBot().getConfigManager().saveConfig(super.getJamesBot().getDatabaseConfig());
                return "DatabaseConfig saved!";
            } else if (command.equals("load")) {
                if (args.length != 1) return "db load";
                super.getJamesBot().getConfigManager().readConfigTo(super.getJamesBot().getDatabaseConfig());
                return "DatabaseConfig loaded!";
            }
        }

        return "db subcommand not found.";
    }
}
