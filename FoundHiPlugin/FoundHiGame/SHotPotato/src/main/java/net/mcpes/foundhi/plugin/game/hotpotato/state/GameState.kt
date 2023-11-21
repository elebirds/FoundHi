package net.mcpes.foundhi.plugin.game.hotpotato.state

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.item.Item
import cn.nukkit.level.Sound
import cn.nukkit.level.particle.ExplodeParticle
import cn.nukkit.level.particle.FloatingTextParticle
import cn.nukkit.level.particle.HugeExplodeParticle
import cn.nukkit.level.particle.HugeExplodeSeedParticle
import cn.nukkit.math.Vector3
import net.mcpes.foundhi.plugin.game.hotpotato.HotPotato
import net.mcpes.foundhi.plugin.game.hotpotato.manager.RoomManager
import net.mcpes.foundhi.plugin.game.hotpotato.task.PotatoBoomTask
import net.mcpes.foundhi.plugin.game.hotpotato.task.PropResetTask
import net.mcpes.foundhi.plugin.game.hotpotato.task.StopTask
import net.mcpes.foundhi.plugin.game.hotpotato.task.WaitTask
import java.util.*

/**
 * HotPotato
 *
 * @author hhm Copyright (c) 2018/5/1
 * version 1.0
 */
class GameState(val roomManager: RoomManager) {
    private val roomData = roomManager.roomData
    private var potatoBoomTask = PotatoBoomTask(this)

    fun resetGame() {
        roomManager.reset()
    }

    fun kickAllPlayers() {
        roomManager.players.keys.forEach {
            this.onQuit(it)
        }
    }

    fun onJoin(playerName: String) {
        val player = HotPotato.instance.server.getPlayerExact(playerName)
        if (roomManager.playerCount >= roomData.max_player) {
            player.sendMessage(HotPotato.NAME + " §4游戏玩家已经满员啦！请期待下一局游戏")
            return
        }
        if (roomManager.isStart) {
            player.sendMessage(HotPotato.NAME + " §4游戏已经开始啦！请期待下一局游戏")
            return
        }
        if (player.isCreative) {
            player.sendMessage(HotPotato.NAME + " §4创造模式无法加入游戏！")
            return
        }
        if (player.allowFlight) {
            player.sendMessage(HotPotato.NAME + " §4飞行模式无法加入游戏")
            return
        }
        if (roomManager.isPlayer(playerName)) {
            player.sendMessage(HotPotato.NAME + " §4你已经在游戏中了！")
        }
        if (player.inventory.heldItemIndex == 8) {
            player.inventory.heldItemIndex = 0
        }
        player.teleport(roomData.getWaitLoc())
        roomManager.addPlayer(player)
        player.setScale(1f)
        player.inventory.clearAll()
        player.inventory.setItem(8, Item.get(324).setCustomName("§l§e双击离开游戏房间"))
        this.addFloatingText(player)
        player.sendActionBar("§e欢迎加入游戏", 20, 30, 20)
        this.roomManager.sendMessageToAll(HotPotato.NAME + "玩家{$playerName}加入了游戏")
        this.roomManager.sendMessageToAll(HotPotato.NAME + "§6游戏当前人数:" + roomManager.playerCount)
        if (roomManager.playerCount == roomData.min_player) {
            if (roomManager.mode != 1) {
                if (roomData.getGameLoc().getLevel() != null) {
                    roomManager.mode = 1
                    this.roomManager.sendMessageToAll(HotPotato.NAME + "§6已经达到标准人数啦！游戏即将开始")
                    Server.getInstance().scheduler.scheduleAsyncTask(HotPotato.instance, WaitTask(this))
                    return
                } else {
                    this.roomManager.sendMessageToAll("§4游戏世界未加载！！请联系管理团队")
                }
            }
        }
    }

    fun onDeath(playerName: String) {
        val player = HotPotato.instance.server.getPlayerExact(playerName)
        roomManager.removeAlivePlayer(playerName)
        player.teleport(roomData.getLookLoc())
        player.sendTitle(HotPotato.NAME, "§e你已进入观战模式", 20, 30, 20)
        roomManager.sendMessageToAll(HotPotato.NAME + "玩家${playerName}死亡")
        roomManager.sendMessageToAll(HotPotato.NAME + "游戏剩余：${this.roomManager.alivePlayerCount}名玩家")
    }

    fun onQuit(playerName: String) {
        val player = HotPotato.instance.server.getPlayerExact(playerName)
        val playerManager = roomManager.getPlayer(playerName)
        player.inventory.clearAll()
        playerManager.setHelmet(Item.get(0))
        playerManager.returnAll()
        player.teleport(roomData.getOverLoc())
        roomManager.removePlayer(playerName)
        if (roomManager.isStart) {
            if (playerManager.isPotatoPlayer()) {
                this.randomPotatoPlayer()
                potatoBoomTask.resetBoomTime()
            }
        }
        roomManager.sendMessageToAll(HotPotato.NAME + "玩家${playerName}退出了游戏")
        if (roomManager.mode == 2) {
            if (this.roomManager.playerCount == 0) {
                this.resetGame()
            }
        }
    }

    fun gameStart() {
        this.roomManager.mode = 2
        this.roomManager.start()
        roomManager.players.keys.forEach {
            val player = Server.getInstance().getPlayerExact(it)
            player.teleport(roomData.getGameLoc())
        }
        Server.getInstance().scheduler.scheduleAsyncTask(HotPotato.instance, PropResetTask(this))
        Server.getInstance().scheduler.scheduleAsyncTask(HotPotato.instance, this.potatoBoomTask)
        Server.getInstance().scheduler.scheduleAsyncTask(HotPotato.instance, StopTask(this))
        roomManager.sendMessageToAll(HotPotato.NAME + "§6游戏开始咯!")
    }

    fun gameStop() {
        this.roomManager.sendMessageToAll(HotPotato.NAME + "§6游戏已经结束啦!")
        this.roomManager.stop()
        this.kickAllPlayers()
        this.resetGame()
    }

    fun suspendGame() {
        this.roomManager.players.keys.forEach {
            val player = Server.getInstance().getPlayerExact(it)
            player.teleport(this.roomData.getWaitLoc())
            player.inventory.setItem(8, Item.get(324).setCustomName("§l§e双击离开游戏房间"))
        }
        this.roomManager.mode = 0
    }

    fun setPotatoBoom() {
        val player = this.roomManager.potatoPlayer
        val pos = player!!.position
        player.level.addParticle(ExplodeParticle(pos))
        player.level.addParticle(HugeExplodeSeedParticle(pos))
        player.level.addParticle(HugeExplodeParticle(pos))
        player.level.addSound(Vector3(player.x, player.y, player.z), Sound.RANDOM_EXPLODE)
        this.onDeath(player.name)
        player.inventory.clearAll()
        player.inventory.setItem(8, Item.get(324).setCustomName("§l§e双击离开游戏房间"))
    }

    fun randomPotatoPlayer() {
        val alivePlayers = roomManager.alivePlayers
        val randomPlayer = Random().nextInt(alivePlayers.size - 1)
        val player = Server.getInstance().getPlayerExact(alivePlayers[randomPlayer])
        this.roomManager.potatoPlayer = player
        this.roomManager.getPlayer(player.name).setPotato()
        this.roomManager.sendMessageToAll(HotPotato.NAME + "§6烫手的山芋落在了玩家§4${player.name}§e的手上")

    }

    fun addFloatingText(player: Player) {
        val text = HotPotato.NAME + "\n" +
                "        §b玩法简介：游戏开始时，有随机一个玩家得到烫手的山芋\n" +
                "        §6山芋玩家一定在规定时间内必须点击其他玩家，将山芋丢出\n" +
                "        §d时间过后手持山芋的玩家将会BOOM\n" +
                "        §f当然普通玩家也必须要避开山芋玩家哦！"
        val pos = this.roomData.getWaitLoc()
        val particle = FloatingTextParticle(Vector3(pos.x, pos.y, pos.z), null, text)
        player.level.addParticle(particle)
    }
}