package net.mcpes.foundhi.plugin.game.has.game.task

import cn.nukkit.scheduler.AsyncTask
import net.mcpes.foundhi.plugin.game.has.data.RoomData
import net.mcpes.foundhi.plugin.game.has.game.HASGame

class HASWaitTask(private var data: RoomData, private var game: HASGame) : AsyncTask() {
    private var waitTick: Int = 0

    override fun onRun() {
        while (true) {
            this.waitTick--
            if (game.players.size < this.data.min) {
                game.broadcast(0, "§c游戏人数不足，暂停开始游戏")
                return
            }
            if (this.waitTick > 5 && game.players.size == this.data.max) {
                this.waitTick = 5
                game.broadcast(0, "§6达到最大游戏人数,五秒后开始游戏")
            }
            //TODO message notice
            Thread.sleep(1000)
            if (this.waitTick <= 0) {
                this.game.onStart()
                return
            }
        }
    }

    init {
        this.waitTick = data.waitTime
    }
}