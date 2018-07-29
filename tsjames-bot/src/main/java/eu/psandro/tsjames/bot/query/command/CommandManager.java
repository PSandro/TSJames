package eu.psandro.tsjames.bot.query.command;


import eu.psandro.tsjames.bot.controller.exception.CommandNotRegisteredException;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public final class CommandManager {

    private final Set<TeamSpeakCommand> commands = new HashSet<>();

    public boolean registerCommand(TeamSpeakCommand teamSpeakCommand) {
        if (this.commands.contains(teamSpeakCommand)) return false;
        return this.commands.add(teamSpeakCommand);
    }

    public void unregisterCommand(String label) {
        this.commands.remove(this.getCommandByLabel(label));
    }

    public TeamSpeakCommand getCommandByLabel(@NonNull String label) {
        return this.commands.stream().filter(teamSpeakCommand -> teamSpeakCommand.getLabel().equalsIgnoreCase(label))
                .findAny()
                .orElseThrow(() -> new CommandNotRegisteredException(label));
    }

}
