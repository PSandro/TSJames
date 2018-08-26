package eu.psandro.tsjames.io.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */

@AllArgsConstructor
public final class NetPacketDecoder extends ByteToMessageDecoder {

    private final PacketRegistry packetRegistry;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        if (in.readableBytes() < 1) {
            in.resetReaderIndex();
            return;
        }
        byte magicNumber = in.readByte();
        if (NetPacket.MAGIC_NUMBER == magicNumber) {
            if (in.readableBytes() < 6) {//Not enough Data for packetId and length
                in.resetReaderIndex();
            } else {
                short packetId = in.readShort();
                int length = in.readInt();
                if (in.readableBytes() < length) {
                    in.resetReaderIndex();
                } else {
                    final ByteBuf data = in.readBytes(length);
                    final NetPacket netPacket = this.packetRegistry.buildPacket(packetId, data);
                    out.add(netPacket);
                }
            }
        }
    }
}