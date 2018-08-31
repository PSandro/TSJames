package eu.psandro.tsjames.daemon.io;

import eu.psandro.tsjames.io.ManagedConnection;
import eu.psandro.tsjames.io.auth.NetSubject;
import eu.psandro.tsjames.io.protocol.NetPacket;
import io.netty.bootstrap.ServerBootstrap;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
public abstract class AbstractNetServer extends ServerBootstrap implements ManagedConnection {

    @Getter
    private final int port;

    public abstract void sendPacket(NetSubject target, NetPacket netPacket);

    public abstract void sendPacket(NetSubject target, Set<NetPacket> netPacket);

}
