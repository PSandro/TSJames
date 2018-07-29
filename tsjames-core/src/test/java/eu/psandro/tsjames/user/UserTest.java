package eu.psandro.tsjames.user;

import eu.psandro.tsjames.model.DatabaseManager;
import eu.psandro.tsjames.rank.RankData;
import eu.psandro.tsjames.rank.RankPermission;
import eu.psandro.tsjames.test.RepositoryUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class UserTest {

    private static DatabaseManager databaseManager;

    @BeforeAll
    static void setUp() {
        databaseManager = RepositoryUtil.createTestDatabaseManager(User.class, UserData.class, UserRank.class, RankData.class, RankPermission.class);
    }

    @AfterAll
    static void tearDown() throws IOException {
        databaseManager.close();
    }


    @Test
    public void testUserInsertion() {

        final String username = "test";

        final User user = databaseManager.createUser(username);

        assertEquals(username, user.getUsername());
        assertTrue(user.getCreation().getTime() <= System.currentTimeMillis());

    }

    @Test
    public void testUserGetByUsername() {

        final String username = "hallo123";

        final User user = databaseManager.createUser(username);
        final User fetchedUser = databaseManager.getUser(username);

        assertEquals(user, fetchedUser);

    }

    @Test
    public void testUserGetById() {

        final String username = "test5678";

        final User user = databaseManager.createUser(username);
        final User fetchedUser = databaseManager.getUser(user.getUserId());

        assertEquals(user, fetchedUser);

    }

    @Test
    public void testUpdateUsername() {

        final String username = "huhu789";

        final User user = databaseManager.createUser(username);
        databaseManager.updateUsername(user.getUserId(), "huhu89");
        final User fetchedUser = databaseManager.getUser(user.getUserId());
        assertNotEquals(user, fetchedUser);

        assertEquals(user.getUserId(), fetchedUser.getUserId());
        assertEquals(user.getCreation(), fetchedUser.getCreation());

    }
}