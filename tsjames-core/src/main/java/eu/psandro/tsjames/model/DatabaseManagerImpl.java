package eu.psandro.tsjames.model;

import eu.psandro.tsjames.api.exception.JamesNotInitException;
import eu.psandro.tsjames.rank.RankData;
import eu.psandro.tsjames.user.*;
import lombok.NonNull;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Date;
import java.util.Optional;
import java.util.Properties;
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
                .addAnnotatedClass(UserRank.class)
                .addAnnotatedClass(UserData.class)
                .addAnnotatedClass(RankData.class)
                .buildSessionFactory();
        return this.init(sessionFactory);
    }

    public DatabaseManagerImpl init(final @NonNull SessionFactory sessionFactory) {
        this.sessionFactory = Optional.of(sessionFactory);
        return this;
    }

    @Override
    public User createUser(@NonNull String username) {
        final Session session = this.openSession();
        final User user = UserFactory.createUser(username);
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(user);
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
