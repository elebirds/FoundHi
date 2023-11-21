package net.mcpes.foundhi.plugin.game.hotpotato.data

import cn.nukkit.level.Location
import cn.nukkit.utils.Config
import net.mcpes.foundhi.plugin.game.hotpotato.HotPotato
import net.mcpes.foundhi.plugin.game.hotpotato.utils.Utils
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * HotPotato
 *
 * @author hhm Copyright (c) 2018/5/1
 * version 1.0
 */

class RoomData(private val roomName: String) {
    val max_player: Int
    val min_player: Int
    val wait_time: Int
    val stop_time: Int
    val prop_rest_time: Int
    val potato_boom_time: Int
    var wait: String
    var game: String
    var look: String
    var over: String
    var props: HashMap<Any, Any>
    val winner_cmd: ArrayList<Any>
    val config: Config = Config(File(HotPotato.instance.dataFolder.toString() + "/rooms/" + this.roomName + ".yml"), 2)


    init {
        if (this.config.all.isEmpty()) {
            this.addNewRoomConfig()
        }
        val roomConfig: HashMap<String, Any> = config.all as HashMap<String, Any>
        this.max_player = roomConfig["max-player"] as Int
        this.min_player = roomConfig["min-player"] as Int
        this.stop_time = roomConfig["stop-time"] as Int
        this.wait_time = roomConfig["wait-time"] as Int
        this.prop_rest_time = roomConfig["prop-reset-time"] as Int
        this.potato_boom_time = roomConfig["potato-boom-time"] as Int
        this.wait = roomConfig["wait"] as String
        this.game = roomConfig["game"] as String
        this.look = roomConfig["look"] as String
        this.over = roomConfig["over"] as String
        this.props = roomConfig["props"] as HashMap<Any, Any>
        this.winner_cmd = roomConfig["winner-cmd"] as ArrayList<Any>
    }

    private fun addNewRoomConfig() {
        val room = LinkedHashMap<String, Any>()
        room["min-player"] = 3
        room["max-player"] = 20
        room["wait-time"] = 20
        room["stop-time"] = 300
        room["prop-reset-time"] = 30
        room["potato-boom-time"] = 10
        room["wait"] = "0,0,0,world"
        room["game"] = "0,0,0,world"
        room["look"] = "0,0,0,world"
        room["over"] = "0,0,0,world"
        room["props"] = HashMap<Any, Any>()
        room["winner-cmd"] = ArrayList<Any>()
        config.setAll(room)
        config.save()
    }

    public fun saveAll() {
        val room = LinkedHashMap<String, Any>()
        room.put("max-player", this.max_player)
        room.put("min-player", this.min_player)
        room.put("wait-time", this.wait_time)
        room.put("stop-time", this.stop_time)
        room.put("prop-reset-time", this.prop_rest_time)
        room.put("potato-boom-time", this.potato_boom_time)
        room.put("wait", this.wait)
        room.put("game", this.game)
        room.put("look", this.look)
        room.put("over", this.over)
        room.put("props", this.props)
        room.put("winner-cmd", this.winner_cmd)
        config.setAll(room)
        config.save()
    }

    fun getWaitLoc(): Location {
        return Utils.stringToLocation(this.wait)!!
    }

    fun getGameLoc(): Location {
        return Utils.stringToLocation(this.game)!!
    }

    fun getLookLoc(): Location {
        return Utils.stringToLocation(this.look)!!
    }

    fun getOverLoc(): Location {
        return Utils.stringToLocation(this.over)!!
    }
}
