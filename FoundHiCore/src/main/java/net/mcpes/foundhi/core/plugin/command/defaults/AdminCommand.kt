package net.mcpes.foundhi.core.plugin.command.defaults

import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.command.ConsoleCommandSender
import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.command.FoundHiPluginCommandFunction
import net.mcpes.foundhi.core.utils.PermissionUtils
import net.mcpes.foundhi.core.utils.getPlayerPermission

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/15
 * version 1.0
 */
class AdminCommand(name: String) : Command(name, "管理系统", "", arrayOf("管理")), FoundHiPluginCommandFunction {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.isEmpty()) {
            sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
            return true
        }
        when (strings[0]) {
            "permission" -> {
                if (strings.size != 3) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (sender !is ConsoleCommandSender && !this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.OWNER)) {
                    sender.sendMessage("§c功能被禁用!")
                }
                try {
                    PermissionUtils[sender.name, strings[1], Integer.parseInt(strings[2]).getPlayerPermission()] = true
                    sender.sendMessage("§3正在处理中...")
                } catch (e: NumberFormatException) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                }

            }
        }
        return false
    }
}
