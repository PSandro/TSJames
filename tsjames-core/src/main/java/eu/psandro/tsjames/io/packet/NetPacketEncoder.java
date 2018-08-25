package eu.psandro.tsjames.io.packet;

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
        byte[] data = msg.write();
        int length = data.length;

        out.writeByte(NetPacket.MAGIC_NUMBER);
        out.writeShort(msg.getPacketId());
        out.writeInt(length);
        out.writeBytes(data);

    }
}
