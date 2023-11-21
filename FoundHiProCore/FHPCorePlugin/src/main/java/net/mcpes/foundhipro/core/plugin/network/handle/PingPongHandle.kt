package net.mcpes.foundhipro.core.plugin.network.handle

import io.netty.channel.Channel
import net.mcpes.foundhipro.core.network.handle.AbstractDataPacketListener
import net.mcpes.foundhipro.core.network.protocol.packet.server.PingPongPacket
import net.mcpes.foundhipro.core.plugin.FHPCorePlugin
import net.mcpes.foundhipro.core.plugin.manager.OverTimeManager

/**
 * FoundHiPro
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class PingPongHandle  : AbstractDataPacketListener<PingPongPacket>(PingPongPacket::class.java) {

    override fun receivePacket(channel: Channel, dataPacket: PingPongPacket) {
        super.receivePacket(channel, dataPacket)
        OverTimeManager.updateOverTime()
        FHPCorePlugin.instance.network.sendDataPacket(PingPongPacket())
    }

}