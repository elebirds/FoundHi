package net.mcpes.foundhipro.core.plugin.network.listener

import cn.nukkit.Server
import cn.nukkit.scheduler.AsyncTask
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import net.mcpes.foundhipro.core.logger.Logger
import net.mcpes.foundhipro.core.plugin.FHPCorePlugin
import net.mcpes.foundhipro.core.plugin.config.FHPCorePluginConfiguration
import java.nio.charset.Charset
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

/**
 * @author hhm @ SGameCore Project
 */
class PluginListener {
    var lastWriteFuture: ChannelFuture? = null
    var channel: Channel? = null
        private set
    private val bootstrap = Bootstrap()
    private val group = NioEventLoopGroup()

    fun init(): PluginListener {
        val sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
        bootstrap.group(group)
                .channel(NioSocketChannel::class.java)
                .handler(PluginInitializer(sslCtx))
                .option(ChannelOption.SO_KEEPALIVE, true)
        return this
    }

    fun connect(): PluginListener {
        if (channel != null && channel!!.isActive) {
            return this
        }
        val cf = bootstrap
                .connect(FHPCorePluginConfiguration.instance.apiServerIP, FHPCorePluginConfiguration.instance.apiServerPort)
        val listener = ChannelFutureListener {
            if (it.isSuccess) {
                channel = it.sync().channel()
                channel!!.pipeline().addLast(StringEncoder(Charset.forName("UTF-8")))
                Logger.instance.info("成功连接到API服务器")
            } else {
                Logger.instance.error("连接API服务器失败,十秒后重试")
                it.channel().eventLoop().schedule({ this.connect() }, 10, TimeUnit.SECONDS)
            }
        }
        cf.addListener(listener)
        return this
    }

    fun listen() {
        Server.getInstance().scheduler.scheduleAsyncTask(FHPCorePlugin.instance, object : AsyncTask() {
            override fun onRun() {
                try {
                    while (FHPCorePlugin.isRunning) {
                        Thread.sleep(1000)
                        if (channel != null) {
                            val flag = willSendPackets.isNotEmpty()
                            while (willSendPackets.isNotEmpty()) {
                                val s = willSendPackets.take() ?: continue
                                if (s.contains("[APǚξλ]")) continue
                                lastWriteFuture = channel?.write("$s[APǚξλ]\n")
                            }
                            if (flag) channel?.flush()
                        }
                    }
                } finally {
                    if (channel != null) {
                        channel!!.closeFuture().sync()
                        channel!!.close()
                    }
                    group.shutdownGracefully()
                }
            }
        })
    }

    fun shutdown() {
        try {
            channel!!.closeFuture().sync()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        channel!!.close()
        group.shutdownGracefully()
    }

    fun restart() {
        if (channel != null) {
            try {
                channel!!.closeFuture().sync()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            channel!!.close()
        }
        this.connect()
    }

    companion object {
        var willSendPackets = LinkedBlockingQueue<String>()
    }
}
