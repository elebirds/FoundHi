package net.mcpes.foundhipro.core.network.handle

import io.netty.channel.Channel
import net.mcpes.foundhipro.core.network.protocol.packet.DataPacket

/**
 * FoundHiPro
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
interface DataPacketListener<in T : DataPacket> {
    fun receivePacket(channel: Channel,dataPacket: T)

    fun sendPacket(channel: Channel,dataPacket: T)
}