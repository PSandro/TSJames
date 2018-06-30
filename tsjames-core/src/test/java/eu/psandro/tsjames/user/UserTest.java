package eu.psandro.tsjames.user;

import eu.psandro.tsjames.rank.RankData;
import eu.psandro.tsjames.test.RepositoryUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


class UserTest {

    private static SessionFactory sessionFactory;

    @BeforeAll
    static void setUp() {
        sessionFactory = RepositoryUtil.createTestSessionFactory(User.class, UserData.class, UserLog.class, UserRank.class, RankData.class);
    }

    @AfterAll
    static void tearDown() {
        sessionFactory.close();
    }


    @Test
    public void testUserInsertion() {
        final Session session = this.sessionFactory.openSession();
        Transaction insertTransaction = null;
        Transaction fetchTransacton = null;

        try {
            insertTransaction = session.beginTransaction();
            final User user = UserFactory.createUser();
            user.setUsername("Test");
            final Integer userId = (Integer) session.save(user);
            insertTransaction.commit();

            fetchTransacton = session.beginTransaction();
            final User fetchedUser = session.get(User.class, userId);
            fetchTransacton.commit();

            assertEquals(userId.intValue(), fetchedUser.getUserId());
            assertEquals(user.getUsername(), fetchedUser.getUsername());
            assertEquals(user.getUserData(), fetchedUser.getUserData());
            assertEquals(user.getUserLog(), fetchedUser.getUserLog());

            assertTrue(fetchedUser.getCreation().before(new Date()));

        } catch (HibernateException e) {
            if (insertTransaction != null) insertTransaction.rollback();
            if (fetchTransacton != null) fetchTransacton.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }
}