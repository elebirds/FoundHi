package net.mcpes.foundhi.core.app.command.defaults

import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.command.Command
import net.mcpes.foundhi.core.app.command.CommandSender
import net.mcpes.foundhi.core.utils.TextFormat

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/27
 * version 1.0
 */
class HelpCommand(name: String) : Command(name, "查看帮助") {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        sender.sendMessage(TextFormat.AQUA.toString() + "-------Help-------")
        APIFunction.instance.commandMap.commands.forEach { k, v -> sender.sendMessage(TextFormat.YELLOW.toString() + "/" + k + " - " + v.dec) }
        sender.sendMessage(TextFormat.AQUA.toString() + "-------Help-------")
        return true
    }
}