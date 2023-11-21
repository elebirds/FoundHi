package net.mcpes.foundhi.plugin.currency.npc.listener

import cn.nukkit.Player
import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.entity.EntityLevelChangeEvent
import cn.nukkit.event.player.PlayerJoinEvent
import cn.nukkit.event.player.PlayerMoveEvent
import cn.nukkit.event.server.DataPacketReceiveEvent
import cn.nukkit.inventory.transaction.data.UseItemOnEntityData
import cn.nukkit.network.protocol.InventoryTransactionPacket
import net.mcpes.foundhi.plugin.currency.npc.SNonPlayerCharacter.Companion.npcs
import net.mcpes.foundhi.plugin.currency.npc.npc.static.StaticNonPlayerCharacter

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/9
 * version 1.0
 */
class StaticNPCListener : Listener {
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun onTouch(event: DataPacketReceiveEvent) {
        if (event.packet is InventoryTransactionPacket) {
            var eid: Long = 0
            if ((event.packet as InventoryTransactionPacket).transactionData is UseItemOnEntityData) {
                eid = ((event.packet as InventoryTransactionPacket).transactionData as UseItemOnEntityData).entityRuntimeId
            }
            var id = ""
            var flag = false
            for (npc in npcs.values) {
                if (npc !is StaticNonPlayerCharacter) return
                if (eid == npc.getEntity().eid) {
                    flag = true
                    id = npc.id
                }
            }
            if (flag) {
                val n = npcs[id]
                if (n != null && n is StaticNonPlayerCharacter) {
                    n.onTouch(event.player)
                }
                event.isCancelled = true
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerMove(event: PlayerMoveEvent) {
        for (npc in npcs.values) {
            if (npc !is StaticNonPlayerCharacter) return
            if (npc.level == event.player.getLevel().name) {
                npc.getEntity().look(event.player)
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        for (npc in npcs.values) {
            if (npc !is StaticNonPlayerCharacter) return
            if (npc.level == event.player.getLevel().name) {
                npc.getEntity().spawnTo(event.player)
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onEntityLevelChange(event: EntityLevelChangeEvent) {
        if (event.entity !is Player) return
        for (npc in npcs.values) {
            if (npc !is StaticNonPlayerCharacter) return
            if (npc.level == event.origin.name) {
                npc.getEntity().despawnTo(event.entity as Player)
            }
            if (npc.level == event.target.name) {
                npc.getEntity().spawnTo(event.entity as Player)
            }
        }
    }
}