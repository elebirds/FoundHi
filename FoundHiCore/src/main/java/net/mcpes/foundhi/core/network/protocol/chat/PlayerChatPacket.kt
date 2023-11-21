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
class PlayerChatPacket : DataPacket() {
    lateinit var player: String
    lateinit var server: String
    lateinit var message: String

    override fun pid(): Int = ProtocolInfo.PLAYER_CHAT_PACKET

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["p"] = this.player
        json["s"] = this.server
        json["m"] = this.message
        return json.toString()
    }

    override fun decode(json: JSONObject): DataPacket {
        this.player = json["p"] as String
        this.message = json["m"] as String
        this.message = json["s"] as String
        return this
    }

    override fun handle() {
        if (ServerType.getType() == ServerType.API) {
            if (!APIFunction.instance.isServerOnline(this.server)) return
            val server = APIFunction.instance.getServer(this.server)!!
            val uc = server.uniteChatID
            if (uc == null || uc == "") return
            if (!APIFunction.instance.hasUniteChat(uc)) return
            if (!APIFunction.instance.isPlayerOnline(player)) return
            val p = APIFunction.instance.getPlayer(player)!!
            val marry: String = if (p.lover != "") {
                "§a[§c情侣§4❤§f" + p.lover + "§a]"
            } else {
                "§a[§c情侣§4❤§2单身贵族§a]"
            }
            val sName: String = if (p.guild != -1 && APIFunction.instance.hasGuild(p.guild)) {
                val guild = APIFunction.instance.getGuild(p.guild)!!
                guild.color + guild.name
            } else {
                "No~team"
            }
            this.message = if (p.title.size == 0) {
                APIFunction.instance.getServer(this.server)!!.name + " §6>> §l[§f" + sName + "§l]" + marry + "§e<§f" + p.title[p.titling] + "の" + p.nameColor + this.player + "§e>:" + p.chatColor + this.message
            } else {
                APIFunction.instance.getServer(this.server)!!.name + " §6>> §l[§f" + sName + "§l]" + marry + "§e<§f" + p.nameColor + this.player + "§e>:" + p.chatColor + this.message
            }
            APIFunction.instance.getUniteChat(uc)!!.forEach {
                if (APIFunction.instance.isServerOnline(it)) APIFunction.instance.getServer(it)!!.dataPacket(this)
            }
        } else {
            cn.nukkit.Server.getInstance().broadcastMessage(this.message)
        }
    }
}