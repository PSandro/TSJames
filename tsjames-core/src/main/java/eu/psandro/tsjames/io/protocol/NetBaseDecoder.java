package eu.psandro.tsjames.io.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author PSandro on 30.08.18
 * @project tsjames
 */

public final class NetBaseDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        if (in.readableBytes() < 5) {
            in.resetReaderIndex();
            return;
        }
        byte magicNumber = in.readByte();
        int size = in.readInt();
        if (in.readableBytes() >= size) {
            in.resetReaderIndex();
            out.add(in.readBytes(size + 5));
        }
    }
}
