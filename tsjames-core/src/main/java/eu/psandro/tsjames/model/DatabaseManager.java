package eu.psandro.tsjames.model;

import eu.psandro.tsjames.api.exception.JamesNotInitException;
import eu.psandro.tsjames.rank.RankData;
import eu.psandro.tsjames.user.*;
import lombok.NonNull;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Optional;
import java.util.Properties;

public final class DatabaseManager implements AutoCloseable {

    private final DatabaseAccessData accessData;
    private Optional<SessionFactory> sessionFactory;

    public DatabaseManager(DatabaseAccessData accessData) {
        this.accessData = accessData;
    }

    public void init() throws HibernateException {
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
        this.sessionFactory = Optional.ofNullable(sessionFactory);
    }

    public Session openSession() throws JamesNotInitException {
        return this.sessionFactory.orElseThrow(() -> new JamesNotInitException("SessionFactory")).openSession();
    }


    @Override
    public void close() {
        this.sessionFactory.ifPresent(SessionFactory::close);
    }
}
