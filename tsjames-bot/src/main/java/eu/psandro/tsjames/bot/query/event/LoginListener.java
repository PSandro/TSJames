package eu.psandro.tsjames.bot.query.event;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import eu.psandro.tsjames.model.database.DatabaseManager;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public final class LoginListener extends TeamSpeakListener<ClientJoinEvent> {

    @SuppressWarnings("FieldCanBeLocal")
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
        //TODO handle user login

    }

    @Override
    public String[] getNeededPermissions() {
        return this.permissions;
    }
}
