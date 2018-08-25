package eu.psandro.tsjames.io;


import eu.psandro.tsjames.api.exception.ConnectionNotOpenException;
import io.netty.channel.Channel;
import lombok.NonNull;


/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */

public final class NetConnection implements AutoCloseable {

    private Channel channel;

    public void setChannel(@NonNull Channel channel) throws ConnectionNotOpenException {
        if (!channel.isOpen()) {
            throw new ConnectionNotOpenException();
        }
        this.channel = channel;
    }

    public Channel getChannel() throws ConnectionNotOpenException {
        if (!this.isOpen()) throw new ConnectionNotOpenException();
        return this.channel;
    }


    public boolean hasChannel() {
        return this.channel != null;
    }

    public boolean isOpen() {

        return this.channel != null && this.channel.isOpen();
    }

    @Override
    public void close() throws Exception {
        if (this.channel != null) {
            this.channel.close();
            this.channel = null;
        }
    }
}
