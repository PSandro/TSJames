package eu.psandro.tsjames.io.auth;

import eu.psandro.tsjames.io.protocol.NetPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public final class AuthResponseEncoder extends MessageToByteEncoder<AuthResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, AuthResponse msg, ByteBuf out) throws Exception {


        final String message = msg.getErrorMessage();

        int messageLength = 0;
        byte[] messageBytes = null;
        if (message != null) {
            messageBytes = message.getBytes(NetPacket.CHARSET);
            messageLength = messageBytes.length;
        }

        int localId = msg.getLocal() == null ? -1 : msg.getLocal().getId();
        int serverId = msg.getServer() == null ? -1 : msg.getServer().getId();


        out.writeByte(AuthResponse.MAGIC_NUMBER);
        out.writeInt((Integer.SIZE + Integer.SIZE + Integer.SIZE) / Byte.SIZE + messageLength);
        out.writeInt(localId);
        out.writeInt(serverId);
        out.writeInt(messageLength);
        if (messageLength > 0)
            out.writeBytes(messageBytes);


    }
}
