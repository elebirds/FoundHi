package net.mcpes.foundhipro.core.plugin.network.listener

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.ssl.SslContext
import net.mcpes.foundhipro.core.plugin.config.FHPCorePluginConfiguration
import net.mcpes.foundhipro.utils.Charset

/**
 * @author hhm @ SGameCore Project
 */
class PluginInitializer(private val sslCtx: SslContext) : ChannelInitializer<SocketChannel>() {
    @Throws(Exception::class)
    public override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()
        pipeline.addLast(sslCtx.newHandler(ch.alloc(), FHPCorePluginConfiguration.instance.apiServerIP, FHPCorePluginConfiguration.instance.apiServerPort))
        pipeline.addLast(DelimiterBasedFrameDecoder(8192, *Delimiters.lineDelimiter()))
        pipeline.addLast(StringDecoder(Charset.defaultCharset))
        pipeline.addLast(StringEncoder(Charset.defaultCharset))
        pipeline.addLast(PluginHandler())
    }
}
