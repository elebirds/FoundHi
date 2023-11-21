package net.mcpes.foundhi.plugin.game.hotpotato.task

import cn.nukkit.level.Sound
import cn.nukkit.math.Vector3
import cn.nukkit.scheduler.AsyncTask
import net.mcpes.foundhi.plugin.game.hotpotato.HotPotato
import net.mcpes.foundhi.plugin.game.hotpotato.data.RoomData
import net.mcpes.foundhi.plugin.game.hotpotato.manager.RoomManager
import net.mcpes.foundhi.plugin.game.hotpotato.state.GameState

class PotatoBoomTask(private var state: GameState) : AsyncTask() {
    private val roomManager: RoomManager = state.roomManager
    private val roomData: RoomData = this.roomManager.roomData
    private var boomTick = roomData.potato_boom_time

    override fun onRun() {
        loop@ while (true) {
            if (!roomManager.isStart) {
                return
            }
            if (!this.roomManager.hasPotatoPlayer()) {
                roomManager.sendTipToAll(HotPotato.NAME + "§6烫手的山芋还有${boomTick}秒随机发放到一人手上")
            } else {
                roomManager.sendTipToAll(HotPotato.NAME + "§4烫手的山芋§6还有${boomTick}秒BOOM")
            }
            Thread.sleep(1000)
            when (boomTick) {
                in 1..5 -> {
                    roomManager.sendSoundToAll(Sound.RANDOM_CLICK)
                }
                0 -> {
                    if (roomManager.hasPotatoPlayer()) {
                        val player = roomManager.potatoPlayer
                        player?.getLevel()?.addSound(Vector3(player.x, player.y, player.z), Sound.RANDOM_EXPLODE)
                        state.setPotatoBoom()
                        if (roomManager.alivePlayerCount <= 1) {
                            continue@loop
                        }
                        state.randomPotatoPlayer()
                        this.boomTick = roomData.potato_boom_time
                    }
                    roomManager.sendSoundToAll(Sound.RANDOM_ANVIL_USE)
                }
            }
            this.boomTick--

        }
    }

    fun resetBoomTime() {
        this.boomTick = roomData.potato_boom_time
    }

}