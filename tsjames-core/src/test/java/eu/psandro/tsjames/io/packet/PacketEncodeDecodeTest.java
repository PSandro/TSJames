package eu.psandro.tsjames.io.packet;

import eu.psandro.tsjames.io.protocol.NetPacket;
import eu.psandro.tsjames.io.protocol.NetPacketDecoder;
import eu.psandro.tsjames.io.protocol.NetPacketEncoder;
import eu.psandro.tsjames.io.protocol.PacketRegistry;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @author PSandro on 26.08.18
 * @project tsjames
 */
class PacketEncodeDecodeTest {


    private final PacketRegistry packetRegistry = new PacketRegistry();
    private final EmbeddedChannel channel = new EmbeddedChannel(new NetPacketEncoder(), new NetPacketDecoder(packetRegistry));

    @BeforeEach
    void clearRegistry() {
        packetRegistry.clear();
    }

    @Test
    void shouldEncodeAndDecode() {
        this.packetRegistry.registerPacket((short) 1, PacketSendMessage.class);

        final PacketSendMessage packet = new PacketSendMessage((short) 1);

        final PacketSendMessage decodedPacket = this.encodeDecode(packet);

        assertNotNull(decodedPacket);
        assertEquals(packet.getPacketId(), decodedPacket.getPacketId());
        assertEquals(packet.getMessage(), decodedPacket.getMessage());
        assertEquals(packet.getRecipientUserId(), decodedPacket.getRecipientUserId());
        assertEquals(packet, decodedPacket);
    }

    private <E extends NetPacket> E encodeDecode(E object) {
        this.channel.writeOutbound(object);
        this.channel.writeInbound((Object) this.channel.readOutbound());
        return this.channel.readInbound();
    }

}