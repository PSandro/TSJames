package eu.psandro.tsjames.io.auth;

import eu.psandro.tsjames.io.protocol.NetPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class AuthRequestEncoder extends MessageToByteEncoder<AuthRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, AuthRequest msg, ByteBuf out) throws Exception {

        final String user = msg.getAuthUser();
        final String key = msg.getAuthPass();

        int userLength = 0;
        int keyLength = 0;

        byte[] userBytes = null;
        byte[] keyBytes = null;


        if (user != null) {
            userBytes = user.getBytes(NetPacket.CHARSET);
            userLength = userBytes.length;
        }
        if (key != null) {
            keyBytes = key.getBytes(NetPacket.CHARSET);
            keyLength = keyBytes.length;
        }


        out.writeByte(AuthRequest.MAGIC_NUMBER);
        out.writeInt(userLength);
        out.writeInt(keyLength);

        if (userLength > 0)
            out.writeBytes(userBytes);

        if (keyLength > 0)
            out.writeBytes(keyBytes);
    }


}
