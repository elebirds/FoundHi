package net.mcpes.foundhi.plugin.game.has

import cn.nukkit.plugin.PluginBase
import net.mcpes.foundhi.plugin.game.has.command.HideAndSeekCommand
import net.mcpes.foundhi.plugin.game.has.data.PlayerGameData
import net.mcpes.foundhi.plugin.game.has.data.RoomData
import net.mcpes.foundhi.plugin.game.has.game.HASGame
import net.mcpes.foundhi.plugin.game.has.listener.FloatingTextListener
import net.mcpes.foundhi.plugin.game.has.utils.RoomUtils
import java.io.File

class HideAndSeek : PluginBase() {
    init {
        init(this)
    }

    companion object {
        const val TITLE = "§l§f >> §6H§ei§dd§3e§6A§an§4d§5S§2e§1e§8k§e §f<< §6"
        const val VERSION = "0.1.0"
        val ROOM = HashMap<Int, RoomData>()
        val GAME = HashMap<Int, HASGame>()
        val PLAYER_DATA = HashMap<String, PlayerGameData>()
        val PLAYING = ArrayList<String>()
        lateinit var instance: HideAndSeek

        fun init(p: HideAndSeek) {
            instance = p
        }

        fun folderInit() {
            File(instance.dataFolder.toString() + "/rooms/").mkdirs()
            File(instance.dataFolder.toString() + "/worlds/").mkdirs()
        }
    }

    override fun onLoad() {
        this.server.logger.info("$TITLE V$VERSION 加载成功!")
        folderInit()
    }

    override fun onEnable() {
        this.server.commandMap.register("has", HideAndSeekCommand())
        this.server.pluginManager.registerEvents(FloatingTextListener(), this)
        RoomUtils.loadAllRoom()
        this.server.logger.info("$TITLE V$VERSION 启动成功!")
    }

    override fun onDisable() {

        this.server.logger.info("$TITLE V$VERSION 关闭成功!")
    }
}