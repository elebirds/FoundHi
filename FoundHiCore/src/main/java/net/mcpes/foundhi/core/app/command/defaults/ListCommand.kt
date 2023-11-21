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
class ListCommand(name: String) : Command(name, "查看玩家数量") {

    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.size != 1) {
            sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
            return false
        }
        when (strings[0]) {
            "server", "s" -> {
                var os = StringBuilder()
                for (server in APIFunction.instance.getServers()) {
                    os.append(server.name).append("(id:").append(server.id).append(")").append(", ")
                }

                if (os.isNotEmpty()) {
                    os = StringBuilder(os.substring(0, os.length - 2))
                }
                sender.sendMessage("目前有 " + APIFunction.instance.getServerCount() + " 个服务器连接到API服务器：")
                sender.sendMessage(os.toString())
            }
            "player", "p" -> {
                var op = StringBuilder()
                for (player in APIFunction.instance.getPlayers()) {
                    op.append(player.name).append(", ")
                }
                if (op.isNotEmpty()) {
                    op = StringBuilder(op.substring(0, op.length - 2))
                }
                sender.sendMessage("目前有 " + APIFunction.instance.getPlayerCount() + " 个玩家在线：")
                sender.sendMessage(op.toString())
            }
            else -> sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
        }
        return true
    }
}
