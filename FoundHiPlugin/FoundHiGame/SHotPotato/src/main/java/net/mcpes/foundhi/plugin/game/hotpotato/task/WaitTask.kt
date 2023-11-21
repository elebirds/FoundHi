package net.mcpes.foundhi.plugin.game.hotpotato.task

import cn.nukkit.level.Sound
import cn.nukkit.scheduler.AsyncTask
import net.mcpes.foundhi.plugin.game.hotpotato.HotPotato
import net.mcpes.foundhi.plugin.game.hotpotato.data.RoomData
import net.mcpes.foundhi.plugin.game.hotpotato.manager.RoomManager
import net.mcpes.foundhi.plugin.game.hotpotato.state.GameState

class WaitTask(state: GameState) : AsyncTask() {
    private val roomManager: RoomManager = state.roomManager
    private val roomData: RoomData = roomManager.roomData
    private var waitTick: Int = roomData.wait_time

    override fun onRun() {
        while (true) {
            val playerCount: Int = roomManager.playerCount
            if (playerCount < roomData.min_player) {
                roomManager.sendMessageToAll(HotPotato.NAME + "§c游戏人数不足，暂停开始游戏")
                return
            }
            if (this.waitTick > 5 && playerCount == roomData.max_player) {
                roomManager.sendMessageToAll(HotPotato.NAME + "§c游戏人数已满，即将开始游戏")
                this.waitTick = 5
            }
            when (this.waitTick) {
                in 1..10 -> {
                    roomManager.sendTitleToAll(HotPotato.NAME, "§l§3 ${waitTick}§e 秒", 0, 20, 0)
                    roomManager.sendSoundToAll(Sound.RANDOM_CLICK)
                }
                0 -> {
                    roomManager.sendTitleToAll(HotPotato.NAME, "§l§e游戏开始，Go！", 0, 20, 0)
                    roomManager.sendSoundToAll(Sound.RANDOM_ANVIL_USE)
                    if (!roomManager.isStart) {
                        roomManager.start()
                        return
                    }
                }
            }
            this.waitTick--
            Thread.sleep(1000)
        }
    }

}