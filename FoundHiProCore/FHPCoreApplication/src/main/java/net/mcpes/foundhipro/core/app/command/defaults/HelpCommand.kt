package net.mcpes.foundhipro.core.app.command.defaults

import net.mcpes.foundhipro.core.app.bootstrap.FoundHiProCoreBootstrap
import net.mcpes.foundhipro.core.app.command.Command
import net.mcpes.foundhipro.core.app.command.CommandSender
import net.mcpes.foundhipro.core.app.command.FHPCommandManager

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/10/28/星期日 14:56
 * version 1.0
 */
class HelpCommand : Command("help", "总帮助") {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        sender.sendMessage("${FoundHiProCoreBootstrap.TITLE}§b------Help-------")
        FHPCommandManager.commandMap.commands.forEach { k, v -> sender.sendMessage("§e/" + k + " - " + v.dec) }
        return true
    }
}