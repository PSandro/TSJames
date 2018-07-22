package eu.psandro.tsjames.bot.controller;

@FunctionalInterface
public interface CommandHandler {

    String handleCommandInput(String[] args);

}
