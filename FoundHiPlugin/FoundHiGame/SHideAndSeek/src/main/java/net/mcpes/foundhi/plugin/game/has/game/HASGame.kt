package net.mcpes.foundhi.plugin.game.has.game

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.event.player.PlayerDeathEvent
import net.mcpes.foundhi.plugin.game.has.HideAndSeek
import net.mcpes.foundhi.plugin.game.has.HideAndSeek.Companion.GAME
import net.mcpes.foundhi.plugin.game.has.HideAndSeek.Companion.PLAYER_DATA
import net.mcpes.foundhi.plugin.game.has.HideAndSeek.Companion.PLAYING
import net.mcpes.foundhi.plugin.game.has.HideAndSeek.Companion.ROOM
import net.mcpes.foundhi.plugin.game.has.HideAndSeek.Companion.TITLE
import net.mcpes.foundhi.plugin.game.has.data.PlayerGameData
import net.mcpes.foundhi.plugin.game.has.data.RoomData
import net.mcpes.foundhi.plugin.game.has.game.task.HASGameTask
import net.mcpes.foundhi.plugin.game.has.game.task.HASWaitTask
import net.mcpes.foundhi.plugin.game.has.utils.EntityTools
import net.mcpes.foundhi.plugin.game.has.utils.EntityUtils

class HASGame(private val id: Int) {
    companion object {
        const val GAME_STATE_WAIT = 0
        const val GAME_STATE_MATCHING = 1
        const val GAME_STATE_GAMING = 2
        const val GAME_STATE_STOPPED = 3
        const val GAME_STATE_RESETING = 4
    }

    var data: RoomData = ROOM[id]!!
    private var state = GAME_STATE_WAIT
    lateinit var players: HashMap<String, Player>
    lateinit var hunter: ArrayList<String>
    lateinit var animal: ArrayList<String>
    lateinit var dead: ArrayList<String>
    lateinit var usedAnimalPos: ArrayList<Int>
    lateinit var gameTask: HASGameTask

    private fun init() {
        GAME[id] = this
        state = GAME_STATE_WAIT
        hunter = ArrayList()
        animal = ArrayList()
        usedAnimalPos = ArrayList()
        players = HashMap()
        data.setGameLevel()
        data.setHunterLocation()
        data.setStopLocation()
        data.setStopLocation()
        data.setAnimalLocation()
    }

    @Synchronized
    fun onJoin(player: Player): Int {
        val pd: PlayerGameData = PLAYER_DATA[player.name]!!
        if (pd.roomID != -1) return -1
        if (this.players.size > data.max) return -2
        if (this.state >= GAME_STATE_GAMING) return -3
        pd.roomID = this.id
        this.players[player.name] = player
        PLAYING.add(player.name)
        pd.state = PlayerGameData.PLAYER_STATE_GAMING //todo matching
        player.setGamemode(2)
        player.inventory.clearAll()
        player.teleport(data.getWaitLocation())
        player.sendTitle(TITLE, "§6§lPowered by hhm", 20, 50, 20)
        this.broadcast(0, "§7<§b" + player.name + "§7> §6加入了房间! (" + this.players.size + "/" + data.max + ")")
        if (this.players.size == data.min) {
            Server.getInstance().scheduler.scheduleAsyncTask(HideAndSeek.instance, HASWaitTask(data, this))
        }
        return 0
    }

    @Synchronized
    fun onQuit(player: Player, online: Boolean) {
        val pd: PlayerGameData = PLAYER_DATA[player.name]!!
        hunter.remove(player.name)
        if (animal.contains(player.name)) {
            players.forEach { _, p -> p.showPlayer(player) }
        }
        animal.remove(player.name)
        PLAYING.remove(player.name)
        this.broadcast(0, "§7<§b" + player.name + "§7> §6退出了房间! (" + this.players.size + "/" + data.max + ")")
        pd.state = GAME_STATE_WAIT
        pd.roomID = -1
        pd.animal.close()
        player.teleport(data.getStopLocation())
        player.setGamemode(0)
        player.removeAllEffects()
        player.inventory.clearAll()
        player.setSpawn(Server.getInstance().defaultLevel.safeSpawn)
        if (online) {
            player.sendMessage("$TITLE§7你已退出游戏房间")
            player.sendMessage("$TITLE§7已经将您传送到游戏结束区域")
        }
    }

    fun onDeath(player: Player, event: PlayerDeathEvent) {
        event.isCancelled = true
        dead.add(player.name)
        EntityTools.callThunder(player.location)
        val msg: String = if (player.killer != null) {
            if (player.killer is Player) {
                player.displayName + " §3被" + (player.killer as Player).displayName + " §3终结了" + "!"
            } else {
                player.displayName + " §3被" + player.killer.name + " §3终结了" + "!"
            }
        } else {
            player.displayName + " §3不知道怎么就一命呜呼了!"
        }
        this.broadcast(0, msg)
        return
    }

    fun onMove(player: Player) {
        if (!animal.contains(player.name)) return
        PLAYER_DATA[player.name]!!.animal.moveToAll(players.values as ArrayList<Player>)
    }

    fun onReincarnation(player: Player) {
        if (!dead.contains(player.name)) return
        when (PLAYER_DATA[player.name]!!.role) {
            1 -> {
                player.teleport(data.getHunterLocation())
                this.broadcast(0, player.displayName + " §e复活了!")

            }
            2 -> {
                players.forEach { _, u -> u.showPlayer(player) }
                PLAYER_DATA[player.name]!!.animal.despawnFromAll()
                PLAYER_DATA[player.name]!!.animal.close()
                animal.remove(player.name)
                hunter.add(player.name)
                player.teleport(data.getHunterLocation())
                this.broadcast(0, player.displayName + " §e作为猎人复活了!")
                player.displayName = "§2[§c猎人§2] " + player.name
                player.nameTag = player.displayName
            }
        }
    }

    fun onStart() {
        this.distributionTeam()
        animal.forEach {
            var rand: Int
            do {
                rand = EntityUtils.rand(0, data.getAnimalLocation().size - 1)
            } while (this.usedAnimalPos.contains(rand))
            this.usedAnimalPos.add(rand)
            val player: Player = players[it]!!
            player.teleport(data.getAnimalLocation()[rand])
        }
        hunter.forEach {
            val player: Player = players[it]!!
            player.teleport(data.getHunterStayLocation())
        }
        //todo message notice
        this.state = GAME_STATE_GAMING
        gameTask = HASGameTask(data, this)
        this.spawnAnimalRobots()
        Server.getInstance().scheduler.scheduleAsyncTask(HideAndSeek.instance, gameTask)
        this.broadcast(0, "§l§7-------------------")
        this.broadcast(0, "$TITLE§2Game Starts!")
        this.broadcast(0, "§l§7-------------------")
    }

    private fun onStop() {
        this.state = GAME_STATE_STOPPED
        this.despawnAnimalRobots()
        gameTask.stopped = true
        players.forEach { _, u ->
            u.sendMessage("$TITLE§5***************")
            u.sendMessage("$TITLE§6游戏已经结束!")
            u.sendMessage("$TITLE§6作为输者,奖励金币500枚!（其实没有")
            u.sendMessage("$TITLE§5***************")
            this.onQuit(u, true)
        }
        this.resetRoom()
    }

    private fun spawnAnimalRobots() {

    }

    private fun despawnAnimalRobots() {

    }

    fun onWin(camp: Int) {
        when (camp) {
            1 -> {
                //猎人获胜
                this.broadcast(0, "$TITLE 猎人获得了最终的胜利!")
                hunter.forEach {
                    val player: Player = players[it]!!
                    player.sendMessage("$TITLE§5***************")
                    player.sendMessage("$TITLE§6游戏已经结束!")
                    player.sendMessage("$TITLE§6作为赢家,奖励金币1000枚!（其实没有")
                    player.sendMessage("$TITLE§5***************")
                    this.onQuit(player, true)
                }
            }
            2 -> {
                //动物获胜
                this.broadcast(0, "$TITLE 动物获得了最终的胜利!")
                animal.forEach {
                    val player: Player = players[it]!!
                    player.sendMessage("$TITLE§5***************")
                    player.sendMessage("$TITLE§6游戏已经结束!")
                    player.sendMessage("$TITLE§6作为赢家,奖励金币1000枚!（其实没有")
                    player.sendMessage("$TITLE§5***************")
                    this.onQuit(player, true)
                }
            }
            else -> return
        }
        this.onStop()
    }

    fun broadcast(type: Int, message: String) {
        when (type) {
            1 -> hunter.forEach { players[it]!!.sendMessage(message) }
            2 -> hunter.forEach { players[it]!!.sendMessage(message) }
            else -> players.forEach { _, player -> player.sendMessage(message) }
        }
    }

    fun broadcast(type: Int, title: String, title2: String, fadeIn: Int, fadeOut: Int, stay: Int) {
        when (type) {
            1 -> hunter.forEach { players[it]!!.sendTitle(title, title2, fadeIn, stay, fadeOut) }
            2 -> hunter.forEach { players[it]!!.sendTitle(title, title2, fadeIn, stay, fadeOut) }
            else -> players.forEach { _, player -> player.sendTitle(title, title2, fadeIn, stay, fadeOut) }
        }
    }

    fun broadcast(type: Int, title: String, fadeIn: Int, fadeOut: Int, stay: Int) {
        when (type) {
            1 -> hunter.forEach { players[it]!!.sendActionBar(title, fadeIn, stay, fadeOut) }
            2 -> hunter.forEach { players[it]!!.sendActionBar(title, fadeIn, stay, fadeOut) }
            else -> players.forEach { _, player -> player.sendActionBar(title, fadeIn, stay, fadeOut) }
        }
    }

    private fun distributionTeam() {
        val hunterCount: Int = (this.players.size % data.animalPlayerProportion).toInt()
        val animalCount: Int = this.players.size - hunterCount
        var nowHC = hunterCount
        var nowAC = animalCount
        if (nowHC == 0) {
            nowHC += 1
            nowAC -= 1
        }
        players.forEach { name, _ ->
            when {
                nowHC == 0 -> {
                    nowAC -= 1
                    PLAYER_DATA[name]!!.chooseRole(2)
                }
                nowAC == 0 -> {
                    nowHC -= 1
                    PLAYER_DATA[name]!!.chooseRole(1)
                }
                else -> when (EntityUtils.rand(0, 1)) {
                    0 -> {
                        nowHC -= 1
                        PLAYER_DATA[name]!!.chooseRole(1)
                    }
                    1 -> {
                        nowAC -= 1
                        PLAYER_DATA[name]!!.chooseRole(2)
                    }
                }
            }
        }
    }

    fun checkTeam() {
        if (animal.size == 0) {
            this.onWin(1)
        }
    }

    private fun resetRoom() {
        this.state = GAME_STATE_RESETING
        this.init()
        this.state = GAME_STATE_WAIT
    }

    init {
        this.init()
    }
}