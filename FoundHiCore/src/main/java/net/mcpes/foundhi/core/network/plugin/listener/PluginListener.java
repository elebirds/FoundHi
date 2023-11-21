package net.mcpes.foundhi.core.network.plugin.listener;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import net.mcpes.foundhi.core.network.Network;
import net.mcpes.foundhi.core.network.protocol.DataPacket;
import net.mcpes.foundhi.core.plugin.FoundHiPlugin;

import javax.net.ssl.SSLException;
import java.nio.charset.Charset;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author hhm @ SGameCore Project
 **/
public class PluginListener {
    public static CopyOnWriteArrayList<DataPacket> sendPackets = new CopyOnWriteArrayList<>();
    public ChannelFuture lastWriteFuture = null;
    private Channel channel;
    private Bootstrap b = new Bootstrap();
    private Network network;
    private EventLoopGroup group = new NioEventLoopGroup();
    private boolean r = true;

    public PluginListener(Network network) {
        this.network = network;
    }

    public Channel getChannel() {
        return channel;
    }

    public void start() throws InterruptedException, SSLException {
        try {
            final SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new PluginInitializer(sslCtx))
                    .option(ChannelOption.SO_KEEPALIVE, true);
            // Start the connection attempt.
            this.doConnect();
            // Wait until all messages are flushed before closing the channel.
            while (this.r) {
                Thread.sleep(1000);
                if (channel != null) {
                    for (DataPacket pk : sendPackets) {
                        String s = pk.toString();
                        if (s.contains("[APǚξλ]")) continue;
                        lastWriteFuture = channel.writeAndFlush(s + "[APǚξλ]\n");
                        sendPackets.remove(pk);
                    }
                }
            }
        } finally {
            if (channel != null) {
                channel.closeFuture().sync();
                channel.close();
            }
            group.shutdownGracefully();
        }
    }

    public void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }
        ChannelFuture cf = b.connect(FoundHiPlugin.getInstance().getServerAPIIP(), FoundHiPlugin.getInstance().getServerAPIPort());
        cf.addListener((ChannelFutureListener) futureListener -> {
            if (futureListener.isSuccess()) {
                channel = futureListener.sync().channel();
                channel.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
                FoundHiPlugin.getInstance().setOverTime(System.currentTimeMillis());
                cn.nukkit.Server.getInstance().getLogger().info("成功连接到API服务器");
            } else {
                cn.nukkit.Server.getInstance().getLogger().info("连接API服务器失败,十秒后重试");
                futureListener.channel().eventLoop().schedule(this::doConnect, 10, TimeUnit.SECONDS);
            }
        });
    }

    public Network getNetwork() {
        return network;
    }

    public void shutdown() {
        r = false;
        try {
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        channel.close();
        group.shutdownGracefully();
    }

    public void restart() {
        if (channel != null) {
            try {
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            channel.close();
        }
        this.doConnect();
    }

    public void dataPacket(DataPacket dataPacket) {
        sendPackets.add(dataPacket);
    }
}
