package eu.psandro.tsjames.io.event;

import eu.psandro.tsjames.io.protocol.NetPacket;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class NetPacketEvent<T extends NetPacket> {
    public abstract void read(T packet);
}
