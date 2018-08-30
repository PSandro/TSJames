package eu.psandro.tsjames.bot.query.command;

import com.github.theholywaffle.teamspeak3.TS3Api;
import eu.psandro.tsjames.bot.query.PermittedAction;


/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public abstract class TeamSpeakCommand extends PermittedAction {


    protected TeamSpeakCommand() {
        super();
    }

    public abstract String getLabel();

    public abstract void handle(String[] args, String senderName, int senderClientId, String senderUniqueId, TS3Api ts3Api);


}
