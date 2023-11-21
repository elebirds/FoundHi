package net.mcpes.foundhi.plugin.game.has.game.task

import cn.nukkit.Player
import cn.nukkit.scheduler.AsyncTask
import net.mcpes.foundhi.plugin.game.has.data.RoomData
import net.mcpes.foundhi.plugin.game.has.game.HASGame

class HASGameTask(private var data: RoomData, private var game: HASGame) : AsyncTask() {
    private var gameTick: Int = 0
    private var tick: Int = 0
    private var stayTick: Int = 0
    var stopped: Boolean = false

    override fun onRun() {
        while (!stopped) {
            if (stayTick != -1) {
                stayTick++
                if (data.stayTime == stayTick) {
                    stayTick = -1
                    //todo message notice
                    game.hunter.forEach {
                        val player: Player = game.players[it]!!
                        player.teleport(data.getHunterLocation())
                    }
                }
            } else {
                this.gameTick--
                this.tick++
                //TODO Game Start Task,send tip
            }
            game.checkTeam()
            if (gameTick == 0) {
                game.onWin(2)
            }
            Thread.sleep(1000)
        }
    }

    init {
        this.gameTick = data.waitTime
    }
}