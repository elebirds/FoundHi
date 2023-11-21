package net.mcpes.foundhipro.core.app.manager

import io.netty.channel.Channel
import net.mcpes.foundhipro.core.app.data.server.NoProveServer
import net.mcpes.foundhipro.core.app.data.server.Server
import net.mcpes.foundhipro.core.network.protocol.packet.DataPacket
import java.util.concurrent.ConcurrentHashMap

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/9
 * version 1.0
 */
object ServerManager {
    /**
     * 服务器列表
     */
    var servers = ConcurrentHashMap<String, Server>()
    /**
     * 未验证服务器列表
     */
    var proveServers = ConcurrentHashMap<String, NoProveServer>()

    /**
     *  Channel Map
     *  |-channel->serverId
     */
    var channels = ConcurrentHashMap<Channel,String>()

    /**
     * 最大数据库连接 <-> 将会加权平均分给各个服务器
     */
    const val MAX_SQL_REQUEST = 2000

    fun getServer(id: String) = this.servers[id]

    fun getServers() = this.servers.values

    fun getServerCount() = this.servers.size

    fun addProveServer(server: NoProveServer) {
        this.proveServers[server.id] = server
        this.channels[server.channel] = server.id
    }

    fun isProveServer(id: String) = this.proveServers.containsKey(id)

    fun getProveServer(id: String) = this.proveServers[id]

    fun removeServer(server: Server) {
        this.servers.remove(server.id)
        this.proveServers.remove(server.id)
        this.channels.remove(server.channel)
    }

    fun sendBannedData(server: Server) {/*

        if (!Database.isEnableRedis()) {
            val sban = AddPlayerBannedPacket()
            sban.data = hashMapOf()
            bannedPlayerInfos.forEach {
                sban.data[it.key.toString()] = it.value.toString()
            }
            server.dataPacket(sban)
        }*/
    }

    fun isServerOnline(id: String) = this.servers.containsKey(id)

    fun broadcastPacket(dataPacket: DataPacket) {
        for (server: Server in servers.values) {
            server.dataPacket(dataPacket)
        }
    }
}