package eu.psandro.tsjames.model.database;

import eu.psandro.tsjames.api.exception.JamesNotInitException;
import eu.psandro.tsjames.user.rank.RankData;
import eu.psandro.tsjames.user.*;
import lombok.NonNull;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;

public final class DatabaseManagerImpl implements DatabaseManager {


    private @NonNull
    Optional<SessionFactory> sessionFactory = Optional.empty();


    public DatabaseManagerImpl() {
    }

    @Override
    public DatabaseManagerImpl init(final DatabaseAccessData accessData) throws HibernateException {
        final Properties settings = new Properties();
        settings.setProperty(Environment.URL, accessData.getUrl());
        settings.setProperty(Environment.USER, accessData.getUsername());
        settings.setProperty(Environment.PASS, accessData.getPassword());
        settings.setProperty(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        settings.setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        settings.setProperty(Environment.HBM2DDL_AUTO, "update");
        settings.setProperty(Environment.SHOW_SQL, "false");
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.WARNING);

        final SessionFactory sessionFactory = new Configuration()
                .addProperties(settings)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(UserFactory.class)
                .addAnnotatedClass(UserData.class)
                .addAnnotatedClass(RankData.class)
                .buildSessionFactory();
        return this.init(sessionFactory);
    }

    public DatabaseManagerImpl init(final @NonNull SessionFactory sessionFactory) {
        this.sessionFactory = Optional.of(sessionFactory);
        this.createDefaults();
        return this;
    }

    private void createDefaults() {
        if (this.isTableEmpty("RankData")) {
            final RankData defaultRank = RankData.DEFAULT;
            final Session session = this.openSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();
                session.save(defaultRank);
                transaction.commit();

            } catch (HibernateException e) {
                if (transaction != null) transaction.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    private boolean isTableEmpty(String table) {
        final Session session = this.openSession();
        boolean result = session.createQuery("SELECT 1 from " + table).setMaxResults(1).list().isEmpty();
        session.close();
        return result;
    }

    @Override
    public User createUser(@NonNull String username, String email, String passwordHash) {
        final Session session = this.openSession();
        final User user = UserFactory.createUser(username, email, passwordHash);
        final UserData userData = UserFactory.createUserData();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            user.setUserData(userData);
            userData.setUser(user);
            session.save(user);
            transaction.commit();

        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public User getUser(String username) {
        final Session session = this.openSession();
        Transaction fetchTransacton = null;
        User fetchedUser = null;

        try {

            fetchTransacton = session.beginTransaction();
            fetchedUser = session.byNaturalId(User.class)
                    .using("username", username).load();
            fetchTransacton.commit();

        } catch (HibernateException e) {
            if (fetchTransacton != null) fetchTransacton.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return fetchedUser;
    }

    @Override
    public User getUserByTeamSpeakUUID(UUID uuid) {
        final Session session = this.openSession();
        Transaction fetchTransacton = null;
        UserData fetchedData = null;

        try {

            fetchTransacton = session.beginTransaction();
            fetchedData = session.byNaturalId(UserData.class)
                    .using("teamspeak_id", uuid.toString()).load();
            fetchTransacton.commit();

        } catch (HibernateException e) {
            if (fetchTransacton != null) fetchTransacton.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        if (fetchedData == null) return null;
        return fetchedData.getUser();
    }


    @Override
    public User getUser(Long userId) {
        final Session session = this.openSession();
        Transaction fetchTransacton = null;
        User fetchedUser = null;

        try {

            fetchTransacton = session.beginTransaction();
            fetchedUser = session.get(User.class, userId);
            fetchTransacton.commit();

        } catch (HibernateException e) {
            if (fetchTransacton != null) fetchTransacton.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return fetchedUser;
    }

    @Override
    public void updateUsername(long userId, String username) {
        final Session session = this.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            final User user = session.get(User.class, userId);
            user.setUsername(username);
            session.update(user);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    @Override
    public boolean isOpen() {
        return this.sessionFactory.isPresent() && this.sessionFactory.get().isOpen();
    }


    private Session openSession() throws JamesNotInitException {
        return this.sessionFactory.orElseThrow(() -> new JamesNotInitException("SessionFactory")).openSession();
    }


    @Override
    public void close() {
        this.sessionFactory.ifPresent(SessionFactory::close);
        this.sessionFactory = Optional.empty();
    }
}
