package eu.psandro.tsjames.io;

import eu.psandro.tsjames.io.auth.NetSubject;
import eu.psandro.tsjames.io.protocol.NetPacket;
import io.netty.bootstrap.Bootstrap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */

@Getter
public abstract class AbstractNetClient extends Bootstrap implements ManagedConnection {

    protected
    String host;
    protected int port;

    public abstract void sendPacket(NetPacket... netPacket);

    public abstract void sendPacket(NetSubject recipient, NetPacket... netPacket);


}
