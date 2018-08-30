package eu.psandro.tsjames.api.exception;

public class NoEventHandlerFoundException extends JamesException {
    public NoEventHandlerFoundException(String message) {
        super(message);
    }

    public NoEventHandlerFoundException(Throwable cause) {
        super(cause);
    }

    public NoEventHandlerFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
