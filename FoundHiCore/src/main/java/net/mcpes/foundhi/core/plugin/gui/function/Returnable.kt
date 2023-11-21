package net.mcpes.foundhi.core.plugin.gui.function

import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.core.plugin.gui.NukkitGraphicalUserInterface

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/8/14
 * version 1.0
 */
interface Returnable {
    fun setParent(player: NukkitPlayer, gui: NukkitGraphicalUserInterface)

    fun getParent(player: NukkitPlayer): NukkitGraphicalUserInterface??

    fun goBack(player: NukkitPlayer?): Boolean {
        player ?: return false
        val parent = this.getParent(player)
        parent ?: return false
        player.showGUI(parent)
        return true
    }
}