package eu.psandro.tsjames.io.auth;

import eu.psandro.tsjames.io.protocol.NetPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public final class AuthResponseDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        byte magicNumber = in.readByte();
        int size = in.readInt();
        if (AuthResponse.MAGIC_NUMBER == magicNumber) {
            int localId = in.readInt();
            int serverId = in.readInt();
            int length = in.readInt();
            String errorMessage = null;

            if (length > 0) {
                errorMessage = in.readBytes(length).toString(NetPacket.CHARSET);
            }

            final NetSubject localSubject = localId == -1 ? null : new NetSubject(localId);
            final NetSubject serverSubject = serverId == -1 ? null : new NetSubject(serverId);

            out.add(new AuthResponse(errorMessage, localSubject, serverSubject));
        } else {
            in.resetReaderIndex();
            out.add(in.readBytes(size + 5));
        }
    }
}
