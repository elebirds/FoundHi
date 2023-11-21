package net.mcpes.foundhi.plugin.game.hotpotato.manager

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.level.Sound
import net.mcpes.foundhi.plugin.game.hotpotato.DataManager
import net.mcpes.foundhi.plugin.game.hotpotato.data.RoomData
import net.mcpes.foundhi.plugin.game.hotpotato.state.GameState
import java.util.*

class RoomManager(val roomName: String) {
    var mode: Int = 0
    var potatoPlayer: Player? = null
    val state = GameState(this)
    var isStart: Boolean = false
        private set
    val roomData: RoomData = RoomData(roomName)
    val alivePlayers = ArrayList<String>()
    val players = HashMap<String, PlayerManager>()
    val playerCount: Int
        get() = this.players.size

    val alivePlayerCount: Int
        get() = this.alivePlayers.size

    fun reset() {
        this.mode = 0
        this.isStart = false
        this.potatoPlayer = null
        this.players.clear()
        this.alivePlayers.clear()
        this.state.resetGame()
    }

    fun start() {
        this.isStart = true
    }

    fun stop() {
        this.isStart = false
    }

    fun addPlayer(player: Player) {
        DataManager.addGamePlayer(player.name, this.roomName)
        this.players[player.name] = PlayerManager(player, this)
        this.addAlivePlayer(player.name)
    }

    fun removePlayer(playerName: String) {
        DataManager.removeGamePlayer(playerName)
        this.players.remove(playerName)
        this.removeAlivePlayer(playerName)
    }


    fun addAlivePlayer(playerName: String) {
        this.alivePlayers.add(playerName)
    }

    fun removeAlivePlayer(playerName: String) {
        this.alivePlayers.remove(playerName)
    }

    fun hasPotatoPlayer(): Boolean = this.potatoPlayer != null

    fun getPlayer(playerName: String): PlayerManager = players[playerName]!!

    fun isPlayer(playerName: String): Boolean = this.players.containsKey(playerName)

    fun isAlive(playerName: String): Boolean = this.alivePlayers.contains(playerName)

    fun sendSoundToAll(sound: Sound, alive: Boolean = false) {
        val players: Collection<String> = if (alive) {
            this.alivePlayers
        } else {
            this.players.keys
        }
        players.forEach {
            val player: Player = Server.getInstance().getPlayerExact(it)
            player.getLevel().addSound(player.position, sound, 100f, 0f, player)
        }
    }

    fun sendTitleToAll(title: String, subtitle: String, fadeIn: Int = -1, stay: Int = -1, fadeOut: Int = -1, alive: Boolean = false) {
        val players: Collection<String> = if (alive) {
            this.alivePlayers
        } else {
            this.players.keys
        }
        players.forEach {
            val player: Player = Server.getInstance().getPlayerExact(it)
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
        }
    }

    fun sendMessageToAll(message: String, alive: Boolean = false) {
        val players: Collection<String> = if (alive) {
            this.alivePlayers
        } else {
            this.players.keys
        }
        players.forEach {
            val player: Player = Server.getInstance().getPlayerExact(it)
            player.sendMessage(message)
        }
    }

    fun sendTipToAll(tip: String, alive: Boolean = false) {
        val players: Collection<String> = if (alive) {
            this.alivePlayers
        } else {
            this.players.keys
        }
        players.forEach {
            val player: Player = Server.getInstance().getPlayerExact(it)
            player.sendTip(tip)
        }
    }

    fun outputCmds() {

    }
}
