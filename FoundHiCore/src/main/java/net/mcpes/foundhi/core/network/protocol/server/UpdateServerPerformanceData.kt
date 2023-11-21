package net.mcpes.foundhi.core.network.protocol.server

import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.network.server.listener.ServerListener.overTime
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/24
 * version 1.0
 */
class UpdateServerPerformanceData : DataPacket() {
    var id: String = "" //服务器id
    var count: Int = 0 //服务器人数
    var tps: Float = 0.0f //tps
    var upload: Double = 0.0 //上传占用网速
    var download: Double = 0.0 //下载占用网速
    var load: Float = 0.0f //负载

    override fun pid(): Int = ProtocolInfo.UPDATE_PLAYER_PERFORMANCE_DATA_PACKET

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["id"] = id
        json["c"] = count
        json["tps"] = tps.toDouble()
        json["ul"] = upload
        json["dl"] = download
        json["l"] = load.toDouble()
        return json.toString()
    }

    override fun decode(json: JSONObject): DataPacket {
        this.id = json.getString("id")
        this.count = json.getInt("c")
        this.load = json.getDouble("l").toFloat()
        this.tps = json.getDouble("tps").toFloat()
        this.download = json.getDouble("dl")
        this.upload = json.getDouble("ul")
        return this
    }

    override fun handle() {
        if (ServerType.getType() == ServerType.API) {
            if (!APIFunction.instance.isServerOnline(id)) return
            val server = APIFunction.instance.getServer(id)!!
            overTime[server.channel] = System.currentTimeMillis()
            server.tps = tps
            server.load = load
            server.count = count
            server.upload = upload
            server.download = download
        } else {
            FoundHiPlugin.instance.count = this.count
            FoundHiPlugin.instance.overTime = System.currentTimeMillis()
        }
    }
}