package eu.psandro.tsjames.controller.console.command.impl;

import eu.psandro.tsjames.controller.console.command.Command;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author PSandro on 30.08.18
 * @project tsjames
 */
@AllArgsConstructor
public final class HelpCommand extends Command {

    @NonNull
    private final Map<String, Command> bindings;


    @Override
    public String handleCommand(String[] args) {
        return "Commands:\n" + bindings.entrySet().stream().map(e -> " " + e.getKey() + " - " + e.getValue().getShortDescription()).collect(Collectors.joining("\n"));
    }

    @Override
    public String getShortDescription() {
        return "Display all commands and short descriptions.";
    }
}
