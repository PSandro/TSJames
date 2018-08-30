package eu.psandro.tsjames.io;

import eu.psandro.tsjames.api.exception.ConnectionNotOpenException;
import eu.psandro.tsjames.api.exception.JamesAlreadyInitException;
import eu.psandro.tsjames.io.auth.NetSubject;
import eu.psandro.tsjames.io.event.NetEventManager;
import eu.psandro.tsjames.io.handler.PacketProcessingHandler;
import eu.psandro.tsjames.io.protocol.NetPacket;
import eu.psandro.tsjames.io.protocol.NetPacketDecoder;
import eu.psandro.tsjames.io.protocol.NetPacketEncoder;
import eu.psandro.tsjames.io.protocol.PacketRegistry;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;

import java.io.IOException;

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
    private EventLoopGroup eventLoopGroup;

    public NetClientImpl(String host, int port) {
        super(host, port);

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
                .remoteAddress(super.getHost(), super.getPort())
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        //TODO init Channel (+maybe SSL)
                        ch.pipeline()
                                .addLast("encoder", new NetPacketEncoder())
                                .addLast("decoder", new NetPacketDecoder(NetClientImpl.this.packetRegistry))
                                .addLast(new PacketProcessingHandler(NetClientImpl.this.netEventManager));

                    }
                });


        return true;
    }

    @Override
    public boolean establish() throws IOException, InterruptedException {
        if (this.netConnection.isOpen()) {
            throw new JamesAlreadyInitException("The NetClient has already been established!");
        }
        final Channel channel = super.connect().sync().channel();
        //TODO maybe authorize?
        if (channel == null || !channel.isOpen()) return false;
        this.netConnection.setChannel(channel);
        return true;
    }

    @Override
    public boolean isRunning() {
        return this.netConnection.isOpen();
    }

    @Override
    public void close() throws IOException {
        this.cleanUp();
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
