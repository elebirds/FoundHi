package net.mcpes.foundhi.core.network.protocol.chat

import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/13
 * version 1.0
 */
class EnableUniteChatPacket : DataPacket() {
    lateinit var server: String
    lateinit var cid: String

    override fun pid(): Int {
        return ProtocolInfo.ENABLE_UNITE_CHAT_PACKET
    }

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["s"] = server
        json["c"] = cid
        return json.toString()
    }

    override fun decode(json: JSONObject): DataPacket {
        this.server = json.getString("s")
        this.cid = json.getString("c")
        return this
    }

    override fun handle() {
        if (ServerType.getType() != ServerType.API) {
            return
        }
        if (!APIFunction.instance.isServerOnline(server)) return
        APIFunction.instance.getServer(server)!!.uniteChatID = cid
    }
}