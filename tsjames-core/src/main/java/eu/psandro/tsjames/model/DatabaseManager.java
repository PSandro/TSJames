package eu.psandro.tsjames.model;

import eu.psandro.tsjames.api.exception.JamesNotInitException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.util.Optional;

public final class DatabaseManager {

    private final Configuration cfg;
    private Optional<SessionFactory> sessionFactory;

    public DatabaseManager(DatabaseAccessData accessData) {

        this.cfg = new Configuration().configure(new File("hibernate.cfg.xml"))
                .setProperty("hibernate.connection.url", accessData.getUrl())
                .setProperty("hibernate.connection.username", accessData.getUsername())
                .setProperty("hibernate.connection.password", accessData.getPassword())
                .configure();

    }

    public void init() throws HibernateException {
        this.sessionFactory = Optional.ofNullable(this.cfg.buildSessionFactory());
    }

    private Session openSession() throws JamesNotInitException {
        return this.sessionFactory.orElseThrow(() -> new JamesNotInitException("SessionFactory")).openSession();
    }


}
