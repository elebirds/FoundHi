package net.mcpes.foundhi.core.app.command.defaults


import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.command.Command
import net.mcpes.foundhi.core.app.command.CommandSender

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/23
 * version 1.0
 */
class KickCommand(name: String) : Command(name, "踢出玩家/服务器") {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.size != 2) {
            sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
            return false
        }
        when (strings[0]) {
            "server", "s" -> {
                if (!APIFunction.instance.isServerOnline(strings[1])) {
                    sender.sendMessage("此服务器不存在!")
                }
                val server = APIFunction.instance.getServer(strings[1])!!
                server.shutdown(true)
                sender.sendMessage("成功踢出服务器: " + server.name + "!")
            }
            "player", "p" -> {
                if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                    sender.sendMessage("此玩家不在线!")
                }
                val player = APIFunction.instance.getPlayer(strings[1])!!
                player.kick("§c系统踢出")
                sender.sendMessage("成功踢出玩家: " + player.name + "!")
            }
            else -> sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
        }
        return true
    }
}