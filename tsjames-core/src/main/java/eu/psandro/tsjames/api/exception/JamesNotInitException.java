package eu.psandro.tsjames.api.exception;

public final class JamesNotInitException extends JamesException {
    public JamesNotInitException(String object) {
        super(String.format("%s has not been initlialized yet.", object));
    }
}
