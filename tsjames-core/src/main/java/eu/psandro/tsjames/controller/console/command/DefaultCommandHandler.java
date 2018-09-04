package eu.psandro.tsjames.controller.console.command;

import eu.psandro.tsjames.bootstrap.JamesBootstrap;
import eu.psandro.tsjames.controller.console.command.impl.ExitCommand;
import eu.psandro.tsjames.controller.console.command.impl.HelpCommand;
import lombok.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author PSandro on 30.08.18
 * @project tsjames
 */
public class DefaultCommandHandler implements CommandHandler {

    private final Map<String, Command> commandBindings = new HashMap<>();

    private final JamesBootstrap jamesBootstrap;

    public DefaultCommandHandler(@NonNull JamesBootstrap jamesBootstrap) {
        this.jamesBootstrap = jamesBootstrap;
        this.registerDefaultCommands();
    }

    private void registerDefaultCommands() {
        this.registerCommand("exit", new ExitCommand(this.jamesBootstrap));
        this.registerCommand("help", new HelpCommand(this.commandBindings));
    }

    public CommandHandler registerCommand(@NonNull String commandName, @NonNull Command command) {
        if (this.commandBindings.containsKey(commandName)) return this; //TODO throw exception
        this.commandBindings.put(commandName, command);
        return this;
    }

    @Override
    public void unregisterCommand(String commandName) {
        this.commandBindings.remove(commandName);
    }

    private Command matchCommand(String command) {
        if (this.commandBindings.containsKey(command)) {
            return this.commandBindings.get(command);
        }
        return null;
    }


    @Override
    public String handleCommandInput(String[] args) {
        if (args.length >= 1) {
            final Command command = this.matchCommand(args[0]);
            if (command != null) {
                return command.handleCommand(Arrays.copyOfRange(args, 1, args.length));
            }
        }


        return "Command not found. Try \"help\"";
    }
}
