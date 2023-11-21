package net.mcpes.foundhipro.core.network

import com.google.gson.Gson
import com.google.gson.JsonParseException
import io.netty.channel.Channel
import net.mcpes.foundhipro.core.logger.Logger
import net.mcpes.foundhipro.core.network.handle.AbstractDataPacketListener
import net.mcpes.foundhipro.core.network.handle.DataPacketListener
import net.mcpes.foundhipro.core.network.protocol.ProtocolInfo
import net.mcpes.foundhipro.core.network.protocol.packet.AutoCode
import net.mcpes.foundhipro.core.network.protocol.packet.DataPacket
import net.mcpes.foundhipro.core.network.protocol.packet.ManuallyCode
import net.mcpes.foundhipro.core.network.protocol.packet.server.PingPongPacket
import net.mcpes.foundhipro.core.network.protocol.packet.server.ServerConnectPacket
import net.mcpes.foundhipro.core.network.protocol.packet.server.SetMaxRequestCountPacket
import net.mcpes.foundhipro.utils.cast
import java.util.concurrent.ConcurrentHashMap


abstract class Network {
    private var packetPool: Array<Class<out DataPacket>?> = arrayOfNulls(256)
    private var listenerPool = ConcurrentHashMap<Class<out DataPacket>,ArrayList<DataPacketListener<*>>>()

    init {
        try {
            this.init()
        } catch (e: Exception) {
            Logger.instance.error("在进行网络通信初始化操作时遇到错误", e)
        }
    }

    protected open fun init() {
        this.registerPackets()
        instance = this
    }

    private fun registerPackets() {
        this.registerPacket(ProtocolInfo.SERVER_CONNECT_PACKET.pid,ServerConnectPacket::class.java)
        this.registerPacket(ProtocolInfo.PING_PONG_PACKET.pid,PingPongPacket::class.java)
        this.registerPacket(ProtocolInfo.SET_MAX_REQUEST_COUNT_PACKET.pid,SetMaxRequestCountPacket::class.java)
    }

    abstract fun handleDataPacket(channel: Channel, pk: DataPacket)

    private fun registerPacket(id: Int, pc: Class<out DataPacket>) {
        this.packetPool[id] = pc
    }

    fun getPacketForDecode(json: String): DataPacket? {
        try {
            val gson = Gson()
            val data = gson.fromJson(json, MutableMap::class.java)
            if (!data.containsKey("pid") || !data.containsKey("pd")) {
                Logger.instance.warn("接收到无法解析的未知数据包,内容:$json")
                return null
            }
            val packetClass = packetPool[data["pid"].cast()]
            if (packetClass == null) {
                Logger.instance.warn("接收到无法解析的未知数据包,内容:$json")
                return null
            }
            when {
                AutoCode::class.java.isAssignableFrom(packetClass) -> {
                    //实现了自动编/解码接口
                    val packet = Gson().fromJson(data["pd"].toString(), packetClass)
                    if (packet == null) {
                        Logger.instance.warn("接收到无法解析的未知数据包,内容:$json")
                        return null
                    }
                    return packet
                }
                ManuallyCode::class.java.isAssignableFrom(packetClass) -> {
                    //手动编/解码接口
                    val packet = packetClass.getDeclaredConstructor().newInstance()
                    if (packet == null) {
                        Logger.instance.warn("数据包Class无法初始化,数据包名:${packetClass.canonicalName}")
                        return null
                    }
                    return (packet as ManuallyCode).decode(Gson().fromJson(data["pd"].toString(), MutableMap::class.java))
                }
                else -> {
                    Logger.instance.warn("数据包没有实现AutoCode/ManuallyCode接口,数据包名:${packetClass.canonicalName}")
                    return null
                }
            }
        } catch (e: JsonParseException) {
            Logger.instance.warn("接收到无法解析的未知数据包,内容:$json")
        }
        return null
    }

    fun addListener(listener: AbstractDataPacketListener<*>) {
        if (listenerPool.containsKey(listener.packetClass)) listenerPool[listener.packetClass]!!.add(listener)
        else {
            listenerPool[listener.packetClass] = arrayListOf()
            addListener(listener)
        }
    }

    fun receiveListener(dataPacket: DataPacket,channel: Channel) {
        val listeners = listenerPool[dataPacket.javaClass]
        if (listeners != null) {
            for (listener: DataPacketListener<*> in listeners) {
                (listeners as DataPacketListener<DataPacket>).receivePacket(channel,dataPacket)
            }
        } else {
            Logger.instance.warn("§b网络系统警告:§c数据包:${dataPacket.javaClass.simpleName}(PID:${dataPacket.pid()})没有任何绑定的Listener!")
        }
    }

    fun sendListener(dataPacket: DataPacket,channel: Channel) {
        val listeners = listenerPool[dataPacket.javaClass]
        if (listeners != null) {
            for (listener: DataPacketListener<*> in listeners) {
                (listeners as DataPacketListener<DataPacket>).sendPacket(channel,dataPacket)
            }
        } else {
            Logger.instance.warn("§b网络系统警告:§c数据包:${dataPacket.javaClass.simpleName}(PID:${dataPacket.pid()})没有任何绑定的Listener!")
        }
    }

    companion object {
        lateinit var instance: Network
    }
}
