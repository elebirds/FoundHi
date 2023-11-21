package net.mcpes.foundhi.core.network.analysiser

import cn.nukkit.Server
import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.network.Network
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.sf.json.JSONException
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/22
 * version 1.0
 */

object PacketAnalysiser {
    @JvmStatic
    private var OverallSituationSTR = ""

    @JvmStatic
    fun analysis(msg: String): DataPacket? {
        if (ServerType.getType() == ServerType.API) {
            if (APIFunction.instance.isDebug()) APIFunction.instance.logger.debug(msg)
        } else {
            if (FoundHiPlugin.instance.isDebug) Server.getInstance().logger.debug(msg)
        }
        var json = JSONObject()
        try {
            json = JSONObject.fromObject(msg.replace("[APǚξλ]", ""))
        } catch (ignored: JSONException) {
        }
        if (json.size == 0) {
            APIFunction.instance.logger.debug("OverallSituation TCP流字符串 ADD >> $msg")
            synchronized(PacketAnalysiser::class.java) {
                OverallSituationSTR += msg
            }
            synchronized(PacketAnalysiser::class.java) {
                for (sp in OverallSituationSTR.split("[APǚξλ]")) {
                    var j2 = JSONObject()
                    try {
                        j2 = JSONObject.fromObject(sp)
                    } catch (ignored: JSONException) {
                    }
                    if (j2.size != 0) {
                        OverallSituationSTR = OverallSituationSTR.replace("$sp[APǚξλ]", "")
                        val pk = Network.instance.getPacket(json.getInt("pid"))!!
                        return pk.decode(json)
                    }
                }
            }
        } else {
            val pk = Network.instance.getPacket(json.getInt("pid"))!!
            return pk.decode(json)
        }
        return null
    }
}
