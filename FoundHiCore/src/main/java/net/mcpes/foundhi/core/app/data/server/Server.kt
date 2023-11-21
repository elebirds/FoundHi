package net.mcpes.foundhi.core.app.data.server

import io.netty.channel.Channel
import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.network.protocol.DataPacket

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/13
 * version 1.0
 */
abstract class Server(val id: String, val name: String, val server_ip: String, val server_port: Int, val channel: Channel, cid: String?, val type: Int) {
    var count: Int = 0
    var isUseCBag: Boolean = false
        protected set
    var cBagID: String? = null
        protected set
    var uniteChatID: String? = null
        set(value) {
            field = value
            if (uniteChatID != null && uniteChatID != "") APIFunction.instance.addUniteChat(this.id, value!!)
        }
    val alreadySentGuilds = arrayListOf<Int>()
    var tps: Float = 0.0f //tps
    var upload: Double = 0.0 //上传占用网速
    var download: Double = 0.0 //下载占用网速
    var load: Float = 0.0f //负载

    init {
        this.count = 0
        if (cid != null) {
            this.isUseCBag = true
            this.cBagID = cid
        } else {
            this.isUseCBag = false
        }
        APIFunction.instance.logger.notice("未经验证的服务器:$name(id:$id)已经连接到API服务器!")
    }

    fun dataPacket(dataPacket: DataPacket) {
        if (channel.isOpen) {
            val pk = dataPacket.toString()
            if (pk.contains("[APǚξλ]")) return
            APIFunction.instance.logger.debug("Send Server:$id Data:$pk")
            channel.writeAndFlush("$pk[APǚξλ]\n")
        }
    }

    open fun shutdown(stopServer: Boolean) {
        APIFunction.instance.serverShutdown(this, stopServer)
    }
}
