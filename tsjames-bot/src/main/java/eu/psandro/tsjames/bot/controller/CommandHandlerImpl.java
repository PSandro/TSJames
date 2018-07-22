package eu.psandro.tsjames.bot.controller;

import eu.psandro.tsjames.bot.bootstrap.TSJamesBot;
import eu.psandro.tsjames.bot.controller.command.CommandDB;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CommandHandlerImpl implements CommandHandler {

    private final TSJamesBot tsJamesBot;
    private final Command commandDB;

    private static final String OPTIONS = Stream.of("ping", "help", "db", "exit").collect(Collectors.joining("\n"));

    public CommandHandlerImpl(TSJamesBot tsJamesBot) {
        this.tsJamesBot = tsJamesBot;
        this.commandDB = new CommandDB(tsJamesBot);
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
            case "exit":
                this.tsJamesBot.shutdown();
                return "bye...";
        }


        return "Command not found. Try \"help\"";
    }
}
