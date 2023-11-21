package net.mcpes.foundhipro.core.app.network.handle.server

import io.netty.channel.Channel
import net.mcpes.foundhipro.core.app.manager.OverTimeManager
import net.mcpes.foundhipro.core.app.manager.ServerManager
import net.mcpes.foundhipro.core.logger.Logger
import net.mcpes.foundhipro.core.network.handle.AbstractDataPacketListener
import net.mcpes.foundhipro.core.network.protocol.packet.server.PingPongPacket

/**
 * FoundHiPro
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class PingPongHandle : AbstractDataPacketListener<PingPongPacket>(PingPongPacket::class.java) {
    override fun receivePacket(channel: Channel, dataPacket: PingPongPacket) {
        super.receivePacket(channel, dataPacket)
        OverTimeManager.update(channel)
        if (ServerManager.channels[channel] != null) {
            ServerManager.getServer(ServerManager.channels[channel]!!)!!.dataPacket(PingPongPacket())
        } else {
            Logger.instance.error("处理PingPong时出现了 Channel不存在的问题")
        }
    }
}