package eu.psandro.tsjames.user;


import eu.psandro.tsjames.rank.RankData;

import java.util.Date;

public final class UserFactory {

    public static User createUser(String username, String email, String passwordHash) {
        final User user = new User(username, email, passwordHash);
        return user;
    }
}
