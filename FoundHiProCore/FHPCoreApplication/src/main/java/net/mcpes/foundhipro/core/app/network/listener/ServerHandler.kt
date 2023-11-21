package net.mcpes.foundhipro.core.app.network.listener

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.handler.ssl.SslHandler
import io.netty.util.concurrent.GlobalEventExecutor
import net.mcpes.foundhipro.core.app.manager.ServerManager
import net.mcpes.foundhipro.core.app.network.ServerNetwork
import net.mcpes.foundhipro.core.logger.Logger
import net.mcpes.foundhipro.core.network.analysiser.PacketAnalysiser

class ServerHandler : SimpleChannelInboundHandler<String>() {
    override fun isSharable(): Boolean {
        return true
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.pipeline().get(SslHandler::class.java).handshakeFuture().addListener {
            channels.add(ctx.channel())
        }
    }

    @Throws(Exception::class)
    public override fun channelRead0(ctx: ChannelHandlerContext, msg: String) {
        //交给解析器
        val pk = PacketAnalysiser.analysis(msg)
        if (pk != null) ServerNetwork.instance.handleDataPacket(ctx.channel(), pk)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        for (server in ServerManager.getServers()) {
            if (server.channel === ctx.channel()) {
                Logger.instance.error("与服务器:" + server.name + "的链接发生错误", cause)
                //server.shutdown(true);
                return
            }
        }
    }

    companion object {
        val channels: ChannelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    }
}

