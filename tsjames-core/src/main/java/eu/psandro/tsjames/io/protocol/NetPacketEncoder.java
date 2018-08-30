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
    protected void encode(ChannelHandlerContext ctx, NetPacket msg, ByteBuf out) throws Exception {

        if (!(this.local.equals(msg.getSender()))) {
            throw new PacketSenderNotLocal(msg.getSender().getId(), this.local.getId());
        }

        final ByteBuf dataBuf = msg.deepWrite();
        int length = dataBuf.readableBytes();

        out.writeByte(NetPacket.MAGIC_NUMBER);
        out.writeShort(msg.getPacketId());
        out.writeInt(msg.getSender().getId());
        out.writeInt(length);
        out.writeBytes(dataBuf);

    }
}
