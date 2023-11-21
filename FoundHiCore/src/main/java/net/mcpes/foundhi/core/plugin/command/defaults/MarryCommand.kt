package net.mcpes.foundhi.core.plugin.command.defaults

import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import net.mcpes.foundhi.core.plugin.command.FoundHiPluginCommandFunction
import net.mcpes.foundhi.core.utils.MarryUtils

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/1
 * version 1.0
 */
class MarryCommand(name: String) : Command(name, "情侣系统", "", arrayOf("结婚", "情侣", "l", "lover")), FoundHiPluginCommandFunction {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (!sender.isPlayer) {
            sender.sendMessage("§c控制台禁用此命令!")
            return true
        }
        if (strings.isEmpty()) {
            sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
            return true
        }
        when (strings[0]) {
            "help" -> {
            }
            "marry", "m", "结婚" -> {
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                when (MarryUtils.marry(sender.name, strings[1])) {
                    0 -> sender.sendMessage("§3正在处理")
                    -4 -> sender.sendMessage("§a§l操作失败:你已和别人成为情侣")
                }
            }
            "divorce", "d", "离婚" -> {
                MarryUtils.divorce(sender.name)
                sender.sendMessage("§3正在处理")
            }
            "accept", "a", "接受" -> {
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                MarryUtils.accept(sender.name, strings[1])
                sender.sendMessage("§3正在处理")
            }
            "reject", "r", "拒绝" -> {
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                MarryUtils.reject(sender.name, strings[1])
                sender.sendMessage("§3正在处理")
            }
        }
        return true
    }
}
