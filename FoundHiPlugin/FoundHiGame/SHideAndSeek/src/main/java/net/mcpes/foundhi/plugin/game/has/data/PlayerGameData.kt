package net.mcpes.foundhi.plugin.game.has.data

import cn.nukkit.Player
import net.mcpes.foundhi.plugin.game.has.HideAndSeek.Companion.ROOM
import net.mcpes.foundhi.plugin.game.has.entity.player.PlayerEntity
import net.mcpes.foundhi.plugin.game.has.utils.EntityTools

class PlayerGameData(val name: String, val player: Player) {
    companion object {
        const val PLAYER_STATE_WAIT = 0
        const val PLAYER_STATE_MATCHING = 1 //TODO: matching system
        const val PLAYER_STATE_GAMING = 2
        const val PLAYER_STATE_VISITING = 3
    }

    var roomID: Int = -1
    var state: Int = PLAYER_STATE_WAIT

    //-------游戏信息---------
    var role: Int = -1 //1是猎人 2是动物
    lateinit var animal: PlayerEntity


    fun chooseRole(type: Int) {
        when (type) {
            1 -> {
                this.role = type
                player.displayName = "§2[§c猎人§2] " + player.name
                player.nameTag = player.displayName
            }
            2 -> {
                this.role = type
                player.displayName = "§2[§2动物§2] " + player.name
                player.nameTag = player.displayName
                this.animal = PlayerEntity(player.chunk, EntityTools.getEntityNBT(player), player, ROOM[roomID]!!.animalType)
            }
            else -> return
        }
    }
}