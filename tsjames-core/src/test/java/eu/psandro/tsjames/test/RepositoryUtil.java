package eu.psandro.tsjames.test;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

public class RepositoryUtil {


    public static SessionFactory createTestSessionFactory(Class<?>... annotatedClasses) {
        final Properties settings = new Properties();
        settings.setProperty(Environment.URL, "jdbc:h2:mem:test?MODE=MYSQL");
        settings.setProperty(Environment.DRIVER, "org.h2.Driver");
        settings.setProperty(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
        settings.setProperty(Environment.HBM2DDL_AUTO, "create");
        settings.setProperty(Environment.SHOW_SQL, "true");

        Configuration configuration = new Configuration()
                .addProperties(settings);
        for (Class<?> aClass : annotatedClasses) {
            configuration.addAnnotatedClass(aClass);
        }
        return configuration.buildSessionFactory();
    }

}
