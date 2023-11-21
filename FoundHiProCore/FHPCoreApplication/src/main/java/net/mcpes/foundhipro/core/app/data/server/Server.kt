package net.mcpes.foundhipro.core.app.data.server

import io.netty.channel.Channel
import net.mcpes.foundhipro.core.ServerType
import net.mcpes.foundhipro.core.app.CoreServer
import net.mcpes.foundhipro.core.logger.Logger
import net.mcpes.foundhipro.core.network.protocol.packet.DataPacket

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/13
 * version 1.0
 */
abstract class Server(val id: String, val name: String, val serverIP: String, val serverPort: Int, val channel: Channel, val type: ServerType.ServerType) {
    val players: HashMap<String, String> = hashMapOf()
    var count: Int = 0
    var tps: Float = 0.0f //tps
    var upload: Double = 0.0 //上传占用网速
    var download: Double = 0.0 //下载占用网速
    var load: Float = 0.0f //负载

    init {
        this.count = 0
        Logger.instance.info("未经验证的服务器:$name(id:$id)已经连接到API服务器!")
    }

    fun dataPacket(dataPacket: DataPacket) {
        if (channel.isOpen) {
            CoreServer.instance.network.sendListener(dataPacket,channel)
            val pk = dataPacket.toString()
            if (pk.contains("[APǚξλ]")) return
            Logger.instance.debug("Send Server:$id Data:$pk")
            channel.writeAndFlush("$pk[APǚξλ]\n")
        }
    }

    abstract fun shutdown(stopServer: Boolean)
}
