package net.mcpes.foundhi.plugin.game.hotpotato

import cn.nukkit.plugin.PluginBase
import net.mcpes.foundhi.plugin.game.hotpotato.listener.PlayerListener
import java.io.File

class HotPotato : PluginBase() {
    init {
        init(this)
    }

    override fun onLoad() {

    }

    override fun onEnable() {
        this.registerEvents()
        this.createData()
        this.prepareData()
        this.server.logger.info(NAME + " 成功加载")
    }

    override fun onDisable() {

    }

    private fun registerEvents() {
        this.server.pluginManager.registerEvents(PlayerListener(), this)
    }

    private fun prepareData() {
        val rooms = File(instance.dataFolder.toString() + "/rooms/").list()
        if (rooms.isEmpty()) {
            return
        }
        for (room in rooms) {
            if (room != "." && room != "..") {
                room.replace(".yml", "")
                DataManager.addRoom(room)
            }
        }
    }

    private fun createData() {
        File(instance.dataFolder.toString() + "/rooms/").mkdirs()
    }

    companion object {
        lateinit var instance: HotPotato
        @JvmField
        val NAME: String = "§l§f >> §6S§eH§do§3t§6P§ao§4t§5a§6t§8o§e §f<< §6"

        private fun init(i: HotPotato) {
            instance = i
        }
    }
}