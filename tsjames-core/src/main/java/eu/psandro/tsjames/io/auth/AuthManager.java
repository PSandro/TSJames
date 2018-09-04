package eu.psandro.tsjames.io.auth;


import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public final class AuthManager {

    private final CountDownLatch latch = new CountDownLatch(1);
    private AuthResponse authResponse;

    @NonNull
    private final Channel channel;
    @NonNull
    private final AuthCredentials credentials;

    protected void setAuthResponse(AuthResponse authResponse) {
        this.authResponse = authResponse;
        this.latch.countDown();
    }

    public AuthResponse auth(int seconds) throws InterruptedException {
        this.requestAuthSync();
        this.latch.await(seconds, TimeUnit.SECONDS);
        this.cleanupAuth();
        return this.authResponse;
    }

    private void requestAuthSync() {
        this.channel.pipeline()
                .addLast("authEncoder", new AuthRequestEncoder())
                .addAfter("lengthBaseDecoder", "authDecoder", new AuthResponseDecoder())
                .addLast("authHandler", new AuthProcessingHandler(this));


        final AuthRequest request = NetSessionFactory.createAuthRequest(this.credentials);
        this.channel.writeAndFlush(request);
    }

    private void cleanupAuth() {
        final ChannelPipeline channelPipeline = this.channel.pipeline();
        channelPipeline.remove("authEncoder");
        channelPipeline.remove("authDecoder");
        channelPipeline.remove("authHandler");
    }
}