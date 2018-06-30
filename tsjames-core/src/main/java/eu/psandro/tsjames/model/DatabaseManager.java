package eu.psandro.tsjames.model;

import eu.psandro.tsjames.user.User;

import java.io.Closeable;


public interface DatabaseManager extends Closeable {

    DatabaseManager init(DatabaseAccessData accessData);

    User createUser(String username);

    User getUser(String username);

    User getUser(Integer userId);

    void updateUsername(int userId, String username);

}
