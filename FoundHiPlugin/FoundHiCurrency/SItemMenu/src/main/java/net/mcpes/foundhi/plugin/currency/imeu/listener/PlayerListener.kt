package net.mcpes.foundhi.plugin.currency.imeu.listener

import cn.nukkit.Player
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.entity.EntityLevelChangeEvent
import cn.nukkit.event.inventory.InventoryTransactionEvent
import cn.nukkit.event.player.PlayerDropItemEvent
import cn.nukkit.event.player.PlayerItemHeldEvent
import cn.nukkit.event.player.PlayerJoinEvent
import cn.nukkit.event.player.PlayerQuitEvent
import cn.nukkit.inventory.ChestInventory
import cn.nukkit.inventory.PlayerInventory
import cn.nukkit.inventory.transaction.action.SlotChangeAction
import cn.nukkit.item.Item
import net.mcpes.foundhi.plugin.currency.imeu.SItemMenu
import net.mcpes.foundhi.plugin.currency.imeu.SItemMenu.Companion.`in`
import net.mcpes.foundhi.plugin.currency.imeu.SItemMenu.Companion.menus
import net.mcpes.foundhi.plugin.currency.imeu.SItemMenu.Companion.touch
import net.mcpes.foundhi.plugin.currency.imeu.SItemMenu.Companion.world
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/13
 * version 1.0
 */
class PlayerListener : Listener {
    @EventHandler
    fun onPY(event: PlayerItemHeldEvent) {
        if (!SItemMenu.`in`.containsKey(event.player.name)) return
        event.isCancelled = SItemMenu.menus[SItemMenu.`in`[event.player.name]]!!.handle(event.player, event.slot)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        removeMenuItem(event.player)
        touch.remove(event.player.name)
        `in`.remove(event.player.name)
        if (SItemMenu.playerItems.containsKey(event.player.name)) {
            event.player.inventory.contents = SItemMenu.playerItems[event.player.name]
        }
        SItemMenu.playerItems.remove(event.player.name)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        removeMenuItem(event.player)
        val newWorld = event.player.level.name
        if (world.containsKey(newWorld)) {
            if (menus.containsKey(world[newWorld])) {
                menus[world[newWorld]]!!.open(event.player, true, true)
            }
        }
    }

    @EventHandler
    fun onLevelChange(event: EntityLevelChangeEvent) {
        if (event.entity !is Player) return
        if (world.containsKey(event.origin.name)) {
            removeMenuItem(event.entity as Player)
        }
        val newWorld = event.target.name
        if (world.containsKey(newWorld)) {
            if (menus.containsKey(world[newWorld])) {
                menus[world[newWorld]]!!.open(event.entity as Player, true, true)
            }
        }
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        val item = event.item
        if (item.hasCustomBlockData() && item.customBlockData != null && item.customBlockData.contains("menu") && item.customBlockData.getBoolean("menu")) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onChest(event: InventoryTransactionEvent) {
        for (action in event.transaction.actions) {
            if (action is SlotChangeAction) {
                if (action.inventory is ChestInventory) {
                    if (`in`.containsKey(event.transaction.source.name)) {
                        val item = action.getTargetItem()
                        if (item.hasCustomBlockData() && item.customBlockData != null && item.customBlockData.contains("menu") && item.customBlockData.getBoolean("menu")) {
                            event.isCancelled = true
                        }
                    }
                } else if (action.inventory is PlayerInventory) {
                    if (`in`.containsKey(event.transaction.source.name)) {
                        val item1 = action.getSourceItem()
                        val item2 = action.getTargetItem()
                        if (item1.hasCustomBlockData() && item1.customBlockData != null && item1.customBlockData.contains("menu") && item1.customBlockData.getBoolean("menu")) {
                            event.isCancelled = true
                        } else if (item2.hasCustomBlockData() && item2.customBlockData != null && item2.customBlockData.contains("menu") && item2.customBlockData.getBoolean("menu")) {
                            event.isCancelled = true
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun removeMenuItem(player: Player) {
            val items = ArrayList<Item>()
            for (item in player.inventory.contents.values) {
                if (item.hasCustomBlockData() && item.customBlockData != null && item.customBlockData.contains("menu") && item.customBlockData.getBoolean("menu")) {
                    items.add(item)
                }
            }
            player.inventory.removeItem(*items.toTypedArray())
        }
    }
}
