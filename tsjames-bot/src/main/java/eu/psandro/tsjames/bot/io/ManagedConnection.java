package eu.psandro.tsjames.bot.io;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */
public abstract class ManagedConnection implements Closeable {

    public abstract boolean init();

    public abstract boolean establish() throws IOException;

    public abstract boolean isRunning();

}
