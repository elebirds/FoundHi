package net.mcpes.foundhi.core.network.protocol.guild

import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.utils.getGuildPermission
import net.sf.json.JSONObject

class UpdateGuildInformationPacket : DataPacket() {
    var id: Int = 0
    lateinit var parameter_name: String//参数名称
    lateinit var parameter_value: Any//参数值

    override fun pid(): Int {
        return ProtocolInfo.GUILD_UPDATE_INFORMATION_PACKET
    }

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["id"] = id
        json["pv"] = parameter_value
        json["pn"] = parameter_name
        return json.toString()
    }

    override fun decode(json: JSONObject): DataPacket {
        this.id = json.getInt("id")
        this.parameter_name = json.getString("pn")
        this.parameter_value = json.getString("pv")
        return this
    }

    override fun handle() {
        if (ServerType.getType() == ServerType.API) {
            return
        }
        if (FoundHiPlugin.instance.hasGuild(id)) {
            val guild = FoundHiPlugin.instance.getGuild(id)!!
            when (parameter_name) {
                "n" -> guild.name = parameter_value.toString()
                "co" -> guild.color = parameter_value.toString()
                "le" -> guild.leader = parameter_value.toString()
                "max" -> guild.maximum = Integer.parseInt(parameter_value.toString())
                "no" -> guild.notice = parameter_value.toString()
                "lv" -> guild.level = Integer.parseInt(parameter_value.toString())
                "ex" -> guild.exp = Integer.parseInt(parameter_value.toString())
                "ca" -> guild.capital = java.lang.Double.parseDouble(parameter_value.toString())
                "bp" -> guild.basePos = parameter_value.toString()
                "ap_a" -> guild.apply.add(parameter_value.toString())
                "ap_b" -> guild.apply.remove(parameter_value.toString())
                "me_a" -> guild.members.add(parameter_value.toString())
                "me_b" -> guild.members.remove(parameter_value.toString())
                "ad_a" -> {
                    val json = JSONObject.fromObject(parameter_value.toString())
                    guild.admins[json.getString("key")] = json.getInt("p").getGuildPermission()
                }
                "ad_b" -> guild.admins.remove(parameter_value.toString())
                "ba_a" -> guild.banned.add(parameter_value.toString())
                "ba_b" -> guild.banned.remove(parameter_value.toString())
            }//todo 地皮服务器分离
            FoundHiPlugin.instance.logger.notice("§b更新公会缓存(guildID:$id,paramName:$parameter_name,value$parameter_value)")
        }
    }
}
