package eu.psandro.tsjames.daemon.io.auth;

import eu.psandro.tsjames.daemon.io.SessionManager;
import eu.psandro.tsjames.io.auth.AuthRequest;
import eu.psandro.tsjames.io.auth.AuthResponse;
import eu.psandro.tsjames.io.auth.NetSubject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author PSandro on 30.08.18
 * @project tsjames
 */
@RequiredArgsConstructor
public final class AuthHandler extends SimpleChannelInboundHandler<AuthRequest> {

    @NonNull
    private final SessionManager sessionManager;

    @NonNull
    private final NetSubject serverSubject;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel Registered: " + ctx.channel().remoteAddress().toString());
        super.channelRegistered(ctx);
    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel active: " + ctx.channel().remoteAddress().toString());
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AuthRequest request) throws Exception {


        AuthResponse authResponse = null;
        if (request.getAuthUser().equals(request.getAuthPass())) {//TODO change

            final NetSubject authSubject = this.sessionManager.registerSession(ctx.channel());
            authResponse = new AuthResponse(null, authSubject, this.serverSubject);

        } else {
            authResponse = new AuthResponse("Wrong credentials", null, null);
        }
        ctx.writeAndFlush(authResponse);
        System.out.println("Auth: " + (authResponse.isSuccess() ? "success" : "failed" + " for " + ctx.channel().remoteAddress().toString()));
    }

}
