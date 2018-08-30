package eu.psandro.tsjames.bot.controller.command;

import eu.psandro.tsjames.bot.bootstrap.TSJamesBot;
import eu.psandro.tsjames.controller.console.command.Command;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author PSandro on 30.08.18
 * @project tsjames
 */
@AllArgsConstructor
public abstract class BotCommand extends Command {
    @Getter(AccessLevel.PROTECTED)
    private final TSJamesBot jamesBot;
}
