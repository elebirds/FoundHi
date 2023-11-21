package net.mcpes.foundhipro.core.network.protocol.packet.server

import net.mcpes.foundhipro.core.network.protocol.ProtocolInfo
import net.mcpes.foundhipro.core.network.protocol.packet.AutoCode
import net.mcpes.foundhipro.core.network.protocol.packet.DataPacket

/**
 * FoundHiPro
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 *
 *
 * 此包只有 Server->Client
 *
 */
class SetMaxRequestCountPacket : DataPacket(), AutoCode {

    var maxRequestCount = 1

    override fun pid(): ProtocolInfo {
        return ProtocolInfo.SET_MAX_REQUEST_COUNT_PACKET
    }

}