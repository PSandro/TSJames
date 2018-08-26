package eu.psandro.tsjames.io.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */
public final class NetPacketEncoder extends MessageToByteEncoder<NetPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, NetPacket msg, ByteBuf out) throws Exception {
        final ByteBuf dataBuf = msg.write();

        int length = dataBuf.readableBytes();

        out.writeByte(NetPacket.MAGIC_NUMBER);
        out.writeShort(msg.getPacketId());
        out.writeInt(length);
        out.writeBytes(dataBuf);

    }
}
