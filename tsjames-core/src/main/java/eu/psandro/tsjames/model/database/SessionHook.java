package eu.psandro.tsjames.model.database;

import org.hibernate.Session;

/**
 * @author PSandro on 09.08.18
 * @project tsjames
 */
@FunctionalInterface
public interface SessionHook {
    void use(Session session);
}
