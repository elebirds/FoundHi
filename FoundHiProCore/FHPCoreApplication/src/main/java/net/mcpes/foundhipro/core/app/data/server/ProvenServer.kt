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
abstract class ProvenServer(id: String, name: String, serverIP: String, serverPort: Int, channel: Channel, type: ServerType.ServerType) : Server(id, name, serverIP, serverPort, channel, type) {
    init {
        this.count = 0
        this.players.clear()
        Logger.instance.info("未经验证的服务器:$name(id:$id)已经通过验证!")
    }

    override fun shutdown(stopServer: Boolean) {
        if (stopServer) {
            /*
            *
            for (player in players.values) {
                if (player.inServer == this.id) {
                    exitedPlayers[player.name] = players[player.name]!!
                    saveTask[player.name] = this.scheduler.scheduleDelayedTask({
                        player.save()
                        this.alreadySavePlayerData(player.name)
                    }, 600).taskId
                    players.remove(player.name)
                }
            }
            for (al in this.uniteChats.values) {
                al.remove(this.id)
            }
            val pk = ServerDisconnectPacket()
            pk.id = this.id
            this.dataPacket(pk)*/
            this.channel.close()
            ServerManager.removeServer(this)
            OverTimeManager.removeServer(this.channel)
            Logger.instance.info("与服务器:${this.name}(id:${this.id})的连接已经关闭!")
            return
        }
        Logger.instance.info("正在与服务器:${this.name}(id:${this.id})重新建立连接!")
    }
}