package net.mcpes.foundhi.core.network.server.listener;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.mcpes.foundhi.core.app.APIFunction;
import net.mcpes.foundhi.core.app.data.server.Server;
import net.mcpes.foundhi.core.network.analysiser.PacketAnalysiser;
import net.mcpes.foundhi.core.network.handle.DataPacketHandle;
import net.mcpes.foundhi.core.network.protocol.DataPacket;

/**
 * @author hhm @ SGameCore Project
 **/
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static ChannelGroup getChannels() {
        return channels;
    }

    @Override
    public boolean isSharable() {
        return true;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        // Once session is secured, send a greeting and register the channel to the global channel
        // list so the channel received the messages from others.
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                (GenericFutureListener<Future<Channel>>) future -> channels.add(ctx.channel()));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //交给解析器
        DataPacket pk = PacketAnalysiser.analysis(msg);
        if (pk != null) DataPacketHandle.handleDataPacket(ctx.channel(), pk);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        for (Server server : APIFunction.getInstance().getServers()) {
            if (server.getChannel() == ctx.channel()) {
                APIFunction.getInstance().getLogger().critical("与服务器:" + server.getName() + "的链接发生错误", cause);
                //server.shutdown(true);
                return;
            }
        }
    }
}

