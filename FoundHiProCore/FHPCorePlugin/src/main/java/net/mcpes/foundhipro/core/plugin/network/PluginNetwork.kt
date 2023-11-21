package net.mcpes.foundhipro.core.plugin.network

import io.netty.channel.Channel
import net.mcpes.foundhipro.core.db.io.FHPMySQLIO
import net.mcpes.foundhipro.core.logger.Logger
import net.mcpes.foundhipro.core.network.Network
import net.mcpes.foundhipro.core.network.handle.AbstractDataPacketListener
import net.mcpes.foundhipro.core.network.protocol.packet.AutoCode
import net.mcpes.foundhipro.core.network.protocol.packet.DataPacket
import net.mcpes.foundhipro.core.network.protocol.packet.ManuallyCode
import net.mcpes.foundhipro.core.network.protocol.packet.server.ServerConnectPacket
import net.mcpes.foundhipro.core.network.protocol.packet.server.SetMaxRequestCountPacket
import net.mcpes.foundhipro.core.plugin.FHPCorePlugin
import net.mcpes.foundhipro.core.plugin.config.FHPCorePluginConfiguration
import net.mcpes.foundhipro.core.plugin.manager.OverTimeManager
import net.mcpes.foundhipro.core.plugin.network.handle.PingPongHandle
import net.mcpes.foundhipro.core.plugin.network.listener.PluginListener
import net.mcpes.foundhipro.utils.encryption.RSA
import java.net.InetSocketAddress
import java.util.function.BiConsumer
/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/5 下午9:36
 * version 1.0
 */
class PluginNetwork : Network() {
    //private val lambdaHandles: HashMap<Class<out DataPacket>, Consumer<DataPacket>> = hashMapOf()
    //private val classHandles: HashMap<Class<out DataPacket>, out DataPacketHandle> = hashMapOf()
    lateinit var pluginListener: PluginListener
    var apiServerChannel: Channel?
        get() = pluginListener.channel
        private set(value) {}

    override fun init() {
        instance = this
        super.init()
        this.pluginListener = PluginListener()
        this.initHandles()
        this.pluginListener.init().connect().listen()
    }

    fun sendDataPacket(pk: DataPacket): Boolean {
        return when (pk) {
            is AutoCode -> {
                PluginListener.willSendPackets.put(pk.encode())
                FHPCorePlugin.instance.network.sendListener(pk,apiServerChannel!!)
                true
            }
            is ManuallyCode -> {
                PluginListener.willSendPackets.put(pk.encode())
                FHPCorePlugin.instance.network.sendListener(pk,apiServerChannel!!)
                true
            }
            else -> {
                Logger.instance.warn("将要发送的数据包没有实现AutoCode/ManuallyCode接口,数据包名:${pk.javaClass.canonicalName}")
                false
            }
        }
    }

    private fun initHandles() {
        /*this.lambdaHandles[ServerConnectPacket::class.java] = Consumer {
            if (it !is ServerConnectPacket) return@Consumer
            if (!it.rsaGet) {
                FHPCorePlugin.instance.proved = true
                //PlayerBannedData.loadAll()
                OverTimeManager.updateOverTime()
            } else {
                it.rsaGet = false
                it.password = RSA.publicEncrypt(FHPCorePluginConfiguration.instance.connectSecretKey, RSA.getPublicKey(it.publicKey))
                this.sendDataPacket(it)
            }
        }*/
        this.addListener(AbstractDataPacketListener<DataPacket>(ServerConnectPacket::class.java)
                .onReceive(BiConsumer { _, dataPacket ->
                    if (dataPacket is ServerConnectPacket) {
                        if (dataPacket.rsaGet) {
                            val pk = dataPacket.clone() as ServerConnectPacket
                            pk.rsaGet = false
                            pk.password = RSA.publicEncrypt(FHPCorePluginConfiguration.instance.connectSecretKey, RSA.getPublicKey(pk.publicKey))
                            this.sendDataPacket(pk)
                        } else {
                            FHPCorePlugin.instance.proved = true
                            OverTimeManager.updateOverTime()
                        }
                    }
                })
        )
        this.addListener(AbstractDataPacketListener<DataPacket>(SetMaxRequestCountPacket::class.java)
                .onReceive(BiConsumer { _, dataPacket ->
                    if (dataPacket is SetMaxRequestCountPacket) {
                        val dao = FHPCorePlugin.instance.dao
                        if (dao is FHPMySQLIO) {
                            dao.maxRequest = dataPacket.maxRequestCount
                        }
                    }
                })
        )
        this.addListener(PingPongHandle())
    }

    override fun handleDataPacket(channel: Channel, pk: DataPacket) {
        if (channel != channel && pk !is ServerConnectPacket) {
            Logger.instance.warn("§b网络系统警告:§c未连接的API服务器(channelID:${channel.id().asLongText()},ip:${(channel.localAddress() as InetSocketAddress).address},连接端口${(channel.localAddress() as InetSocketAddress).port})向本服务器发送了数据包!\n内容:${pk.toJson()}")
            return
        }

        /*if (!lambdaHandles.containsKey(pk.javaClass)) {
            /*if (!classHandles.containsKey(pk.javaClass)) {
                Logger.instance.warn("§b网络系统警告:§c数据包:${pk.javaClass.simpleName}(PID:${pk.pid()})没有注册Handle!")
            } else {
                //classHandles[pk.javaClass]?.handle(pk)
            }*/
            this.receiveListener(pk,channel)
            return
        }
        lambdaHandles[pk.javaClass]?.accept(pk)*/
        this.receiveListener(pk,channel)
    }

    companion object {
        lateinit var instance: PluginNetwork
    }
}