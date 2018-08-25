package eu.psandro.tsjames.api.exception;

import eu.psandro.tsjames.io.packet.NetPacket;

/**
 * @author PSandro on 26.08.18
 * @project tsjames
 */
public final class JamesPacketAlreadyRegistered extends JamesPacketException {

    public JamesPacketAlreadyRegistered(short id) {
        super("A packet with the packet id " + id + " has already been registered!");
    }

    public JamesPacketAlreadyRegistered(Class<? extends NetPacket> clazz) {
        super("A packet with the Class " + clazz.getSimpleName() + " has already been registered!");
    }
}
