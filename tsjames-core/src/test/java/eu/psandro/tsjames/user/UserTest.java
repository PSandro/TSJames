package eu.psandro.tsjames.user;

import eu.psandro.tsjames.model.database.DatabaseManager;
import eu.psandro.tsjames.user.rank.RankData;
import eu.psandro.tsjames.test.RepositoryUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class UserTest {

    private static DatabaseManager databaseManager;
    private static RankData defaultRank = RankData.DEFAULT;

    @BeforeAll
    static void setUp() {
        databaseManager = RepositoryUtil.createTestDatabaseManager(User.class, UserData.class, RankData.class);
    }

    @AfterAll
    static void tearDown() throws IOException {
        databaseManager.close();
    }


    @Test
    public void testUserInsertion() {

        final String username = "test";

        final User user = databaseManager.createUser(username, UUID.randomUUID().toString(), UUID.randomUUID().toString());



        assertEquals(username, user.getUsername());

    }

    @Test
    public void testUserGetByUsername() {

        final String username = "hallo123";

        final User user = databaseManager.createUser(username, UUID.randomUUID().toString(), UUID.randomUUID().toString());
        final User fetchedUser = databaseManager.getUser(username);

        assertEquals(user, fetchedUser);

    }

    @Test
    public void testUserGetById() {

        final String username = "test5678";

        final User user = databaseManager.createUser(username, UUID.randomUUID().toString(), UUID.randomUUID().toString());
        final User fetchedUser = databaseManager.getUser(user.getUserId());

        assertEquals(user, fetchedUser);

    }

    @Test
    public void testUpdateUsername() {

        final String username = "huhu789";

        final User user = databaseManager.createUser(username, UUID.randomUUID().toString(), UUID.randomUUID().toString());
        databaseManager.updateUsername(user.getUserId(), "huhu89");
        final User fetchedUser = databaseManager.getUser(user.getUserId());
        assertNotEquals(user, fetchedUser);

        assertEquals(user.getUserId(), fetchedUser.getUserId());

    }

}