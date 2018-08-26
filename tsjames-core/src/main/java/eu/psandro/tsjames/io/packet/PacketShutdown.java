package eu.psandro.tsjames.io.packet;

import eu.psandro.tsjames.api.exception.JamesIOReadException;
import eu.psandro.tsjames.io.protocol.NetPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.*;

/**
 * @author PSandro on 26.08.18
 * @project tsjames
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
public class PacketShutdown extends NetPacket {

    private String message;
    private long whenTimeStamp;

    protected PacketShutdown(Short packetId) {
        super(packetId);
    }

    @Override
    protected ByteBuf write() {

        int messageLength = 0;
        byte[] messageBytes = null;
        if (this.message != null) {
            messageBytes = this.message.getBytes(NetPacket.CHARSET);
            messageLength = messageBytes.length;
        }

        final ByteBuf buf = Unpooled.buffer(Integer.SIZE / Byte.SIZE + messageLength + Long.SIZE / Byte.SIZE);

        buf.writeLong(this.whenTimeStamp);
        buf.writeInt(messageLength);
        if (messageLength > 0)
            buf.writeBytes(messageBytes);


        return buf;
    }

    @Override
    protected void read(ByteBuf data) throws JamesIOReadException {
        if (data.readableBytes() < ((Long.SIZE + Integer.SIZE) / Byte.SIZE))
            throw new JamesIOReadException("Insufficient bytes!");
        this.whenTimeStamp = data.readLong();
        int messageLength = data.readInt();
        if (data.readableBytes() < messageLength)
            throw new JamesIOReadException("Insufficient bytes!");
        if (messageLength <= 0) {
            this.message = null;
        } else
            this.message = data.readBytes(messageLength).toString(NetPacket.CHARSET);
    }
}
