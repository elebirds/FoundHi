package net.mcpes.foundhi.core.network.plugin.listener;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.mcpes.foundhi.core.ServerType;
import net.mcpes.foundhi.core.network.analysiser.PacketAnalysiser;
import net.mcpes.foundhi.core.network.handle.DataPacketHandle;
import net.mcpes.foundhi.core.network.protocol.DataPacket;
import net.mcpes.foundhi.core.network.protocol.server.ServerConnectPacket;
import net.mcpes.foundhi.core.plugin.FoundHiPlugin;

/**
 * @author hhm @ SGameCore Project
 **/
public class PluginHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
        FoundHiPlugin.getInstance().getLogger().info("发生了一个错误,与API服务器的连接已经断开,正在尝试重新连接");
        FoundHiPlugin.getInstance().getNetwork().getPluginListener().doConnect();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        //交给解析器
        DataPacket pk = PacketAnalysiser.analysis(msg);
        if (pk != null) DataPacketHandle.handleDataPacket(ctx.channel(), pk);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ServerConnectPacket cpk = new ServerConnectPacket();
        cpk.type = ServerType.getType();
        cpk.id = FoundHiPlugin.getInstance().getId();
        cpk.name = FoundHiPlugin.getInstance().getServerName();
        cpk.ip = FoundHiPlugin.getInstance().getServerIP();
        cpk.port = FoundHiPlugin.getInstance().getServerPort();
        cpk.cid = FoundHiPlugin.getInstance().getCBagID();
        cpk.rsaGet = true;
        FoundHiPlugin.getInstance().dataPacket(cpk, true);
    }
}
