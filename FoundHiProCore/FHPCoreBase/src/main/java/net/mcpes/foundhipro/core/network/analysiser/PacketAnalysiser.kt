package net.mcpes.foundhipro.core.network.analysiser

import net.mcpes.foundhipro.core.logger.Logger
import net.mcpes.foundhipro.core.network.Network
import net.mcpes.foundhipro.core.network.protocol.packet.DataPacket

object PacketAnalysiser {
    @JvmStatic
    private var OverallSituationSTR = ""

    @JvmStatic
    fun analysis(msg: String): DataPacket? {
        Logger.instance.trace("收到数据包:$msg")
        val pk = Network.instance.getPacketForDecode(msg.replace("[APǚξλ]", ""))
        if (pk == null) {
            Logger.instance.trace("OverallSituation TCP流字符串 ADD >> $msg")
            synchronized(PacketAnalysiser::class.java) {
                OverallSituationSTR += msg
            }
            synchronized(PacketAnalysiser::class.java) {
                for (sp in OverallSituationSTR.split("[APǚξλ]")) {
                    val sdp = Network.instance.getPacketForDecode(sp)
                    if (sdp != null) {
                        return sdp
                    }
                }
            }
        } else {
            return pk
        }
        return null
    }
}
