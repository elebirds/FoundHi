package net.mcpes.foundhipro.core.app.command.defaults

import net.mcpes.foundhipro.core.app.CoreServer
import net.mcpes.foundhipro.core.app.command.Command
import net.mcpes.foundhipro.core.app.command.CommandSender

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/26
 * version 1.0
 */
class StopCommand : Command("stop", "停止FHPCApplication运行") {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        CoreServer.instance.isRunning.set(false)
        sender.sendMessage("§c成功关闭FHPCore Application!")
        return true
    }
}