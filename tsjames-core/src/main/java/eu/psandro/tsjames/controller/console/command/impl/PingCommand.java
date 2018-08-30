package eu.psandro.tsjames.controller.console.command.impl;

import eu.psandro.tsjames.controller.console.command.Command;

/**
 * @author PSandro on 30.08.18
 * @project tsjames
 */
public final class PingCommand extends Command {
    @Override
    public String handleCommand(String[] args) {
        return "Pong";
    }

    @Override
    public String getShortDescription() {
        return "Return \"Pong\"." ;
    }
}
