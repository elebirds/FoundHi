package net.mcpes.foundhi.plugin.survival.land.listener

import cn.nukkit.Player
import cn.nukkit.block.Block
import cn.nukkit.entity.mob.EntityCreeper
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.block.BlockBreakEvent
import cn.nukkit.event.block.BlockPlaceEvent
import cn.nukkit.event.entity.EntityExplosionPrimeEvent
import cn.nukkit.event.entity.ExplosionPrimeEvent
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.command.FoundHiPluginCommandFunction
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.landWorldCorresponding
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.landWorlds
import net.mcpes.foundhi.plugin.survival.land.land.Land
import net.mcpes.foundhi.plugin.survival.land.utils.LandUtils

/**
 * SLand
 *
 * @author hhm Copyright (c) 2018/5/4
 * version 1.0
 */
class PlayerListener : Listener, FoundHiPluginCommandFunction {
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (this.checkBuild(event.block, event.player)) {
            event.isCancelled = true
            event.player.sendMessage("§c你没有权限破坏别人的领地")
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (this.checkBuild(event.block, event.player)) {
            event.isCancelled = true
            event.player.sendMessage("§c你没有权限破坏别人的领地")
        }
    }

    @EventHandler
    fun onBoom(event: EntityExplosionPrimeEvent) {
        val level = event.entity.level.name
        if (!landWorldCorresponding.containsKey(level)) return
        event.isBlockBreaking = false
    }

    @EventHandler
    fun onBoom2(event: ExplosionPrimeEvent) {
        val level = event.entity.level.name
        if (!landWorldCorresponding.containsKey(level)) return
        if (event.entity !is EntityCreeper) return
        event.isBlockBreaking = false
    }

    private fun checkBuild(block: Block, player: Player): Boolean {
        val level = block.level.name
        if (!landWorldCorresponding.containsKey(level)) {
            return false
        }
        val name = player.name
        val lw = landWorlds[landWorldCorresponding[level]!!]!!
        if (!LandUtils.isLandBlock(block, lw.type)) {
            if (!FoundHiPlugin.instance.isPlayerOnline(name)) {
                return true
            }
            if (!this.checkPermission(FoundHiPlugin.instance.getPlayer(name)!!, net.mcpes.foundhi.core.app.data.player.Player.Permission.HIGH_ADMIN)) {
                if (!LandUtils.isOwner(name, block.location)) {
                    val land = LandUtils.isGuest(name, block.location) ?: return true
                    if (!land.permission[Land.Companion.PlayerType.GUEST]!!["build"]!!) {
                        return true
                    }
                } else return false
            } else return false
        }
        return true
    }
}