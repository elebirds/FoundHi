package net.mcpes.foundhi.plugin.game.has.listener

import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerJoinEvent
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.scheduler.AsyncTask
import net.mcpes.foundhi.plugin.game.has.HideAndSeek
import net.mcpes.foundhi.plugin.game.has.utils.FloatingTextPacket

class FloatingTextListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        HideAndSeek.instance.server.scheduler.scheduleAsyncTask(HideAndSeek.instance, object : AsyncTask() {
            override fun onRun() {
                try {
                    Thread.sleep(1000L)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                for (data in FloatingTextPacket.datas.values) {
                    val pk = data["packet"] as DataPacket
                    event.player.dataPacket(pk)
                }
            }
        })
    }
}