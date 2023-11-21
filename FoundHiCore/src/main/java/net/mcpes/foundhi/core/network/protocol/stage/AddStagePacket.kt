package net.mcpes.foundhi.core.network.protocol.stage

import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author WetABQ Copyright (c) 2018.07
 * @version 1.0
 */
class AddStagePacket : DataPacket() {

    var stageId = -1
    lateinit var game: String
    var maxPlayer = -1
    var fastPlayer = -1


    override fun pid(): Int {
        return ProtocolInfo.STAGE_SERVER_ADD_STAGE_PACKET
    }

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["sid"] = this.stageId
        json["g"] = this.game
        json["mp"] = this.maxPlayer
        json["fp"] = this.fastPlayer
        return json.toString()
    }

    override fun decode(json: JSONObject): DataPacket {
        this.stageId = json["sid"] as Int
        this.game = json["g"] as String
        this.maxPlayer = json["mp"] as Int
        this.fastPlayer = json["fp"] as Int
        return this
    }

    override fun handle() {

    }


}