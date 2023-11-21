package net.mcpes.foundhi.core.network.protocol.banned

import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.plugin.banned.PlayerBannedData
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/3
 * version 1.0
 */
class RemovePlayerBannedPacket : DataPacket() {
    var id: Int = 0

    override fun pid(): Int = ProtocolInfo.BANNED_PLAYER_DEL_PACKET

    override fun decode(json: JSONObject): DataPacket {
        this.id = json["id"] as Int
        return this
    }

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["id"] = this.id
        return json.toString()
    }

    override fun handle() {
        if (ServerType.getType() == ServerType.API) return
        PlayerBannedData.remove(this.id)
    }
}