package eu.psandro.tsjames.api.exception;

/**
 * @author PSandro on 26.08.18
 * @project tsjames
 */
public class JamesPacketException extends JamesException {
    public JamesPacketException(String message) {
        super(message);
    }

    public JamesPacketException(Throwable cause) {
        super(cause);
    }

    public JamesPacketException(String message, Throwable cause) {
        super(message, cause);
    }
}
