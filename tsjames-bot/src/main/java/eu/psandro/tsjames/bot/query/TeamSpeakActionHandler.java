package eu.psandro.tsjames.bot.query;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import eu.psandro.tsjames.api.exception.CommandNotRegisteredException;
import eu.psandro.tsjames.bot.query.command.CommandManager;
import eu.psandro.tsjames.bot.query.command.TeamSpeakCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */

@AllArgsConstructor
public class TeamSpeakActionHandler extends TS3EventAdapter {

    private final TS3Api ts3Api;
    private final CommandManager commandManager;
    @Getter
    private boolean enabled;

    @Override
    public void onTextMessage(TextMessageEvent e) {
        if (e.getMessage() == null) return;
        if (e.getMessage().startsWith("!")) {
            final String[] message = e.getMessage().split("\\s+");
            if (message[0].length() <= 1) return;
            message[0] = message[0].replaceFirst("!", "");
            final TeamSpeakCommand command;
            try {
                command = this.commandManager.getCommandByLabel(message[0]);

            } catch (CommandNotRegisteredException exc) {
                this.ts3Api.sendPrivateMessage(e.getInvokerId(), "The command was not found or is not registered!");
                return;
            }

            command.handle(message, e.getInvokerName(), e.getInvokerId(), e.getInvokerUniqueId(), this.ts3Api);

        }
    }

    @Override
    public void onClientJoin(ClientJoinEvent e) {
    }

    @Override
    public void onClientLeave(ClientLeaveEvent e) {
    }


    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }
}
