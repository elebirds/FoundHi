package net.mcpes.foundhi.core.network.protocol.player

import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.app.data.player.PlayerPreferenceSettings
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.data.MailCache
import net.mcpes.foundhi.core.utils.getPlayerPermission
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import java.util.*

class UpdatePlayerInformationPacket : DataPacket() {
    lateinit var id: String
    lateinit var parameter_name: String//参数名称
    lateinit var parameter_value: Any//参数值

    override fun pid(): Int {
        return ProtocolInfo.PLAYER_UPDATE_INFORMATION_PACKET
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
        this.id = json.getString("id")
        this.parameter_name = json.getString("pn")
        this.parameter_value = json.getString("pv")
        return this
    }

    override fun handle() {
        if (ServerType.getType() == ServerType.API) {
            return
        }
        if (FoundHiPlugin.instance.isPlayerOnline(id)) {
            val player = FoundHiPlugin.instance.getPlayer(id)!!
            when (parameter_name) {
                "p" -> player.permission = parameter_value.toString().toInt().getPlayerPermission()
                "m1" -> player.money1 = parameter_value.toString().toDouble()
                "m2" -> player.money2 = parameter_value.toString().toDouble()
                "m3" -> player.money3 = parameter_value.toString().toDouble()
                "guild" -> player.guild = parameter_value.toString().toInt()
                "nc" -> player.nameColor = parameter_value.toString()
                "cc" -> player.chatColor = parameter_value.toString()
                "titling" -> player.titling = parameter_value.toString().toInt()
                "lover" -> player.lover = parameter_value.toString()
                "contribution" -> player.contribution = parameter_value.toString().toInt()
                "title" -> {
                    val real = arrayListOf<String>()
                    JSONArray.fromObject(parameter_value).forEach { real.add(it.toString()) }
                    player.title = real
                }
                "title_a" -> {
                    player.title.add(parameter_value.toString())
                }
                "title_b" -> {
                    player.title.remove(parameter_value.toString())
                }
                "mi" -> {
                    val json = JSONObject.fromObject(this.parameter_value)
                    json.forEach { t, u ->
                        val data = JSONObject.fromObject(t)
                        val map = HashMap<String, Any>()
                        JSONObject.fromObject(data.getString("a")).forEach { a, b -> map[a.toString()] = b }
                        player.inbox[MailCache(data.getString("i"),
                                data.getInt("type"),
                                data.getString("db"),
                                data.getString("d"),
                                data.getString("s"),
                                data.getString("c"),
                                map,
                                data.getBoolean("r"))] = u as Int
                    }
                }
                "mo" -> {
                    val json = JSONObject.fromObject(this.parameter_value)
                    json.forEach { t, u ->
                        val data = JSONObject.fromObject(t)
                        val map = HashMap<String, Any>()
                        JSONObject.fromObject(data.getString("a")).forEach { a, b -> map[a.toString()] = b }
                        player.outbox[MailCache(data.getString("i"),
                                data.getInt("type"),
                                data.getString("db"),
                                data.getString("d"),
                                data.getString("s"),
                                data.getString("c"),
                                map,
                                data.getBoolean("r"))] = u as Int
                    }
                }
                "fr" -> {
                    val json = JSONObject.fromObject(this.parameter_value)
                    json.forEach { t, u ->
                        player.friends[t.toString()] = u.toString()
                    }
                }
                "fr_a" -> {
                    val json = JSONObject.fromObject(this.parameter_value)
                    player.friends[json.getString("id")] = json.getString("date")
                }
                "fr_b" -> {
                    player.friends.remove(this.parameter_value.toString())
                }
                "he" -> player.head = this.parameter_value as Int
                "ag" -> player.autograph = this.parameter_value.toString()
                "ap" -> {
                    val json = JSONObject.fromObject(this.parameter_value)
                    json.forEach { t, u ->
                        player.apply[t.toString()] = u.toString().toInt()
                    }
                }
                "ap_a" -> {
                    val json = JSONObject.fromObject(this.parameter_value)
                    player.apply[json.getString("id")] = json.getInt("type")
                }
                "ap_b" -> {
                    player.apply.remove(this.parameter_value.toString())
                }
                "it" -> player.inTeam = this.parameter_value.toString().toInt()
                "ma" -> {
                    val json = JSONArray.fromObject(this.parameter_value)
                    json.forEach {
                        player.marry.add(it.toString())
                    }
                }
                "ma_a" -> {
                    player.marry.add(this.parameter_value.toString())
                }
                "ma_b" -> {
                    player.marry.remove(this.parameter_value.toString())
                }
                "inv" -> {
                    val json = JSONObject.fromObject(this.parameter_value)
                    json.forEach {
                        player.invitation[it.key.toString()] = it.value.toString().toInt()
                    }
                }
                "inv_a" -> {
                    val json = JSONObject.fromObject(this.parameter_value)
                    player.invitation[json.getString("id")] = json.getInt("guild")
                }
                "inv_b" -> {
                    player.invitation.remove(this.parameter_value.toString())
                }
                else -> {
                    val split = this.parameter_name.split(":")
                    if (split.size == 2 && split[0] == "pre") {
                        when (split[1]) {
                            "hide_player" -> player.preferenceSettings.set(PlayerPreferenceSettings.PreferenceType.HIDE_PLAYER, this.parameter_value.toString().toBoolean())
                        }
                    }
                }
            }
            FoundHiPlugin.instance.logger.notice("§b更新玩家缓存(id:$id,paramName:$parameter_name,value$parameter_value)")
        }
    }
}