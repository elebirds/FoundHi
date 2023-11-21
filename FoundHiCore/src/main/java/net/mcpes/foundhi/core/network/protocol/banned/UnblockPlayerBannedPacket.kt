package net.mcpes.foundhi.core.network.protocol.banned

import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/3
 * version 1.0
 */
class UnblockPlayerBannedPacket : DataPacket() {
    var id: Int = 0
    lateinit var reviser: String

    override fun pid(): Int = ProtocolInfo.BANNED_PLAYER_UNBLOCK_PACKET

    override fun decode(json: JSONObject): DataPacket {
        this.id = json["id"] as Int
        this.reviser = json["r"].toString()
        return this
    }

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["id"] = this.id
        json["r"] = this.reviser
        return json.toString()
    }

    override fun handle() {
        if (ServerType.getType() == ServerType.API) return
        if (!FoundHiPlugin.instance.bannedPlayerInfos.containsKey(id)) return
        val data = FoundHiPlugin.instance.bannedPlayerInfos[id]!!
        data.reviser = reviser
        data.beLifted = true
        FoundHiPlugin.instance.playerEnabledBannedInfos[data.banned]?.remove(data)
    }
}