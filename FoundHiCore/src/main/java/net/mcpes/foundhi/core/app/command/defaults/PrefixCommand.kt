package net.mcpes.foundhi.core.app.command.defaults

import net.mcpes.foundhi.core.app.command.Command
import net.mcpes.foundhi.core.app.command.CommandSender

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/28
 * version 1.0
 */
class PrefixCommand(name: String) : Command(name, "称号系统") {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        return true
    }
}