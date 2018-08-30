package eu.psandro.tsjames.io.auth;

import eu.psandro.tsjames.io.protocol.NetPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public final class AuthResponseEncoder extends MessageToByteEncoder<AuthResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, AuthResponse msg, ByteBuf out) throws Exception {

        out.writeByte(AuthResponse.MAGIC_NUMBER);
        out.writeInt(msg.getLocal().getId());
        out.writeInt(msg.getServer().getId());

        final String message = msg.getErrorMessage();

        int messageLength = 0;
        byte[] messageBytes = null;
        if (message != null) {
            messageBytes = message.getBytes(NetPacket.CHARSET);
            messageLength = messageBytes.length;
        }
        out.writeInt(messageLength);
        if (messageLength > 0)
            out.writeBytes(messageBytes);

    }
}
