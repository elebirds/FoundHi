package net.mcpes.foundhi.core.network.protocol.player

import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.sf.json.JSONObject

class PlayerJoinPacket : DataPacket() {
    lateinit var id: String

    override fun pid(): Int {
        return ProtocolInfo.PLAYER_JOIN_PACKET
    }

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["id"] = id
        return json.toString()
    }

    override fun decode(json: JSONObject): DataPacket {
        this.id = json.getString("id")
        return this
    }

    override fun handle() {
        if (ServerType.getType() != ServerType.API) {
            return
        }
        APIFunction.instance.addPlayer(id)
    }
}
