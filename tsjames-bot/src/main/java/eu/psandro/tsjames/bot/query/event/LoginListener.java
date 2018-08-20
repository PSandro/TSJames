package eu.psandro.tsjames.bot.query.event;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import eu.psandro.tsjames.model.DatabaseManager;
import eu.psandro.tsjames.model.PermissionFetcher;
import eu.psandro.tsjames.rank.RankPermission;
import eu.psandro.tsjames.user.User;

import java.util.UUID;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public final class LoginListener extends TeamSpeakListener<ClientJoinEvent> {

    private final RankPermission[] permissions;
    private final DatabaseManager databaseManager;

    protected LoginListener(PermissionFetcher permissionFetcher, DatabaseManager databaseManager) {
        super(permissionFetcher);
        this.databaseManager = databaseManager;
        this.permissions = new RankPermission[]{
                super.getPermissionFetcher().getOrCreatePermission("human"),
        };
    }

    @Override
    public void handle(ClientJoinEvent event) {
        final UUID uniqueId = UUID.fromString(event.getUniqueClientIdentifier());
        final User user = this.databaseManager.getUserByTeamSpeakUUID(uniqueId);
        if (user == null) {
            //TODO do sth usefull
        } else {

        }

    }

    @Override
    public RankPermission[] getNeededPermissions() {
        return this.permissions;
    }
}
