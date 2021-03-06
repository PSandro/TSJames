package eu.psandro.tsjames.io.packet;

import eu.psandro.tsjames.io.auth.NetSubject;
import eu.psandro.tsjames.io.protocol.*;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @author PSandro on 26.08.18
 * @project tsjames
 */
public class PacketEncodeDecodeTest {


    private final PacketRegistry packetRegistry = new PacketRegistry();
    private final NetSubject testSubject = NetSubject.byId(1);
    private final EmbeddedChannel channel = new EmbeddedChannel(new NetPacketEncoder(testSubject), new NetBaseDecoder(), new NetPacketDecoder(packetRegistry));

    @BeforeEach
    void clearRegistry() {
        packetRegistry.clear();
    }

    @Test
    void shouldEncodeAndDecodeSendMessage() {
        this.packetRegistry.registerPacket((short) 1, PacketSendMessage.class);

        final PacketSendMessage packet = new PacketSendMessage((short) 1);
        packet.setSender(this.testSubject);
        packet.setMessage("Hi");
        packet.setRecipientUserId(4646565041L);

        final PacketSendMessage decodedPacket = this.encodeDecode(packet);

        assertNotNull(decodedPacket);
        assertEquals(packet.getPacketId(), decodedPacket.getPacketId());
        assertEquals(packet.getMessage(), decodedPacket.getMessage());
        assertEquals(packet.getRecipientUserId(), decodedPacket.getRecipientUserId());
        assertEquals(packet, decodedPacket);
    }

    @Test
    void shouldEncodeAndDecodeShutdownm() {
        this.packetRegistry.registerPacket((short) 1, PacketShutdown.class);

        final PacketShutdown packet = new PacketShutdown((short) 1);
        packet.setSender(this.testSubject);
        packet.setMessage("The system is going down!");
        packet.setWhenTimeStamp(System.currentTimeMillis() + 1000L);

        final PacketShutdown decodedPacket = this.encodeDecode(packet);

        assertNotNull(decodedPacket);
        assertEquals(packet.getPacketId(), decodedPacket.getPacketId());
        assertEquals(packet.getMessage(), decodedPacket.getMessage());
        assertEquals(packet.getWhenTimeStamp(), decodedPacket.getWhenTimeStamp());
        assertEquals(packet, decodedPacket);
    }

    private <E extends NetPacket> E encodeDecode(E object) {
        this.channel.writeOutbound(object);
        this.channel.writeInbound((Object) this.channel.readOutbound());
        return this.channel.readInbound();
    }

}