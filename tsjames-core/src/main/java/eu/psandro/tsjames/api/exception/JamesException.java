package eu.psandro.tsjames.api.exception;

public class JamesException extends RuntimeException {

    public JamesException(String message) {
        super(message);
    }

    public JamesException(Throwable cause) {
        super(cause);
    }

    public JamesException(String message, Throwable cause) {
        super(message, cause);
    }
}
