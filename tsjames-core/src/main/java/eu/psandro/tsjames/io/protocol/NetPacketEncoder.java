package eu.psandro.tsjames.io.protocol;

import eu.psandro.tsjames.api.exception.PacketSenderNotLocal;
import eu.psandro.tsjames.io.auth.NetSubject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */
@RequiredArgsConstructor
public final class NetPacketEncoder extends MessageToByteEncoder<NetPacket> {

    private final @NonNull
    NetSubject local;

    @Override
    protected void encode(ChannelHandlerContext ctx, NetPacket msg, ByteBuf out) {
        if (msg == null) return;
        assert this.local != null;
        if (!(this.local.equals(msg.getSender()))) {
            throw new PacketSenderNotLocal(msg.getSender().getId(), this.local.getId());
        }

        final ByteBuf dataBuf = msg.deepWrite();
        int packetDataLength = dataBuf.readableBytes();

        //The total byte length of the data followed by the magic number and this number.
        int totalLength = ((Short.SIZE + Integer.SIZE + Integer.SIZE) / Byte.SIZE) + packetDataLength;

        out.writeByte(NetPacket.MAGIC_NUMBER);
        out.writeInt(totalLength);

        out.writeShort(msg.getPacketId());

        out.writeInt(msg.getSender().getId());
        out.writeInt(packetDataLength);
        out.writeBytes(dataBuf);

        assert out.readableBytes() == (totalLength + 5);


    }
}
