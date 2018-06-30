package eu.psandro.tsjames.user;


import java.util.Date;

public final class UserFactory {

    public static User createUser(String username) {
        final User user = new User();
        user.setUsername(username);
        user.setCreation(new Date());
        return user;
    }
}
