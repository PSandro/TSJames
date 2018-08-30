package eu.psandro.tsjames.io.protocol;


import eu.psandro.tsjames.api.exception.JamesPacketAlreadyRegistered;
import eu.psandro.tsjames.api.exception.JamesPacketException;
import eu.psandro.tsjames.api.exception.PacketNotFoundException;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */
public final class PacketRegistry {

    private final Map<Short, Class<? extends NetPacket>> registeredPackets = new HashMap<>();

    public void registerPacket(final short packetId, final Class<? extends NetPacket> packetClazz) throws JamesPacketException {
        if (this.registeredPackets.containsKey(packetId)) {
            throw new JamesPacketAlreadyRegistered(packetId);
        }
        if (this.registeredPackets.containsValue(packetClazz)) {
            throw new JamesPacketAlreadyRegistered(packetClazz);
        }
        if (!this.hasParameterlessConstructor(packetClazz)) {
            throw new JamesPacketException("The protocol does not contain an empty, public accessible constructor!");
        }
        this.registeredPackets.put(packetId, packetClazz);

    }

    protected NetPacket buildPacket(short packetId, ByteBuf data) throws PacketNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!this.registeredPackets.containsKey(packetId)) throw new PacketNotFoundException(packetId);
        final Class<? extends NetPacket> clazz = this.registeredPackets.get(packetId);
        final Constructor constructor = clazz.getConstructor();
        final NetPacket netPacket = (NetPacket) constructor.newInstance();
        netPacket.deepRead(data);
        netPacket.setPacketId(packetId);
        return netPacket;
    }

    public void clear() {
        this.registeredPackets.clear();
    }


    private boolean hasParameterlessConstructor(Class<?> clazz) {
        return Stream.of(clazz.getConstructors())
                .anyMatch((c) -> c.getParameterCount() == 0);
    }
}
