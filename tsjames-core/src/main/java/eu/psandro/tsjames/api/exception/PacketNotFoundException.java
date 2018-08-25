package eu.psandro.tsjames.api.exception;

import eu.psandro.tsjames.io.packet.NetPacket;

/**
 * @author PSandro on 26.08.18
 * @project tsjames
 */
public final class PacketNotFoundException extends JamesPacketException {

    public PacketNotFoundException(short id) {
        super("A packet with the packet id " + id + " was not found!");
    }

    public PacketNotFoundException(Class<? extends NetPacket> clazz) {
        super("A packet with the Class " + clazz.getSimpleName() + " was not found!");
    }
}
