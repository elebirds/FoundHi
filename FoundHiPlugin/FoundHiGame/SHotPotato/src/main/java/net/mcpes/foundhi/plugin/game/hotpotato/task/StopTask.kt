package net.mcpes.foundhi.plugin.game.hotpotato.task

import cn.nukkit.Server
import cn.nukkit.level.Sound
import cn.nukkit.scheduler.AsyncTask
import net.mcpes.foundhi.plugin.game.hotpotato.HotPotato
import net.mcpes.foundhi.plugin.game.hotpotato.data.RoomData
import net.mcpes.foundhi.plugin.game.hotpotato.manager.RoomManager
import net.mcpes.foundhi.plugin.game.hotpotato.state.GameState

class StopTask(private var state: GameState) : AsyncTask() {
    private val roomManager: RoomManager = state.roomManager
    private val roomData: RoomData = this.roomManager.roomData
    private var stopTick: Int = this.roomData.stop_time

    override fun onRun() {
        while (true) {
            if (this.roomManager.alivePlayerCount < 1) {
                Server.getInstance().logger.info("房间${this.roomManager.roomName}无一生存玩家，已强制停止游戏")
                this.state.gameStop()
                return
            }
            if (this.roomManager.playerCount <= 0) {
                Server.getInstance().logger.info("房间${this.roomManager.roomName}无一玩家，已强制停止游戏")
                this.state.gameStop()
                return
            }
            if (this.roomManager.alivePlayerCount == 1) {
                if (this.roomManager.playerCount == 1) {
                    this.roomManager.sendMessageToAll(HotPotato.NAME + "§l§3本场游戏人数不足，已停止!", true)
                    this.state.gameStop()
                    return
                }
                val winnerName: String = roomManager.alivePlayers[0]
                this.roomManager.sendMessageToAll(HotPotato.NAME + "§l§3恭喜你成功成为本场游戏最终赢家!", true)
                this.roomManager.sendTitleToAll(HotPotato.NAME, "§l§3恭喜玩家${winnerName}成为本场游戏最终赢家!", 10, 40, 10)
                Server.getInstance().broadcastMessage(HotPotato.NAME + "§l§3恭喜玩家${winnerName}赢得了比赛!")
                this.state.gameStop()
                this.roomManager.outputCmds()

            }
            Thread.sleep(1000)
            this.stopTick--
            when (this.stopTick) {
                in 1..10 -> {
                    this.roomManager.sendMessageToAll(HotPotato.NAME + "游戏还有${this.stopTick}秒结束")
                    this.roomManager.sendSoundToAll(Sound.RANDOM_CLICK)
                }
                0 -> {
                    this.roomManager.sendTitleToAll(HotPotato.NAME, "§l§e游戏结束，没有人获得最终胜利！", 10, 20, 10)
                    this.roomManager.sendSoundToAll(Sound.RANDOM_ANVIL_USE)
                    this.state.gameStop()
                    return
                }
            }
        }
    }
}