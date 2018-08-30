package eu.psandro.tsjames.io.auth;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class AuthProcessingHandler extends ChannelInboundHandlerAdapter {

    private final @NonNull
    AuthManager authManager;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Waiting for Auth Response...");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (!(msg instanceof AuthResponse)) return;
        System.out.println("Received Auth Response!");
        final AuthResponse auth = (AuthResponse) msg;
        this.authManager.setAuthResponse(auth);

        super.channelRead(ctx, msg);

    }


}
