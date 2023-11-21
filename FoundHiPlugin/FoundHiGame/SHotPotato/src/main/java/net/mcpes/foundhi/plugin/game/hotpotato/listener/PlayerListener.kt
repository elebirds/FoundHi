package net.mcpes.foundhi.plugin.game.hotpotato.listener

import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.block.BlockBreakEvent
import cn.nukkit.event.block.BlockPlaceEvent
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityLevelChangeEvent
import cn.nukkit.event.player.*

class PlayerListener : Listener {
    @EventHandler
    fun onTouch(event: PlayerInteractEvent) {

    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {

    }

    @EventHandler
    fun onHungerChange(event: PlayerFoodLevelChangeEvent) {

    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {

    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {

    }

    @EventHandler
    fun onDropItem(event: PlayerDropItemEvent) {

    }


    @EventHandler
    fun onEat(event: PlayerItemConsumeEvent) {

    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        if (event is EntityDamageByEntityEvent) {

        }
    }

    @EventHandler
    fun onModeChange(event: PlayerGameModeChangeEvent) {

    }

    @EventHandler
    fun onHeld(event: PlayerItemHeldEvent) {

    }

    @EventHandler
    fun onChat(event: PlayerChatEvent) {

    }

    @EventHandler
    fun onCommand(event: PlayerCommandPreprocessEvent) {

    }

    @EventHandler
    fun onLevelChange(event: EntityLevelChangeEvent) {

    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {

    }
}