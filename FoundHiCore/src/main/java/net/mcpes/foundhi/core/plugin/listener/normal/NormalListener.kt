package net.mcpes.foundhi.core.plugin.listener.normal

import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerCreationEvent
import cn.nukkit.event.player.PlayerJoinEvent
import cn.nukkit.event.player.PlayerPreLoginEvent
import cn.nukkit.event.player.PlayerQuitEvent
import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.network.protocol.player.PlayerQuitPacket
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import java.text.SimpleDateFormat


class NormalListener : Listener {
    @EventHandler
    fun onNewPlayer(event: PlayerCreationEvent) {
        event.playerClass = NukkitPlayer::class.java
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        if (event.player !is NukkitPlayer) return
        val pname = event.player.name
        if (!event.reason.contains("Transfer")) {
            (event.player as NukkitPlayer).sendBagTag()
            val pk = PlayerQuitPacket()
            pk.id = pname
            FoundHiPlugin.instance.dataPacket(pk)
        }
    }

    @EventHandler
    fun onPreLogin(event: PlayerPreLoginEvent) {
        FoundHiPlugin.instance.playerEnabledBannedInfos[event.player.name]?.forEach {
            if (!it.beLifted) {
                if (System.currentTimeMillis() < it.endTime.time) {
                    event.player.kick("§c此账户已被封禁 §2持续至:${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(it.endTime)}\n§6封禁原因:${it.reason}")
                    return
                }
            }
        }
        if (ServerType.getType() != ServerType.LOGIN) {
            if (!FoundHiPlugin.instance.hasWillJoin(event.player.name)) {
                event.player.kick("§c不可以直接进入指定服务器!")
            } else {
                FoundHiPlugin.instance.getPlayer(event.player.name)!!.resetKick()
                FoundHiPlugin.instance.delWillJoin(event.player.name)
                FoundHiPlugin.instance.willKick.remove(event.player.name)
            }
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val handle = FoundHiPlugin.instance.getAndRemoveLoadBagHandle(event.player.name)
        if (handle != null) {
            handle.player = event.player
            handle.handle()
        }
    }
}
