package net.mcpes.foundhipro.core.app.network.listener

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.SelfSignedCertificate
import net.mcpes.foundhipro.core.app.CoreServer

/**
 * @author hhm @ SGameCore Project
 */
class ServerListener @Throws(Exception::class) constructor() {
    private val ssc: SelfSignedCertificate = SelfSignedCertificate()
    private val sslCtx: SslContext = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build()
    private val bossGroup = NioEventLoopGroup(1)
    private val workerGroup = NioEventLoopGroup()
    lateinit var initializer: ServerInitializer
        private set

    val handler: ServerHandler
        get() = initializer.handler

    @Throws(Exception::class)
    fun start() {
        try {
            initializer = ServerInitializer(sslCtx)
            val b = ServerBootstrap()
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel::class.java)
                    .handler(LoggingHandler(LogLevel.INFO))
                    .childHandler(initializer)
                    .option(ChannelOption.SO_KEEPALIVE, true)
            b.bind(CoreServer.instance.config.listenPort)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync()
        } finally {
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }

    fun shutdown() {
        bossGroup.shutdownGracefully()
        workerGroup.shutdownGracefully()
    }
}
