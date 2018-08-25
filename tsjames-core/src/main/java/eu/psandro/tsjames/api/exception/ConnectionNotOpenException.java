package eu.psandro.tsjames.api.exception;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */
public final class ConnectionNotOpenException extends JamesException {

    public ConnectionNotOpenException() {
        super("The connection is not open!");
    }
}
