package eu.psandro.tsjames.controller.console.command;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class Command {

    public abstract String handleCommand(String[] args);

    public abstract String getShortDescription();

}
