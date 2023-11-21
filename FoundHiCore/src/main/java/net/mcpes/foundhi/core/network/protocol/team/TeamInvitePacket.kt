package net.mcpes.foundhi.core.network.protocol.team

import cn.nukkit.Server
import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.core.plugin.gui.window.lambda.defaults.LambdaVariableTipGUI
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/8/14
 * version 1.0
 */
class TeamInvitePacket : DataPacket() {
    lateinit var leader: String
    lateinit var invitee: String
    var success: Boolean = false

    override fun pid(): Int = ProtocolInfo.TEAM_INVITE_PACKET

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["l"] = this.leader
        json["i"] = this.invitee
        if (ServerType.getType() != ServerType.API) json["f"] = success
        return json.toString()
    }

    override fun decode(json: JSONObject): DataPacket {
        this.leader = json["l"].toString()
        this.invitee = json["i"].toString()
        if (ServerType.getType() == ServerType.API) this.success = json["f"] as Boolean
        return this
    }

    override fun handle() {
        if (ServerType.getType() == ServerType.API) return
        if (!FoundHiPlugin.instance.isPlayerOnline(invitee)) {
            FoundHiPlugin.instance.dataPacket(this)
            return
        }
        val player = Server.getInstance().getPlayerExact(invitee) as? NukkitPlayer ?: return
        FoundHiPlugin.instance.teamInvitation["${invitee}_$leader"] = 1
        player.showGUI(FoundHiPlugin.instance.getCommonGUI("team_function_invite")!! as LambdaVariableTipGUI, leader)
    }
}