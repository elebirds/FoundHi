package net.mcpes.foundhi.plugin.game.has.config

import cn.nukkit.utils.Config
import net.mcpes.foundhi.plugin.game.has.HideAndSeek
import net.mcpes.foundhi.plugin.game.has.data.RoomData
import java.io.File
import java.util.*

internal class RoomConfig(private val id: Int) {
    private val file: File
    val config: Config

    init {
        this.file = File(HideAndSeek.instance.dataFolder.toString() + "/rooms/" + id + ".yml")
        this.config = Config(this.file, 2)
    }

    fun addRoom(data: RoomData) {
        val room = LinkedHashMap<String, Any>()
        room["id"] = data.id
        room["display-name"] = data.disName
        room["min"] = data.min
        room["max"] = data.max
        room["game-time"] = data.gameTime
        room["game-world"] = data.gameWorld
        room["startX"] = data.startX
        room["startZ"] = data.startZ
        room["endX"] = data.endX
        room["endZ"] = data.endZ
        room["wait-time"] = data.waitTime
        room["wait-pos"] = data.waitPos
        room["stop-pos"] = data.stopPos
        room["hunter-pos"] = data.hunterPos
        room["hunter-stay-pos"] = data.hunterStayPos
        room["animal-pos"] = data.animalPos
        room["animal-player-proportion"] = data.animalPlayerProportion
        room["animal-robot-proportion"] = data.animalRobotProportion
        room["animal-type"] = data.animalType
        this.config.setAll(room)
        this.config.save()
    }

    fun delRoom() {
        this.config.setAll(LinkedHashMap())
        this.config.save()
        this.file.delete()
    }
}