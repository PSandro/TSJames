package eu.psandro.tsjames.daemon.io.auth;

import eu.psandro.tsjames.io.auth.AuthRequest;
import eu.psandro.tsjames.io.auth.AuthResponse;
import eu.psandro.tsjames.io.auth.NetSubject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author PSandro on 30.08.18
 * @project tsjames
 */
public final class AuthHandler extends ChannelInboundHandlerAdapter {

    private final AtomicInteger subjectIds = new AtomicInteger(1);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel Registered: " + ctx.channel().remoteAddress().toString());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof AuthRequest) {
            final AuthRequest request = (AuthRequest) msg;
            AuthResponse authResponse = null;
            if (request.getAuthUser() == request.getAuthPass()) {
                authResponse = new AuthResponse(null, NetSubject.byId(subjectIds.getAndIncrement()), NetSubject.byId(0));
            } else {
                authResponse = new AuthResponse("Wrong credentials", null, null);
            }
            ctx.writeAndFlush(authResponse);
            System.out.println("Sent auth response: " + authResponse.toString());
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
