package eu.psandro.tsjames.bot.query.event;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import eu.psandro.tsjames.model.database.DatabaseManager;
import eu.psandro.tsjames.user.User;

import java.util.UUID;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public final class LoginListener extends TeamSpeakListener<ClientJoinEvent> {

    private final DatabaseManager databaseManager;
    private final String[] permissions;

    protected LoginListener(DatabaseManager databaseManager) {
        super();
        this.databaseManager = databaseManager;
        this.permissions = new String[]{
                "human"
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
    public String[] getNeededPermissions() {
        return this.permissions;
    }
}
