package eu.psandro.tsjames.io.handler;

import eu.psandro.tsjames.io.event.NetEventManager;
import eu.psandro.tsjames.io.protocol.NetPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * @author PSandro on 26.08.18
 * @project tsjames
 */
@AllArgsConstructor
public final class PacketProcessingHandler extends ChannelInboundHandlerAdapter {

    private final @NonNull
    NetEventManager netEventManager;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (!(msg instanceof NetPacket)) return;

        final NetPacket packet = (NetPacket) msg;

        //Calls all registered Event Listener for this packet type
        this.netEventManager.executePacketEvent(packet);

    }
}
