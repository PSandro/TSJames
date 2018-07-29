package eu.psandro.tsjames.bot.query.command;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import eu.psandro.tsjames.model.PermissionFetcher;
import eu.psandro.tsjames.rank.RankPermission;
import lombok.NonNull;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public final class PingCommand extends TeamSpeakCommand {

    private final RankPermission[] permissions;

    public PingCommand(@NonNull PermissionFetcher permissionFetcher) {
        super(permissionFetcher);
        this.permissions = new RankPermission[]{
                super.getPermissionFetcher().getOrCreatePermission("human"),
        };
    }

    @Override
    public RankPermission[] getNeededPermissions() {
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
