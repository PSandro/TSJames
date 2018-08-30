package eu.psandro.tsjames.model.database;

import eu.psandro.tsjames.user.User;

import java.io.Closeable;
import java.util.UUID;


public interface DatabaseManager extends Closeable {

    DatabaseManager init(DatabaseAccessData accessData);

    User createUser(String username, String email, String passwordHash);

    User getUser(String username);

    User getUserByTeamSpeakUUID(UUID uuid);

    User getUser(Long userId);

    void updateUsername(long userId, String username);


    boolean isOpen();

}
