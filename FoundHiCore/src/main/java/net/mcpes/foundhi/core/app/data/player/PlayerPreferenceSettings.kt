package net.mcpes.foundhi.core.app.data.player


import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.network.protocol.player.UpdatePlayerInformationPacket
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/15
 * version 1.0
 */
class PlayerPreferenceSettings(val id: String) {
    val values: HashMap<PreferenceType, Any> = HashMap()

    fun load(s: String) {
        JSONObject.fromObject(s).forEach { k, v ->
            values[this.getPreferenceType(k.toString().toInt())] = v
        }
        values.putIfAbsent(PreferenceType.HIDE_PLAYER, PreferenceType.HIDE_PLAYER.defaultValue)
    }

    fun set(type: PreferenceType, value: Any): Boolean {
        if (type.valueType.name != value.javaClass.name) {
            return false
        }
        values[type] = value
        return true
    }

    fun get(type: PreferenceType): Any {
        return values[type]!!
    }

    override fun toString(): String {
        val jsonObject = JSONObject()
        this.values.forEach { t, u -> jsonObject[t.id.toString()] = u }
        return jsonObject.toString()
    }

    fun getPreferenceType(id: Int): PreferenceType {
        return when (id) {
            0 -> PreferenceType.HIDE_PLAYER
            else -> throw RuntimeException("没有此偏好设置")
        }
    }

    fun update(type: PreferenceType, value: Any) {
        val player = APIFunction.instance.getPlayer(id) ?: return
        val server = APIFunction.instance.getServer(player.inServer) ?: return
        val pk = UpdatePlayerInformationPacket()
        pk.parameter_name = "pre_${type.updateID}"
        pk.parameter_value = value
        pk.id = this.id
        server.dataPacket(pk)
    }

    enum class PreferenceType(val id: Int, val updateID: String, val valueType: Class<*>, val defaultValue: Any) {
        HIDE_PLAYER(0, "hide_player", Boolean::class.java, false);

        override fun toString(): String {
            return this.id.toString()
        }
    }
}