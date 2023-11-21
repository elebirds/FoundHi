package net.mcpes.foundhi.plugin.survival.land.listener

import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.block.BlockBreakEvent
import cn.nukkit.event.player.PlayerInteractEvent
import net.mcpes.foundhi.core.utils.cast
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.defaultPermissions
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.landWorldCorresponding
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.landWorlds
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.lands
import net.mcpes.foundhi.plugin.survival.land.land.Land
import net.mcpes.foundhi.plugin.survival.land.utils.LandUtils

/**
 * SLand
 *
 * @author hhm Copyright (c) 2018/5/4
 * version 1.0
 */
class LandBlockListener : Listener {
    @EventHandler
    fun onBuy(event: PlayerInteractEvent) {
        val level = event.block.level.name
        if (!landWorldCorresponding.containsKey(level)) return
        val landWorld = landWorlds[landWorldCorresponding[level]!!]!!
        if (!LandUtils.isLandBlock(event.block, landWorld.type)) return
        val x1 = event.block.x.toInt()
        val z1 = event.block.z.toInt()
        val id = "$x1-$z1"
        if (lands.containsKey("$id-$level")) {
            event.player.sendMessage("§c操作失败:领地已被购买!")
            //领地已被购买
            return
        }
        val x2 = if (landWorld.type == 1) {
            x1 + 39
        } else {
            x1 + 55
        }
        val z2 = if (landWorld.type == 1) {
            z1 + 39
        } else {
            z1 + 55
        }
        val land = Land(id, x1, x2, z1, z2, event.player.name, defaultPermissions.clone().cast(), ArrayList(), level)
        land.buy(event.player)
    }

    @EventHandler
    fun onSell(event: BlockBreakEvent) {
        val level = event.block.level.name
        if (!landWorldCorresponding.containsKey(level)) return
        val landWorld = landWorlds[landWorldCorresponding[level]!!]!!
        if (!LandUtils.isLandBlock(event.block, landWorld.type)) return
        val x1 = event.block.x.toInt()
        val z1 = event.block.z.toInt()
        val id = "$x1-$z1"
        if (!lands.containsKey("$id-$level")) {
            event.player.sendMessage("§c操作失败:领地不存在!")
            return
        }
        val land = lands["$id-$level"]!!
        if (land.owner != event.player.name) {
            event.player.sendMessage("§c操作失败:你不是领地主人!")
            return
        }
        land.sell(event.player)
    }
}