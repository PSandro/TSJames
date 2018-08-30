package eu.psandro.tsjames.controller.console.command;

public interface CommandHandler {

    String handleCommandInput(String[] args);

    CommandHandler registerCommand(String commandName, Command command);

    void unregisterCommand(String commandName);

}
