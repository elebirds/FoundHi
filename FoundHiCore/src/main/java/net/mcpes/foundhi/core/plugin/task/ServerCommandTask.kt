package net.mcpes.foundhi.core.plugin.task

import cn.nukkit.command.ConsoleCommandSender
import cn.nukkit.scheduler.PluginTask
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import java.util.concurrent.CopyOnWriteArrayList

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/23
 * version 1.0
 */
class ServerCommandTask : PluginTask<FoundHiPlugin>(FoundHiPlugin.instance) {
    val sender: ConsoleCommandSender = ConsoleCommandSender()
    override fun onRun(p0: Int) {
        commands.forEach {
            FoundHiPlugin.instance.server.dispatchCommand(sender, it)
            commands.remove(it)
        }
    }

    companion object {
        @JvmStatic
        val commands: CopyOnWriteArrayList<String> = CopyOnWriteArrayList()
    }
}