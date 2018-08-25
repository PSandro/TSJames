package eu.psandro.tsjames.io;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public interface ManagedConnection extends Closeable {

    boolean init();

    boolean establish() throws IOException, InterruptedException;

    boolean isRunning();

}
