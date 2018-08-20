package eu.psandro.tsjames.user;


public final class UserFactory {

    public static User createUser(String username, String email, String passwordHash) {
        return new User(username, email, passwordHash);

    }

    public static UserData createUserData() {
        return new UserData();

    }


}
