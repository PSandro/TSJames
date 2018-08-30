package eu.psandro.tsjames.io.protocol;

import eu.psandro.tsjames.io.auth.NetSubject;
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
        byte magicNumber = in.readByte();
        int size = in.readInt();
        if (NetPacket.MAGIC_NUMBER == magicNumber) {
            short packetId = in.readShort();
            int senderId = in.readInt();
            int length = in.readInt();
            final ByteBuf data = in.readBytes(length);
            final NetPacket netPacket = this.packetRegistry.buildPacket(packetId, data);
            netPacket.setSender(NetSubject.byId(senderId));
            out.add(netPacket);
        } else {
            in.resetReaderIndex();
            out.add(in.readBytes(size + 5));
        }
    }
}
