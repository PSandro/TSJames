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
        if (in.readableBytes() < 1) {
            in.resetReaderIndex();
            return;
        }
        byte magicNumber = in.readByte();
        if (AuthResponse.MAGIC_NUMBER == magicNumber) {
            if (in.readableBytes() < 12) {
                in.resetReaderIndex();
            } else {
                int localId = in.readInt();
                int serverId = in.readInt();
                int length = in.readInt();
                String errorMessage = null;

                if (length > 0) {
                    if (in.readableBytes() < length) {
                        in.resetReaderIndex();
                    } else {
                        errorMessage = in.readBytes(length).toString(NetPacket.CHARSET);
                    }
                }

                out.add(new AuthResponse(errorMessage, new NetSubject(localId), new NetSubject(serverId)));
            }
        }
    }
}
