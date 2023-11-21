package net.mcpes.foundhi.core.app.command.defaults

import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.command.Command
import net.mcpes.foundhi.core.app.command.CommandSender

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/27
 * version 1.0
 */
class StopCommand(name: String) : Command(name, "关闭API服务器") {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        APIFunction.instance.shutdown()
        return true
    }
}