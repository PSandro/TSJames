package eu.psandro.tsjames.daemon.io;

import eu.psandro.tsjames.api.exception.JamesAlreadyInitException;
import eu.psandro.tsjames.daemon.io.auth.AuthHandler;
import eu.psandro.tsjames.io.NetConnection;
import eu.psandro.tsjames.io.auth.AuthRequestDecoder;
import eu.psandro.tsjames.io.auth.AuthResponseEncoder;
import eu.psandro.tsjames.io.auth.NetSessionFactory;
import eu.psandro.tsjames.io.event.NetEventManager;
import eu.psandro.tsjames.io.handler.PacketProcessingHandler;
import eu.psandro.tsjames.io.protocol.*;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;

public final class NetServerImpl extends AbstractNetServer {

    private static final int SUBJECT_ID = 0;

    private EventLoopGroup bossGroup, workerGroup;

    private final NetConnection netConnection = new NetConnection();
    @Getter
    private final PacketRegistry packetRegistry = new PacketRegistry();
    private final NetEventManager netEventManager = new NetEventManager();
    private final ResponseManager responseManager = new ResponseManager();
    private final SessionManager sessionManager = new SessionManager();

    public NetServerImpl(int port) {
        super(port);
    }

    public boolean init() {
        this.cleanUp();
        this.bossGroup = this.createEventLoopGroup();
        this.workerGroup = this.createEventLoopGroup();
        this.netConnection.setSession(NetSessionFactory.createServerSession(NetServerImpl.SUBJECT_ID));

        super.group(this.bossGroup, this.workerGroup)
                .channel(this.getSocketChannelClazz())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) {
                        //TODO SSL
                        ch.pipeline()
                                //Decoder
                                .addLast("lengthBaseDecoder", new NetBaseDecoder())
                                .addLast("authRequestDecoder", new AuthRequestDecoder())
                                .addLast("packetDecoder", new NetPacketDecoder(NetServerImpl.this.packetRegistry))
                                //Encoder
                                .addLast("authResponseEncoder", new AuthResponseEncoder())
                                .addLast("packetEncoder", new NetPacketEncoder(NetServerImpl.this.netConnection.getSession().getLocalSubject()))

                                //Handler
                                .addLast("authHandler", new AuthHandler(NetServerImpl.this.sessionManager, NetServerImpl.this.netConnection.getSession().getLocalSubject()))
                                .addLast("packetHandler", new PacketProcessingHandler(NetServerImpl.this.netEventManager, NetServerImpl.this.responseManager, NetServerImpl.this.netConnection.getSession().getLocalSubject()));


                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .localAddress(super.getPort())
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        return true;
    }

    public boolean establish() throws InterruptedException {
        if (this.netConnection.isOpen()) {
            throw new JamesAlreadyInitException("The NetServer has already been established!");
        }
        final Channel channel = super.bind().sync().channel();
        if (channel == null || !channel.isOpen()) return false;
        this.netConnection.setChannel(channel);

        return true;
    }

    public boolean isRunning() {
        return this.netConnection.isOpen();
    }

    private void cleanUp() {
        try {
            this.netConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.bossGroup != null) {
            this.bossGroup.shutdownGracefully();
            this.bossGroup = null;
        }
        if (this.workerGroup != null) {
            this.workerGroup.shutdownGracefully();
            this.workerGroup = null;
        }
    }

    public void close() {
        this.cleanUp();
    }


    private EventLoopGroup createEventLoopGroup() {
        return Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    }

    private Class<? extends ServerSocketChannel> getSocketChannelClazz() {
        return Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }
}
