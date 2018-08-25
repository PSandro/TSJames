package eu.psandro.tsjames.io;

import io.netty.bootstrap.Bootstrap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */

@AllArgsConstructor
@Getter
public abstract class AbstractNetClient extends Bootstrap implements ManagedConnection {

    private final @NonNull
    String host;
    private final int port;

    public abstract void sendPacket(NetPaket... netPaket);

    public abstract void sendPacket(NetSubject recipient, NetPaket... netPaket);


}
