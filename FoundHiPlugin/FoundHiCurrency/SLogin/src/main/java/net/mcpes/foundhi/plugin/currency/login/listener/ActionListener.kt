package net.mcpes.foundhi.plugin.currency.login.listener

import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.block.BlockBreakEvent
import cn.nukkit.event.block.BlockPlaceEvent
import cn.nukkit.event.player.*
import net.mcpes.foundhi.plugin.currency.login.SLogin
import net.mcpes.foundhi.plugin.currency.login.SLogin.Companion.TITLE

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/26
 * version 1.0
 */
class ActionListener : Listener {
    /*
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onMove(PlayerMoveEvent event) {
        if (!SLogin.Companion.getInstance().getLogged().contains(event.getPlayer().getName())) {
            event.getPlayer().sendMessage(TITLE + " §6你没有登陆,不能进行操作!");
            event.setCancelled(true);
        }
    }*/
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        if (!SLogin.instance.logged.contains(event.player.name)) {
            val command = event.message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            var flag = false
            for (cmd in SLogin.instance.registerCommands) {
                if ("/$cmd" == command) flag = true
            }
            for (cmd in SLogin.instance.loginCommands) {
                if ("/$cmd" == command) flag = true
            }
            for (cmd in SLogin.instance.changeCommands) {
                if ("/$cmd" == command) flag = true
            }
            if (!flag) {
                event.player.sendMessage("$TITLE §6你没有登陆,不能进行操作!")
                event.isCancelled = true
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onBreak(event: BlockBreakEvent) {
        if (!SLogin.instance.logged.contains(event.player.name)) {
            //event.getPlayer().sendMessage(TITLE + " §6你没有登陆,不能进行操作!");
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onPlace(event: BlockPlaceEvent) {
        if (!SLogin.instance.logged.contains(event.player.name)) {
            //event.getPlayer().sendMessage(TITLE + " §6你没有登陆,不能进行操作!");
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onInteract(event: PlayerInteractEvent) {
        if (!SLogin.instance.logged.contains(event.player.name)) {
            //event.getPlayer().sendMessage(TITLE + " §6你没有登陆,不能进行操作!");
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onChat(event: PlayerChatEvent) {
        if (!SLogin.instance.logged.contains(event.player.name)) {
            //event.getPlayer().sendMessage(TITLE + " §6你没有登陆,不能进行操作!");
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onDrop(event: PlayerDropItemEvent) {
        if (!SLogin.instance.logged.contains(event.player.name)) {
            //event.getPlayer().sendMessage(TITLE + " §6你没有登陆,不能进行操作!");
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onEatFood(event: PlayerEatFoodEvent) {
        if (!SLogin.instance.logged.contains(event.player.name)) {
            //event.getPlayer().sendMessage(TITLE + " §6你没有登陆,不能进行操作!");
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onFoodLevelChange(event: PlayerFoodLevelChangeEvent) {
        if (!SLogin.instance.logged.contains(event.player.name)) {
            //event.getPlayer().sendMessage(TITLE + " §6你没有登陆,不能进行操作!");
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onGameModeChange(event: PlayerGameModeChangeEvent) {
        if (!SLogin.instance.logged.contains(event.player.name)) {
            //event.getPlayer().sendMessage(TITLE + " §6你没有登陆,不能进行操作!");
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onBedEnter(event: PlayerBedEnterEvent) {
        if (!SLogin.instance.logged.contains(event.player.name)) {
            //event.getPlayer().sendMessage(TITLE + " §6你没有登陆,不能进行操作!");
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onBedLeave(event: PlayerBedLeaveEvent) {
        if (!SLogin.instance.logged.contains(event.player.name)) {
            //event.getPlayer().sendMessage(TITLE + " §6你没有登陆,不能进行操作!");
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onTeleport(event: PlayerTeleportEvent) {
        if (!SLogin.instance.logged.contains(event.player.name)) {
            //event.getPlayer().sendMessage(TITLE + " §6你没有登陆,不能进行操作!");
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onToggleFlight(event: PlayerToggleFlightEvent) {
        if (!SLogin.instance.logged.contains(event.player.name)) {
            //event.getPlayer().sendMessage(TITLE + " §6你没有登陆,不能进行操作!");
            event.isCancelled = true
        }
    }
}