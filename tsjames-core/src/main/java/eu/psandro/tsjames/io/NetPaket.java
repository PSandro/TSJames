package eu.psandro.tsjames.io;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */
abstract class NetPaket<E> {

    @Getter
    protected final int packetId;

    protected NetPaket(@NonNull Integer packetId) {
        this.packetId = packetId;
    }

    public abstract ByteBuf write();

    public abstract E read();
}
