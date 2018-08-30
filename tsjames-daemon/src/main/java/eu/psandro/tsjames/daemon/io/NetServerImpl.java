package eu.psandro.tsjames.daemon.io;

import eu.psandro.tsjames.api.exception.JamesAlreadyInitException;
import eu.psandro.tsjames.io.NetConnection;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;

public final class NetServerImpl extends AbstractNetServer {

    private EventLoopGroup bossGroup, workerGroup;

    private final NetConnection netConnection = new NetConnection();

    public NetServerImpl(int port) {
        super(port);
    }

    public boolean init() {
        this.cleanUp();
        this.bossGroup = this.createEventLoopGroup();
        this.workerGroup = this.createEventLoopGroup();

        super.group(this.bossGroup, this.workerGroup)
                .channel(this.getSocketChannelClazz())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //TODO
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .localAddress(super.getPort())
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        return true;
    }

    public boolean establish() throws IOException, InterruptedException {
        if (this.netConnection.isOpen()) {
            throw new JamesAlreadyInitException("The NetServer has already been established!");
        }
        final Channel channel = super.bind().sync().channel();
        if (channel == null || !channel.isOpen()) return false;
        this.netConnection.setChannel(channel);

        return false;
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

    public void close() throws IOException {
        this.cleanUp();
    }


    private EventLoopGroup createEventLoopGroup() {
        return Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    }

    private Class<? extends ServerSocketChannel> getSocketChannelClazz() {
        return Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }
}
