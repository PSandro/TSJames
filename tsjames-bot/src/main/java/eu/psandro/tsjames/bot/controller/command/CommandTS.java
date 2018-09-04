package eu.psandro.tsjames.bot.controller.command;

import eu.psandro.tsjames.bot.bootstrap.TSJamesBot;
import eu.psandro.tsjames.bot.model.TeamSpeakConfig;
import eu.psandro.tsjames.bot.query.TeamSpeakConnection;

import java.io.IOException;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public final class CommandTS extends BotCommand {
    private static final String OPTIONS = "Commands:\n " + String.join("\n ",
            "set <host|username|password|nickname|port|vServerId> <value>",
            "info",
            "status - get the status of the connection",
            "connect - (re)establish the connection",
            "save - save the configuration to the file",
            "load - load the configuration from the file") + "\n";

    private final TeamSpeakConfig teamSpeakConfig;
    private final TeamSpeakConnection teamSpeakConnection;

    public CommandTS(TSJamesBot jamesBot) {
        super(jamesBot);
        this.teamSpeakConnection = (TeamSpeakConnection) super.getJamesBot().getTeamSpeakConnection();
        this.teamSpeakConfig = this.teamSpeakConnection.getTeamSpeakConfig();
    }


    @Override
    public String handleCommand(String[] args) {
        if (args.length <= 0) {
            return OPTIONS;
        } else {
            final String command = args[0].toLowerCase();
            if ("set".equals(command)) {
                if (args.length != 3) return "ts set <field> <value>";
                final String field = args[1];
                final String value = args[2];
                switch (field) {
                    case "host":
                        this.teamSpeakConfig.setHost(value);
                        return field + " set to " + value;
                    case "username":
                        this.teamSpeakConfig.setUsername(value);
                        return field + " set to " + value;
                    case "password":
                        this.teamSpeakConfig.setPassword(value);
                        return field + " set to " + value;
                    case "nickname":
                        this.teamSpeakConfig.setNickname(value);
                        return field + " set to " + value;
                    case "port":
                        final int port;
                        try {
                            port = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            return "The <port> is not an integer!";
                        }
                        this.teamSpeakConfig.setPort(port);
                        return field + " set to " + value;
                    case "vServerId":
                        final int vServerId;
                        try {
                            vServerId = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            return "The <port> is not an integer!";
                        }
                        this.teamSpeakConfig.setVServerId(vServerId);
                        return field + " set to " + value;
                }
            } else if ("info".equals(command)) {
                if (args.length != 1) return "ts info";
                return "Host: " + this.teamSpeakConfig.getHost() + "\n" +
                        "Username: " + this.teamSpeakConfig.getUsername() + "\n" +
                        "Nickname: " + this.teamSpeakConfig.getNickname() + "\n" +
                        "Port: " + this.teamSpeakConfig.getPort() + "\n" +
                        "vServerId: " + this.teamSpeakConfig.getVServerId() + "\n";
            } else if ("status".equals(command)) {
                if (args.length != 1) return "ts status";
                return this.teamSpeakConnection.isRunning() ? "TS connection is up." : "TS connection is closed.";
            } else if ("connect".equals(command)) {
                if (args.length != 1) return "ts connect";
                try {
                    this.teamSpeakConnection.establish();
                    return "ts connection successfully established!";
                } catch (IOException e) {
                    return "error while establishing ts connection: " + e.getMessage();
                }

            } else if ("save".equals(command)) {
                if (args.length != 1) return "ts save";
                super.getJamesBot().getConfigManager().saveConfig(this.teamSpeakConfig);
                return "TeamSpeakConfig saved!";
            } else if ("load".equals(command)) {
                if (args.length != 1) return "ts load";
                super.getJamesBot().getConfigManager().readConfigTo(this.teamSpeakConfig);
                return "TeamSpeakConfig loaded!";
            }
        }

        return "ts subcommand not found.";
    }

    @Override
    public String getShortDescription() {
        return "Configure the TeamSpeak settings.";
    }
}
