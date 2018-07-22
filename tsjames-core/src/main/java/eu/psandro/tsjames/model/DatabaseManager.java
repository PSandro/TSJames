package eu.psandro.tsjames.model;

import eu.psandro.tsjames.user.User;

import java.io.Closeable;


public interface DatabaseManager extends Closeable {

    DatabaseManager init(DatabaseAccessData accessData);

    User createUser(String username);

    User getUser(String username);

    User getUser(Long userId);

    void updateUsername(long userId, String username);

    boolean isOpen();

}
