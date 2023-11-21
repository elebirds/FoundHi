package net.mcpes.foundhipro.core.app.network.handle.server

import io.netty.channel.Channel
import net.mcpes.foundhipro.core.app.CoreServer
import net.mcpes.foundhipro.core.app.data.server.NoProveServer
import net.mcpes.foundhipro.core.app.manager.OverTimeManager
import net.mcpes.foundhipro.core.app.manager.ServerManager
import net.mcpes.foundhipro.core.network.handle.AbstractDataPacketListener
import net.mcpes.foundhipro.core.network.protocol.packet.server.ServerConnectPacket
import net.mcpes.foundhipro.core.network.protocol.packet.server.SetMaxRequestCountPacket
import net.mcpes.foundhipro.utils.encryption.RSA

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/7
 * version 1.0
 */
class ServerConnectHandle : AbstractDataPacketListener<ServerConnectPacket>(ServerConnectPacket::class.java){
    override fun receivePacket(channel: Channel, dataPacket: ServerConnectPacket) {
        super.receivePacket(channel, dataPacket)
        if (ServerManager.isServerOnline(dataPacket.id)) {
            ServerManager.getServer(dataPacket.id)!!.shutdown(false)
        }
        if (!dataPacket.rsaGet) {
            if (!ServerManager.isProveServer(dataPacket.id)) return
            val ps = ServerManager.getProveServer(dataPacket.id)!!
            val un = RSA.privateDecrypt(dataPacket.password, ps.privateKey)
            if (un == CoreServer.instance.config.connectSecretKey) {
                ps.verification = true
                ps.setAsProvenDetectServer()
                val server = ServerManager.getServer(dataPacket.id)!!
                server.dataPacket(dataPacket)
                // 发送最大SQL 连接上限
                // + 1指加上API服务器均分到的连接数
                val setMaxRequestCountPacket = SetMaxRequestCountPacket()
                setMaxRequestCountPacket.maxRequestCount = (ServerManager.MAX_SQL_REQUEST.toDouble() / (ServerManager.getServerCount()+1).toDouble()).toInt()
                ServerManager.broadcastPacket(setMaxRequestCountPacket)
                ServerManager.sendBannedData(server)
            } else {
                ps.shutdown(true)
                return
            }
        } else {
            val server = NoProveServer(dataPacket.id, dataPacket.name, dataPacket.ip, dataPacket.port, dataPacket.channel!!)
            val rsaKey = RSA.createKeys(2048)
            dataPacket.publicKey = rsaKey[RSA.RSA_PUBLIC_KEY].toString()
            server.privateKey = rsaKey[RSA.RSA_PRIVATE_KEY].toString()
            ServerManager.addProveServer(server)
            OverTimeManager.update(server)
            server.dataPacket(dataPacket)
        }
    }

}