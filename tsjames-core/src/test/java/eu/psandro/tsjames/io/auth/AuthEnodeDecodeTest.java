package eu.psandro.tsjames.io.auth;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class AuthEnodeDecodeTest {

    private final EmbeddedChannel responseChannel = new EmbeddedChannel(new AuthResponseDecoder(), new AuthResponseEncoder());
    private final EmbeddedChannel requestChannel = new EmbeddedChannel(new AuthRequestEncoder(), new AuthRequestDecoder());


    @Test
    void shouldEncodeAndDecodeAuthRequest() {
        final AuthRequest authRequest = new AuthRequest("hi", "test");
        final AuthRequest testedRequest = this.encodeDecode(authRequest, requestChannel);


        assertEquals(testedRequest, authRequest);
    }

    @Test
    void shouldEncodeAndDecodeAuthResponseError() {
        final AuthResponse authResponse = new AuthResponse("Fehler", new NetSubject(1), new NetSubject(0));
        final AuthResponse testedResponse = this.encodeDecode(authResponse, responseChannel);


        assertEquals(testedResponse, authResponse);
        assertFalse(testedResponse.isSuccess());
        assertFalse(authResponse.isSuccess());
    }

    @Test
    void shouldEncodeAndDecodeAuthResponseSuccess() {
        final AuthResponse authResponse = new AuthResponse(null, new NetSubject(1), new NetSubject(0));
        final AuthResponse testedResponse = this.encodeDecode(authResponse, responseChannel);


        assertEquals(testedResponse, authResponse);
        assertTrue(testedResponse.isSuccess());
        assertTrue(authResponse.isSuccess());
    }


    private <E> E encodeDecode(E object, EmbeddedChannel channel) {
        channel.writeOutbound(object);
        channel.writeInbound((Object) channel.readOutbound());
        return channel.readInbound();
    }
}
