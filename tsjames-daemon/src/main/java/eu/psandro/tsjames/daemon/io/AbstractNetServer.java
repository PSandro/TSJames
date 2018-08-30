package eu.psandro.tsjames.daemon.io;

import eu.psandro.tsjames.io.ManagedConnection;
import io.netty.bootstrap.ServerBootstrap;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class AbstractNetServer extends ServerBootstrap implements ManagedConnection {

    @Getter
    private final int port;

}
