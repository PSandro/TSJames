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
public class PacketSendMessage extends NetPacket {

    private @NonNull
    String message = "";
    private long recipientUserId;

    protected PacketSendMessage(Short packetId) {
        super(packetId);
    }

    @Override
    protected ByteBuf write() {

        byte[] messageBytes = this.message.getBytes(NetPacket.CHARSET);
        int messageLength = messageBytes.length;

        final ByteBuf buf = Unpooled.buffer((Integer.SIZE + Long.SIZE) / Byte.SIZE + messageLength);

        buf.writeInt(messageLength);
        buf.writeBytes(messageBytes);
        buf.writeLong(this.recipientUserId);

        return buf;
    }

    @Override
    protected void read(ByteBuf data) throws JamesIOReadException {
        if (data.readableBytes() < (Integer.SIZE / Byte.SIZE)) throw new JamesIOReadException("Insufficient bytes!");
        int messageLength = data.readInt();
        if (data.readableBytes() < ((Long.SIZE) / Byte.SIZE) + messageLength)
            throw new JamesIOReadException("Insufficient bytes!");
        this.message = data.readBytes(messageLength).toString(NetPacket.CHARSET);
        this.recipientUserId = data.readLong();
    }
}
