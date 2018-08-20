package eu.psandro.tsjames.bot.controller;

import eu.psandro.tsjames.bot.bootstrap.TSJamesBot;
import eu.psandro.tsjames.bot.controller.command.CommandDB;
import eu.psandro.tsjames.bot.controller.command.CommandTS;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CommandHandlerImpl implements CommandHandler {

    private final TSJamesBot tsJamesBot;
    private final Command commandDB, commandTS;

    private static final String OPTIONS = "Commands:\n " + String.join("\n ", "ping", "help", "db", "ts", "exit");

    public CommandHandlerImpl(TSJamesBot tsJamesBot) {
        this.tsJamesBot = tsJamesBot;
        this.commandDB = new CommandDB(tsJamesBot);
        this.commandTS = new CommandTS(tsJamesBot);

    }


    @Override
    public String handleCommandInput(String[] args) {

        switch (args[0].toLowerCase()) {
            case "ping":
                return "pong";
            case "help":
                return OPTIONS;
            case "db":
                return this.commandDB.handleCommand(Arrays.copyOfRange(args, 1, args.length));
            case "ts":
                return this.commandTS.handleCommand(Arrays.copyOfRange(args, 1, args.length));
            case "exit":
                this.tsJamesBot.shutdown();
                return "bye...";
        }


        return "Command not found. Try \"help\"";
    }
}
