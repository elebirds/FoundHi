package net.mcpes.foundhi.core.plugin.gui.listener

import cn.nukkit.Server
import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerQuitEvent
import cn.nukkit.event.server.DataPacketReceiveEvent
import cn.nukkit.network.protocol.ModalFormResponsePacket
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.core.plugin.gui.NukkitGraphicalUserInterface
import net.mcpes.foundhi.core.plugin.gui.NukkitGraphicalUserInterface.Companion.GUIs
import net.mcpes.foundhi.core.plugin.gui.NukkitGraphicalUserInterface.Companion.playerInGUI
import net.mcpes.foundhi.core.plugin.gui.event.PlayerGUIResultEvent
import net.mcpes.foundhi.core.plugin.gui.window.EventGUI
import net.mcpes.foundhi.core.plugin.gui.window.LambdaGUI

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/11
 * version 1.0
 *
 * GUI NK事件监听，实现窗口回调
 */
class GUIListener : Listener {
    private val lastWindows: MutableMap<String, NukkitGraphicalUserInterface> = hashMapOf()

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun onQuit(event: PlayerQuitEvent) {
        lastWindows.remove(event.player.name)
        playerInGUI.remove(event.player.name)
    }

    @EventHandler
    fun onResult(event: DataPacketReceiveEvent) {
        event.player as? NukkitPlayer ?: return
        if (event.packet !is ModalFormResponsePacket) return
        val packet = event.packet as ModalFormResponsePacket
        if (!playerInGUI.containsKey(event.player.name)) return
        event.isCancelled = true
        val id = playerInGUI[event.player.name]!!
        playerInGUI.remove(event.player.name)
        if (!GUIs.containsKey(id)) {
            return
        }
        val gui = GUIs[id]!!
        val parent = lastWindows.remove(event.player.name)
        if (parent != null) {
            gui.setParent(event.player as NukkitPlayer, parent)
        }
        lastWindows[event.player.name] = gui
        when (gui) {
            is EventGUI -> {
                if (packet.data == "null") {
                    Server.getInstance().pluginManager.callEvent(PlayerGUIResultEvent(event.player as NukkitPlayer, gui, true))
                    return
                }
                val data: HashMap<String, Any> = gui.analysis(packet.data.trim())
                Server.getInstance().pluginManager.callEvent(PlayerGUIResultEvent(event.player as NukkitPlayer, gui, false, data))
            }
            is LambdaGUI -> {
                if (packet.data == "null") {
                    gui.callClosed(event.player)
                    return
                }
                gui.callClicked(event.player, packet.data)
            }
        }
        (event.player as NukkitPlayer).guiParams.remove(gui.id)
        event.isCancelled = true
    }
}