package net.mcpes.foundhipro.core.app.manager

import net.mcpes.foundhipro.core.app.CoreServer
import net.mcpes.foundhipro.core.app.data.player.Player
import net.mcpes.foundhipro.core.logger.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/9
 * version 1.0
 */
class PlayerManager {
    /**
     * 玩家列表
     */
    private var allPlayers = CopyOnWriteArrayList<String>()
    /**
     * 登录玩家的信息
     */
    private var players = ConcurrentHashMap<String, Player>()
    /**
     * 退出30秒内玩家的信息
     */
    private var exitedPlayers = ConcurrentHashMap<String, Player>()
    /**
     * 玩家保存任务
     */
    private var saveTask = ConcurrentHashMap<String, Int>()

    var playerCount: Int = 0

    fun getPlayer(id: String) = this.players[id]

    fun addPlayer(id: String) {
        if (this.isPlayerOnline(id)) return
        do {
            if (this.exitedPlayers.containsKey(id)) {
                players[id] = exitedPlayers.remove(id) ?: continue
                playerCount++
                exitedPlayers.remove(id)
                saveTask[id]?.let { CoreServer.instance.scheduler.cancelTask(it) }
                saveTask.remove(id)
                Logger.instance.info("玩家 " + id + " 加入了Summit服务器,现在人数($playerCount/${this.playerCount})")
                return
            }
        } while (false)
        this.players[id] = Player(id)
        playerCount++
        Logger.instance.info("玩家 " + id + " 加入了Summit服务器,现在人数($playerCount/${this.playerCount})")
    }

    @JvmOverloads
    fun delPlayer(id: String, kicked: Boolean = false) {
        val player = this.players.remove(id) ?: return
        playerCount--
        this.exitedPlayers[id] = player
        saveTask[id] = CoreServer.instance.scheduler.scheduleDelayedTask(Runnable {
            //player.save()
            this.alreadySavePlayerData(player.name)
        }, 600).taskId
        if (!kicked) Logger.instance.info("玩家 $id 退出了服务器,现在人数($playerCount/${this.playerCount})")
    }

    @JvmOverloads
    fun kickPlayer(player: Player, reason: String = "无原因") {
        this.delPlayer(player.name, true)
        Logger.instance.info("玩家 ${player.name} 被踢出了服务器,原因:$reason,现在人数($playerCount/${this.playerCount}")
    }

    fun alreadySavePlayerData(id: String) {
        this.saveTask.remove(id)
        this.exitedPlayers.remove(id)
    }

    fun isPlayerOnline(id: String) = this.players.containsKey(id)

    fun hasPlayer(id: String) = this.allPlayers.contains(id)

    fun getPlayers() = this.players.values
}