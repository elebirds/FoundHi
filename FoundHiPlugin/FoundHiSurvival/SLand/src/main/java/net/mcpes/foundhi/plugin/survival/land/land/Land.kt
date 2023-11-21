package net.mcpes.foundhi.plugin.survival.land.land

import cn.nukkit.Player
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.landWorldCorresponding
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.landWorlds
import net.mcpes.foundhi.plugin.survival.land.utils.LandUtils

/**
 * SLand
 *
 * @author hhm Copyright (c) 2018/5/2
 * version 1.0
 */
data class Land(val id: String,
                val x1: Int,
                val x2: Int,
                val z1: Int,
                val z2: Int,
                var owner: String,
                val permission: HashMap<PlayerType, HashMap<String, Boolean>>,
                var guests: ArrayList<String>,
                val level: String) {
    var selling = false
    var buying = false

    fun bestowal(to: String) {
        this.owner = to
    }

    fun addGuest(guest: String) {
        this.guests.add(guest)
    }

    fun removeGuest(guest: String) {
        this.guests.remove(guest)
    }

    /**
     * 设置领地权限
     * @param playerType 玩家类型
     * @param permissionType 权限类型
     * @param result 是否允许
     *
     * 权限列表:
     *  build 放置/破坏
     *  chest 打开箱子
     *  sign 触摸木牌
     * */
    fun setPermission(playerType: String, permissionType: String, result: Boolean) {
        this.permission[getPlayerType(playerType)]!![permissionType] = result
    }

    fun buy(player: Player) {
        LandUtils.buy(player, this, landWorlds[landWorldCorresponding[level]!!]!!)
    }

    fun sell(player: Player) {
        LandUtils.sell(player, this, landWorlds[landWorldCorresponding[level]!!]!!)
    }

    companion object {
        enum class PlayerType(val typeName: String, val type: Int) {
            NORMAL("normal", 1),
            GUEST("guest", 2),
            OWNER("owner", 3);
        }

        fun getPlayerType(s: String): PlayerType {
            return when (s) {
                "owner" -> PlayerType.OWNER
                "guest" -> PlayerType.GUEST
                else -> PlayerType.NORMAL
            }
        }
    }
}