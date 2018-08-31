package eu.psandro.tsjames.io.packet;

import eu.psandro.tsjames.api.exception.JamesIOReadException;
import eu.psandro.tsjames.io.protocol.RespondableNetPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.NoArgsConstructor;

/**
 * @author PSandro on 31.08.18
 * @project tsjames
 */
@NoArgsConstructor
public class PacketPing extends RespondableNetPacket {

    @Override
    protected ByteBuf write() {
        return Unpooled.buffer();
    }

    @Override
    protected void read(ByteBuf data) throws JamesIOReadException {
    }
}
