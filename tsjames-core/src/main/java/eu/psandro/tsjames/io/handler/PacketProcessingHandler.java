package eu.psandro.tsjames.io.handler;

import eu.psandro.tsjames.io.auth.NetSubject;
import eu.psandro.tsjames.io.event.NetEventManager;
import eu.psandro.tsjames.io.protocol.NetPacket;
import eu.psandro.tsjames.io.protocol.RespondableNetPacket;
import eu.psandro.tsjames.io.protocol.ResponseManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * @author PSandro on 26.08.18
 * @project tsjames
 */
@AllArgsConstructor
public final class PacketProcessingHandler extends SimpleChannelInboundHandler<NetPacket> {

    private final @NonNull
    NetEventManager netEventManager;

    private final @NonNull
    ResponseManager responseManager;

    private final @NonNull
    NetSubject local;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NetPacket packet) throws Exception {
        if (packet instanceof RespondableNetPacket) {
            final RespondableNetPacket respondablePacket = (RespondableNetPacket) packet;
            if (respondablePacket.getResponseTarget().equals(this.local)) {
                this.responseManager.call(respondablePacket.getRespondId(), respondablePacket);
                return;
            }
        }

        //Calls all registered Event Listener for this packet type
        this.netEventManager.executePacketToEvent(packet);
    }
}
