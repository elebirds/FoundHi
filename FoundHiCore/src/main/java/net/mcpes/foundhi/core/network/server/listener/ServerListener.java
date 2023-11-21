package net.mcpes.foundhi.core.network.server.listener;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import net.mcpes.foundhi.core.app.APIFunction;
import net.mcpes.foundhi.core.network.Network;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hhm @ SGameCore Project
 **/
public class ServerListener {
    public static ConcurrentHashMap<Channel, Long> overTime = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Channel, Integer> overCount = new ConcurrentHashMap<>();
    private Network network;
    private SelfSignedCertificate ssc;
    private SslContext sslCtx;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerInitializer initializer;

    public ServerListener(Network network) throws Exception {
        this.network = network;
        ssc = new SelfSignedCertificate();
        sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
    }

    public Network getNetwork() {
        return network;
    }

    public ServerInitializer getInitializer() {
        return initializer;
    }

    public ServerHandler getHander() {
        return getInitializer().getHandler();
    }

    public void start() throws Exception {
        try {
            initializer = new ServerInitializer(sslCtx);
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(initializer)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            b.bind(APIFunction.getInstance().getServerAPIPort()).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void shutdown() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
