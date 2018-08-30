package eu.psandro.tsjames.io.auth;

import eu.psandro.tsjames.io.protocol.NetPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public final class AuthRequestDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        if (in.readableBytes() < 1) {
            in.resetReaderIndex();
            return;
        }
        byte magicNumber = in.readByte();
        if (AuthRequest.MAGIC_NUMBER == magicNumber) {
            if (in.readableBytes() < 8) {
                in.resetReaderIndex();
            } else {
                int userLength = in.readInt();
                int keyLength = in.readInt();

                if (in.readableBytes() < (userLength + keyLength)) {
                    in.resetReaderIndex();
                }
                final String user = in.readBytes(userLength).toString(NetPacket.CHARSET);
                final String key = in.readBytes(keyLength).toString(NetPacket.CHARSET);


                out.add(new AuthRequest(user, key));
            }
        }
    }
}
