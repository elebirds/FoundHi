package net.mcpes.foundhipro.core.plugin.network.listener

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import net.mcpes.foundhipro.core.logger.Logger
import net.mcpes.foundhipro.core.network.analysiser.PacketAnalysiser
import net.mcpes.foundhipro.core.network.protocol.packet.server.ServerConnectPacket
import net.mcpes.foundhipro.core.plugin.config.FHPCorePluginConfiguration
import net.mcpes.foundhipro.core.plugin.network.PluginNetwork

/**
 * @author hhm @ SGameCore Project
 */
class PluginHandler : SimpleChannelInboundHandler<String>() {
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()

        Logger.instance.error("发生了一个错误,与API服务器的连接已经断开,正在尝试重新连接")
        PluginNetwork.instance.pluginListener.connect()
    }

    public override fun channelRead0(ctx: ChannelHandlerContext, msg: String) {
        //交给解析器
        val pk = PacketAnalysiser.analysis(msg)
        if (pk != null) PluginNetwork.instance.handleDataPacket(ctx.channel()!!, pk)
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        val cpk = ServerConnectPacket()
        cpk.id = FHPCorePluginConfiguration.instance.serverID
        cpk.name = FHPCorePluginConfiguration.instance.serverID
        cpk.ip = FHPCorePluginConfiguration.instance.serverID
        cpk.port = FHPCorePluginConfiguration.instance.nukkitServerPort
        cpk.rsaGet = true
        PluginNetwork.instance.sendDataPacket(cpk)
    }
}
