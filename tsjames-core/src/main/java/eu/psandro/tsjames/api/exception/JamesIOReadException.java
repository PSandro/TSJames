package eu.psandro.tsjames.api.exception;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */
public class JamesIOReadException extends JamesException {
    public JamesIOReadException(String message) {
        super(message);
    }

    public JamesIOReadException(Throwable cause) {
        super(cause);
    }

    public JamesIOReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
