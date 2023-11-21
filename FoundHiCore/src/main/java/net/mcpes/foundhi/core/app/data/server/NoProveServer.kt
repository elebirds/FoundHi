package net.mcpes.foundhi.core.app.data.server

import io.netty.channel.Channel
import net.mcpes.foundhi.core.app.APIFunction

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/30
 * version 1.0
 */
class NoProveServer(id: String, name: String, server_ip: String, server_port: Int, channel: Channel, cid: String?, type: Int) : Server(id, name, server_ip, server_port, channel, cid, type) {
    var verification = false
    var privateKey = ""

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

    fun toProvenServer() {
        APIFunction.instance.toProvenServer(this)
    }
}