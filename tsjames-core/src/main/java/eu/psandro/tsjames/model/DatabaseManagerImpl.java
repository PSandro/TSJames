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

public final class DatabaseManagerImpl implements DatabaseManager {


    private Optional<SessionFactory> sessionFactory;

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
        settings.setProperty(Environment.HBM2DDL_AUTO, "create"); //TODO: need to change to "update" after debugging
        settings.setProperty(Environment.SHOW_SQL, "true");

        final SessionFactory sessionFactory = new Configuration()
                .addProperties(settings)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(UserData.class)
                .addAnnotatedClass(UserFactory.class)
                .addAnnotatedClass(UserLog.class)
                .addAnnotatedClass(UserRank.class)
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
        Transaction insertTransaction = null;
        Transaction fetchTransacton = null;
        User fetchedUser = null;

        try {
            insertTransaction = session.beginTransaction();
            final Integer userId = (Integer) session.save(user);
            insertTransaction.commit();

            fetchTransacton = session.beginTransaction();
            fetchedUser = session.get(User.class, userId);
            fetchTransacton.commit();

        } catch (HibernateException e) {
            if (insertTransaction != null) insertTransaction.rollback();
            if (fetchTransacton != null) fetchTransacton.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return fetchedUser;
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
    public User getUser(Integer userId) {
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
    public void updateUsername(int userId, String username) {
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


    private Session openSession() throws JamesNotInitException {
        return this.sessionFactory.orElseThrow(() -> new JamesNotInitException("SessionFactory")).openSession();
    }


    @Override
    public void close() {
        this.sessionFactory.ifPresent(SessionFactory::close);
    }
}
