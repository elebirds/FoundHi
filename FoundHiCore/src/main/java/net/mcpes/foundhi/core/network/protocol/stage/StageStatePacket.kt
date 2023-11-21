package net.mcpes.foundhi.core.network.protocol.stage

import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.stage.StageState
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author WetABQ Copyright (c) 2018.07
 * @version 1.0
 */
class StageStatePacket : DataPacket() {

    var stageId = -1
    lateinit var stageState: StageState

    override fun pid(): Int {
        return ProtocolInfo.STAGE_CLIENT_STATE_PACKET
    }

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["sid"] = stageId
        json["ss"] = stageState.toInt()
        return json.toString()
    }

    override fun decode(json: JSONObject): DataPacket {
        this.stageId = json["sid"] as Int
        this.stageState = StageState.toState(json["ss"] as Int)!!
        return this
    }

    override fun handle() {

    }

}