package eu.psandro.tsjames.io;

import eu.psandro.tsjames.api.exception.ConnectionNotOpenException;
import eu.psandro.tsjames.api.exception.JamesAuthException;
import eu.psandro.tsjames.controller.listener.PingListener;
import eu.psandro.tsjames.io.auth.*;
import eu.psandro.tsjames.io.event.NetEventManager;
import eu.psandro.tsjames.io.handler.PacketProcessingHandler;
import eu.psandro.tsjames.io.protocol.*;
import eu.psandro.tsjames.model.file.NetClientConfig;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.Getter;
import lombok.NonNull;

import javax.net.ssl.SSLException;
import java.util.Set;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */
public final class NetClientImpl extends AbstractNetClient {

    private static final int EVENT_LOOP_GROUP_THREADS = 4;

    private final NetConnection netConnection = new NetConnection();

    private final ResponseSenderHook hook = (response, netSubject) -> {
        NetClientImpl.this.sendPacket(response);
    };

    @Getter
    private final PacketRegistry packetRegistry = new PacketRegistry();
    private final ResponseManager responseManager = new ResponseManager();
    private final NetEventManager netEventManager = new NetEventManager(this.hook);
    @NonNull
    private AuthCredentials authCredentials;
    private EventLoopGroup eventLoopGroup;


    public NetClientImpl(String host, int port, @NonNull AuthCredentials authCredentials) {
        super.host = host;
        super.port = port;
        this.authCredentials = authCredentials;

    }


    public NetClientImpl() {
    }

    public void fillConnectionData(String host, int port, AuthCredentials authCredentials) {
        this.host = host;
        this.port = port;
        this.authCredentials = authCredentials;
    }

    public void fillConnectionData(NetClientConfig config) {
        this.host = config.getHost();
        this.port = config.getPort();
        this.authCredentials = new AuthCredentials(config.getUser(), config.getKey());
    }


    @Override
    public void sendPacket(NetPacket... netPacket) throws ConnectionNotOpenException {
        if (this.netConnection.isOpen()) {
            final Channel channel = this.netConnection.getChannel();
            final NetSubject local = this.netConnection.getSession().getLocalSubject();
            for (NetPacket paket : netPacket) {
                paket.setSender(local);
                channel.write(paket);
            }
            channel.flush();
        }
    }


    @Override
    public void sendPacket(Set<NetPacket> netPackets) {
        if (this.netConnection.isOpen()) {
            final Channel channel = this.netConnection.getChannel();
            final NetSubject local = this.netConnection.getSession().getLocalSubject();
            netPackets.forEach(p -> {
                p.setSender(local);
                channel.write(p);
            });
            channel.flush();
        }
    }

    @Override
    public boolean init() {
        this.cleanUp();
        this.eventLoopGroup = this.createEventLoopGroup(EVENT_LOOP_GROUP_THREADS);
        super.option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.AUTO_READ, true)
                .channel(this.getSocketChannelClazz())
                .group(this.eventLoopGroup)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline()
                                .addLast("lengthBaseDecoder", new NetBaseDecoder());
                    }
                });


        this.netEventManager.registerListener(new PingListener());


        return true;
    }

    @Override
    public boolean establish() throws InterruptedException {
        this.prepareRestart();
        final Channel channel = super
                .remoteAddress(super.getHost(), super.getPort())
                .connect().sync().channel();
        if (channel == null || !channel.isOpen()) return false;
        this.removeSslHandler(channel);
        this.addSslHandler(channel);
        this.netConnection.setChannel(channel);

        final AuthManager authManager = new AuthManager(channel, this.authCredentials);
        final AuthResponse response = authManager.auth(4);

        if (response.isSuccess()) {
            System.out.println("Auth successful! Local NetSubject identifier: " + response.getLocal().getId());
            NetClientImpl.this.netConnection.setSession(NetSessionFactory.createSession(response));

        } else {
            throw new JamesAuthException(response.getErrorMessage());
        }
        this.registerWorkerHandlers(channel.pipeline());

        return true;
    }

    private void addSslHandler(Channel ch) {
        try {
            final SslContext sslContext = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
            ch.pipeline().addBefore("lengthBaseDecoder", "ssl", sslContext.newHandler(ch.alloc(), super.getHost(), super.getPort()));
        } catch (SSLException e) {
            e.printStackTrace();
        }
    }

    private void removeSslHandler(Channel ch) {
        if (ch.pipeline().get("ssl") != null)
            ch.pipeline().remove("ssl");
    }

    private void registerWorkerHandlers(ChannelPipeline channelPipeline) {
        channelPipeline
                .addLast("encoder", new NetPacketEncoder(this.netConnection.getSession().getLocalSubject()))
                .addAfter("lengthBaseDecoder", "decoder", new NetPacketDecoder(this.packetRegistry))
                .addLast("handler", new PacketProcessingHandler(this.netEventManager, this.responseManager, this.netConnection.getSession().getLocalSubject()));
    }

    @Override
    public boolean isRunning() {
        return this.netConnection.isOpen();
    }

    @Override
    public void close() {
        this.cleanUp();
    }

    private void prepareRestart() {
        try {
            this.netConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.packetRegistry.clear();
    }

    private void cleanUp() {
        try {
            this.netConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.eventLoopGroup != null) {
            this.eventLoopGroup.shutdownGracefully();
            this.eventLoopGroup = null;
        }
        this.packetRegistry.clear();
    }


    private EventLoopGroup createEventLoopGroup(int threads) {
        return Epoll.isAvailable() ? new EpollEventLoopGroup(threads) : new NioEventLoopGroup(threads);
    }

    private Class<? extends SocketChannel> getSocketChannelClazz() {
        return Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class;
    }
}
