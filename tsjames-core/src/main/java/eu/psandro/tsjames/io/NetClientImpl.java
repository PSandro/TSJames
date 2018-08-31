package eu.psandro.tsjames.io;

import eu.psandro.tsjames.api.exception.ConnectionNotOpenException;
import eu.psandro.tsjames.api.exception.JamesAlreadyInitException;
import eu.psandro.tsjames.api.exception.JamesAuthException;
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
import lombok.Getter;
import lombok.NonNull;

/**
 * @author PSandro on 25.08.18
 * @project tsjames
 */
public final class NetClientImpl extends AbstractNetClient {

    private static final int EVENT_LOOP_GROUP_THREADS = 4;

    private final NetConnection netConnection = new NetConnection();
    @Getter
    private final PacketRegistry packetRegistry = new PacketRegistry();
    private final NetEventManager netEventManager = new NetEventManager();
    private final ResponseManager responseManager = new ResponseManager();
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
        //TODO
    }

    @Override
    public void sendPacket(NetSubject recipient, NetPacket... netPacket) throws ConnectionNotOpenException {
        for (NetPacket paket : netPacket) {
            this.netConnection.getChannel();
        }
        //TODO
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
                        //TODO SSL
                        ch.pipeline().addLast("lengthBaseDecoder", new NetBaseDecoder());
                    }
                });


        return true;
    }

    @Override
    public boolean establish() throws InterruptedException {
        this.prepareRestart();
        final Channel channel = super
                .remoteAddress(super.getHost(), super.getPort())
                .connect().sync().channel();
        if (channel == null || !channel.isOpen()) return false;
        this.netConnection.setChannel(channel);

        final AuthManager authManager = new AuthManager(channel, this.authCredentials);
        final AuthResponse response = authManager.auth(4);

        if (response.isSuccess()) {
            System.out.println("Auth successful!");
            NetClientImpl.this.netConnection.setSession(NetSessionFactory.createSession(response));

        } else {
            throw new JamesAuthException(response.getErrorMessage());
        }
        this.registerWorkerHandlers(channel.pipeline());

        return true;
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
