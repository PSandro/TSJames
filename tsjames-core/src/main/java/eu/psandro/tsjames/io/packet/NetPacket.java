package eu.psandro.tsjames.io.packet;

import eu.psandro.tsjames.api.exception.JamesIOReadException;
import io.netty.buffer.ByteBuf;
import lombok.*;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */
public abstract class NetPacket {

    public static final byte MAGIC_NUMBER = 'P';

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    private short packetId;

    protected NetPacket(@NonNull Short packetId) {
        this.packetId = packetId;
    }


    abstract byte[] write();

    abstract void read(ByteBuf data) throws JamesIOReadException;
}
