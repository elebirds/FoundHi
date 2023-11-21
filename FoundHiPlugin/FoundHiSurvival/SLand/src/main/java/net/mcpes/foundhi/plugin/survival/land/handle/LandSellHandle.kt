package net.mcpes.foundhi.plugin.survival.land.handle

import cn.nukkit.Player
import net.mcpes.foundhi.core.handle.Handle
import net.mcpes.foundhi.core.utils.getMoneyName
import net.mcpes.foundhi.core.utils.moneyFormat
import net.mcpes.foundhi.plugin.survival.land.land.Land
import net.mcpes.foundhi.plugin.survival.land.land.LandWorld
import net.mcpes.foundhi.plugin.survival.land.utils.LandUtils


/**
 * SLand
 *
 * @author hhm Copyright (c) 2018/5/5
 * version 1.0
 */
class LandSellHandle(private val player: Player, val land: Land, val landWorld: LandWorld) : Handle() {
    override fun handle() {
        if (player.isOnline) {
            when (this.result) {
                0 -> {
                    LandUtils.cleanLand(land.id, landWorld.level)
                    LandUtils.delete(land, landWorld)
                    player.sendMessage("§l§8┃§bS§au§dm§em§4i§6t§8┃§8 - NOTICE: §a§l你成功出售了ID为${land.id}-${landWorld.level}的地皮,获得了${landWorld.money.moneyFormat()}枚${landWorld.moneyType.getMoneyName()}!")
                }
                -1 -> player.sendMessage("§a§l操作失败:未知错误!请尝试重新进入服务器!")
                -2 -> player.sendMessage("§a§l操作失败:未知参数错误!")
            }
        } else {
            if (this.result != 0) return
            LandUtils.cleanLand(land.id, landWorld.level)
            LandUtils.delete(land, landWorld)
        }
    }
}