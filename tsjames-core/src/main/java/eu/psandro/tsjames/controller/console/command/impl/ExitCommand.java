package eu.psandro.tsjames.controller.console.command.impl;

import eu.psandro.tsjames.bootstrap.JamesBootstrap;
import eu.psandro.tsjames.controller.console.command.Command;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * @author PSandro on 30.08.18
 * @project tsjames
 */
@AllArgsConstructor
public final class ExitCommand extends Command {

    private final @NonNull
    JamesBootstrap jamesBootstrap;

    @Override
    public String handleCommand(String[] args) {
        this.jamesBootstrap.shutdown();
        return "bye...";
    }

    @Override
    public String getShortDescription() {
        return "Shutdown this James instance.";
    }
}
