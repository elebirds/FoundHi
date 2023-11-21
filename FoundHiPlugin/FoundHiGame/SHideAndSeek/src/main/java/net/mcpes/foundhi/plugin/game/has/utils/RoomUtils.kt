package net.mcpes.foundhi.plugin.game.has.utils

import cn.nukkit.Server
import net.mcpes.foundhi.plugin.game.has.HideAndSeek
import net.mcpes.foundhi.plugin.game.has.HideAndSeek.Companion.ROOM
import net.mcpes.foundhi.plugin.game.has.HideAndSeek.Companion.TITLE
import net.mcpes.foundhi.plugin.game.has.config.RoomConfig
import net.mcpes.foundhi.plugin.game.has.data.RoomData
import java.io.File
import java.io.IOException

object RoomUtils {
    fun loadAllRoom() {
        val rns = File(HideAndSeek.instance.dataFolder.toString() + "/rooms/").list() ?: return
        for (rn in rns) {
            val r = Integer.valueOf(rn.replace(".yml", ""))
            if (this.loadRoom(r)) {
                HideAndSeek.instance.server.logger.info(TITLE + "加载" + r + "号房间成功!")
            } else {
                HideAndSeek.instance.server.logger.info(TITLE + "加载" + r + "号房间失败!")
            }
        }
    }

    fun loadRoom(id: Int): Boolean {
        try {
            val cr = RoomConfig(id)
            Server.getInstance().loadLevel(cr.config.getString("game-world"))
            if (Server.getInstance().isLevelLoaded(cr.config.getString("game-world"))) {
                val data = RoomData(id)
                //TODO
                ROOM[id] = data
            } else
                return false
            return true
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return false
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    fun getFreeID(): Int {
        var max = 0
        val rns = File(HideAndSeek.instance.dataFolder.toString() + "/rooms/").list() ?: return 1
        var length = 0
        for (rn in rns) {
            if (max < Integer.valueOf(rn.replace(".yml", ""))) {
                max = Integer.valueOf(rn.replace(".yml", ""))
            }
            length++
        }
        if (max <= length) {
            return max + 1
        }
        for (i in 1 until max) {
            if (!File(HideAndSeek.instance.dataFolder.toString() + "/rooms/" + i + ".yml").exists()) {
                return i
            }
        }
        return max + 1
    }

    @Throws(IOException::class)
    fun loadWorld(worldName: String) {
        val dataFolder: String = HideAndSeek.instance.dataFolder.toString()
        val serverWorldFolder: String = HideAndSeek.instance.dataFolder.parentFile.parentFile.toString()
        if (!Server.getInstance().isLevelLoaded(worldName)) {
            FileUtils.copy(File("$dataFolder/worlds/$worldName/").canonicalPath, "$serverWorldFolder/worlds/$worldName/")
            Server.getInstance().loadLevel(worldName)
        } else {
            try {
                val players = Server.getInstance().getLevelByName(worldName).players
                if (players.isNotEmpty()) {
                    players.forEach { _, v -> v.teleport(Server.getInstance().defaultLevel.spawnLocation) }
                }
            } catch (e: Exception) {
                // 屏蔽
            }

            Server.getInstance().unloadLevel(Server.getInstance().getLevelByName(worldName))
            FileUtils.remove(File("$serverWorldFolder/worlds/$worldName/"))
            FileUtils.copy(File("$dataFolder/worlds/$worldName/").canonicalPath, "$serverWorldFolder/worlds/$worldName/")
            Server.getInstance().loadLevel(worldName)
        }
    }
}