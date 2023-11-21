package net.mcpes.foundhi.plugin.game.has.listener

import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerJoinEvent
import cn.nukkit.event.player.PlayerQuitEvent

import net.mcpes.foundhi.plugin.game.has.HideAndSeek.Companion.PLAYER_DATA
import net.mcpes.foundhi.plugin.game.has.data.PlayerGameData

class PlayerListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        PLAYER_DATA[event.player.name] = PlayerGameData(event.player.name, event.player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        PLAYER_DATA.remove(event.player.name)
    }
}