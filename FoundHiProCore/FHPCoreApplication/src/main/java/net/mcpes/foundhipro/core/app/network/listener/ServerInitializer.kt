package net.mcpes.foundhipro.core.app.network.listener

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.ssl.SslContext
import java.nio.charset.Charset

/**
 * @author hhm @ SGameCore Project
 */
class ServerInitializer internal constructor(private val sslCtx: SslContext) : ChannelInitializer<SocketChannel>() {
    lateinit var handler: ServerHandler
        private set

    public override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()
        handler = ServerHandler()
        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the listener side.
        // You will need something more complicated to identify both
        // and server in the real world.
        pipeline.addLast(sslCtx.newHandler(ch.alloc()))
        // On top of the SSL handler, add the text line codec.
        pipeline.addLast(DelimiterBasedFrameDecoder(8192, *Delimiters.lineDelimiter()))
        pipeline.addLast(StringDecoder(Charset.forName("UTF-8")))
        pipeline.addLast(StringEncoder(Charset.forName("UTF-8")))
        // and then business logic.
        pipeline.addLast(handler)
    }
}
