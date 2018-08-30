package eu.psandro.tsjames.bot.query.command;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public final class PingCommand extends TeamSpeakCommand {

    private final String[] permissions;

    public PingCommand() {
        super();
        this.permissions = new String[]{
                "human"
        };
    }

    @Override
    public String[] getNeededPermissions() {
        return this.permissions;
    }

    @Override
    public String getLabel() {
        return "ping";
    }

    @Override
    public void handle(String[] args, String senderName, int senderClientId, String senderUniqueId, TS3Api ts3Api) {
        ts3Api.sendTextMessage(TextMessageTargetMode.CLIENT, senderClientId, "Pong!");
    }


}
