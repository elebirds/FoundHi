package net.mcpes.foundhipro.core.app.data.server

import io.netty.channel.Channel
import net.mcpes.foundhipro.core.ServerType
import net.mcpes.foundhipro.core.app.manager.OverTimeManager
import net.mcpes.foundhipro.core.app.manager.ServerManager
import net.mcpes.foundhipro.core.logger.Logger

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/30
 * version 1.0
 */
class NoProveServer(id: String, name: String, serverIP: String, serverPort: Int, channel: Channel) : Server(id, name, serverIP, serverPort, channel, ServerType.ServerType.DETECT) {
    var verification = false
    var privateKey = ""

    init {
        this.count = 0
        Logger.instance.info("未经验证的服务器:$name(id:$id)已经连接到API服务器!")
    }

    fun setAsProvenDetectServer() {
        if (!verification) return
        val server = DetectServer(id, name, serverIP, serverPort, channel)
        ServerManager.proveServers.remove(id)
        ServerManager.servers[id] = server
    }

    override fun shutdown(stopServer: Boolean) {
        if (stopServer) {
            OverTimeManager.removeServer(this.channel)
            ServerManager.removeServer(this)
            this.channel.close()
            Logger.instance.info("与未经验证的服务器:${this.name}(id:${this.id})的连接已经关闭!")
            return
        }
        Logger.instance.info("正在与未经验证的服务器:${this.name}(id:${this.id})重新建立连接!")
    }
}