package net.mcpes.foundhipro.core.network.protocol.packet.server

import net.mcpes.foundhipro.core.network.protocol.ProtocolInfo
import net.mcpes.foundhipro.core.network.protocol.packet.AutoCode
import net.mcpes.foundhipro.core.network.protocol.packet.DataPacket

/**
 * FoundHiPro
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class PingPongPacket : DataPacket(),AutoCode {
    override fun pid() = ProtocolInfo.PING_PONG_PACKET
}