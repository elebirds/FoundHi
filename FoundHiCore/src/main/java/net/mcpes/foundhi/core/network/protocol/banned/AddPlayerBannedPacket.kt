package net.mcpes.foundhi.core.network.protocol.banned

import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.plugin.banned.PlayerBannedData
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/26 下午9:13
 * version 1.0
 */
class AddPlayerBannedPacket : DataPacket() {
    lateinit var data: HashMap<String, String>

    override fun pid(): Int = ProtocolInfo.BANNED_PLAYER_ADD_PACKET

    override fun decode(json: JSONObject): DataPacket {
        this.data = HashMap()
        JSONObject.fromObject(json["data"]).forEach { t, u -> data[t.toString()] = u.toString() }
        return this
    }

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["data"] = JSONObject.fromObject(this.data).toString()
        return json.toString()
    }

    override fun handle() {
        if (ServerType.getType() == ServerType.API) return
        this.data.forEach { t, u -> PlayerBannedData.load(t.toInt(), u) }
    }
}