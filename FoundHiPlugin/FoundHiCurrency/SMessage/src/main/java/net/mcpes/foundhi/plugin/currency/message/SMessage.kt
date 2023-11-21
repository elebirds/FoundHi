package net.mcpes.foundhi.plugin.currency.message

import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerChatEvent
import cn.nukkit.plugin.PluginBase
import cn.nukkit.utils.Config
import cn.nukkit.utils.ConfigSection
import net.mcpes.foundhi.core.network.protocol.chat.PlayerChatPacket
import net.mcpes.foundhi.core.plugin.FoundHiPlugin

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/13
 * version 1.0
 */
class SMessage : PluginBase(), Listener {
    companion object {
        lateinit var config: Config
        var chatId: String = ""
        var enableChat: Boolean = false

        fun init(dataFolder: String) {
            config = Config("$dataFolder/config.yml", Config.YAML, object : ConfigSection() {
                init {
                    this["跨服开启"] = false
                    this["跨服聊天id"] = ""
                }
            })
            enableChat = config["跨服开启"] as Boolean
            chatId = config["跨服聊天id"] as String
        }
    }

    override fun onLoad() {
        SMessage.init(this.dataFolder.path)
    }

    override fun onEnable() {
        this.server.pluginManager.registerEvents(this, this)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(event: PlayerChatEvent) {
        val pk = PlayerChatPacket()
        pk.player = event.player.name
        pk.message = event.message
        pk.server = FoundHiPlugin.instance.id
        FoundHiPlugin.instance.dataPacket(pk)
    }
}