package net.mcpes.foundhi.core.plugin.banned

import net.mcpes.foundhi.core.dao.Database
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.sf.json.JSONObject
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/25 下午8:47
 * version 1.0
 */
class PlayerBannedData(val id: Int, json: JSONObject) {
    val startTime: Date
    var endTime: Date
    val operator: String
    val banned: String
    val reason: String
    var beLifted: Boolean
    var reviser: String

    init {
        this.startTime = Date(json["st"] as Long)
        this.endTime = Date(json["et"] as Long)
        this.operator = json["o"].toString()
        this.banned = json["b"].toString()
        this.reason = json["r"].toString()
        this.beLifted = json["l"] as Boolean
        this.reviser = json["e"].toString()
    }

    companion object {
        @JvmStatic
        fun loadAll() {
            if (!Database.isEnableRedis()) return
            val redis = Database.getRedisConnection()
            val map = redis.hgetAll("banned_player_init")
            redis.close()
            map.forEach { id, data ->
                this.load(id.toString().toInt(), data.toString())
            }
        }

        @JvmStatic
        fun load(id: Int, data: String) {
            addToMap(PlayerBannedData(id, JSONObject.fromObject(data)))
        }

        @JvmStatic
        fun addToMap(bannedData: PlayerBannedData) {
            FoundHiPlugin.instance.bannedPlayerInfos[bannedData.id] = bannedData
            addPlayerBan(bannedData)
        }

        @JvmStatic
        fun remove(id: Int) {
            synchronized(id) {
                if (!FoundHiPlugin.instance.bannedPlayerInfos.containsKey(id)) return
                val data = FoundHiPlugin.instance.bannedPlayerInfos.remove(id)!!
                FoundHiPlugin.instance.playerBannedInfos[data.banned]?.remove(data)
                FoundHiPlugin.instance.playerEnabledBannedInfos[data.banned]?.remove(data)
            }
        }

        @JvmStatic
        private fun addPlayerBan(bannedData: PlayerBannedData) {
            synchronized(bannedData.banned) {
                if (FoundHiPlugin.instance.playerBannedInfos.containsKey(bannedData.banned)) {
                    FoundHiPlugin.instance.playerBannedInfos[bannedData.banned]!!.add(bannedData)
                } else {
                    val list = ArrayList<PlayerBannedData>()
                    list.add(bannedData)
                    FoundHiPlugin.instance.playerBannedInfos[bannedData.banned] = list
                }
                if (!bannedData.beLifted) {
                    if (FoundHiPlugin.instance.playerEnabledBannedInfos.containsKey(bannedData.banned)) {
                        FoundHiPlugin.instance.playerEnabledBannedInfos[bannedData.banned]!!.add(bannedData)
                    } else {
                        val list = ArrayList<PlayerBannedData>()
                        list.add(bannedData)
                        FoundHiPlugin.instance.playerEnabledBannedInfos[bannedData.banned] = list
                    }
                }
            }
        }
    }
}