package net.mcpes.foundhipro.core.app.network

import io.netty.channel.Channel
import net.mcpes.foundhipro.core.app.CoreServer
import net.mcpes.foundhipro.core.app.network.handle.server.PingPongHandle
import net.mcpes.foundhipro.core.app.network.handle.server.ServerConnectHandle
import net.mcpes.foundhipro.core.app.network.listener.ServerListener
import net.mcpes.foundhipro.core.app.scheduler.AsyncTask
import net.mcpes.foundhipro.core.logger.Logger
import net.mcpes.foundhipro.core.network.Network
import net.mcpes.foundhipro.core.network.protocol.packet.DataPacket
import java.net.InetSocketAddress

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/6 下午12:41
 * version 1.0
 */
class ServerNetwork : Network() {
    //private val lambdaHandles: HashMap<Class<out DataPacket>, BiConsumer<String, DataPacket>> = hashMapOf()
    //private val classHandles: HashMap<Class<out DataPacket>, out DataPacketHandle> = hashMapOf()
    private val connectedServer: HashMap<Channel, String> = hashMapOf()
    lateinit var serverListener: ServerListener

    override fun init() {
        instance = this
        super.init()
        this.serverListener = ServerListener()
        this.initHandle()
        CoreServer.instance.scheduler.scheduleAsyncTask(object : AsyncTask() {
            override fun onRun() {
                ServerNetwork.instance.serverListener.start()
            }
        })
    }

    private fun initHandle() {
        this.addListener(ServerConnectHandle())
        this.addListener(PingPongHandle())
    }

    override fun handleDataPacket(channel: Channel, pk: DataPacket) {
        val server = connectedServer[channel]
        if (server == null) {
            Logger.instance.warn("§b网络系统警告:§c未连接的服务器(channelID:${channel.id().asLongText()},ip:${(channel.localAddress() as InetSocketAddress).address},连接端口${(channel.localAddress() as InetSocketAddress).port})向API服务器发送了数据包!")
            return
        }
        /*if (!lambdaHandles.containsKey(pk.javaClass)) {
            /*if (!classHandles.containsKey(pk.javaClass)) {
                Logger.instance.warn("§b网络系统警告:§c数据包:${pk.javaClass.simpleName}(PID:${pk.pid()})没有注册Handle!")
            } else {
                //classHandles[pk.javaClass]?.handle(pk)
            }*/
            return
        }
        lambdaHandles[pk.javaClass]?.accept(server, pk)*/
        this.receiveListener(pk,channel)
    }

    companion object {
        lateinit var instance: ServerNetwork
    }
}