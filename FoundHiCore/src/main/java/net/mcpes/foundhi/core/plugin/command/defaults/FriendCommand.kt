package net.mcpes.foundhi.core.plugin.command.defaults

import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.command.ConsoleCommandSender
import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.command.FoundHiPluginCommandFunction
import net.mcpes.foundhi.core.utils.FriendUtils

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/8
 * version 1.0
 */
class FriendCommand(name: String) : Command(name, "好友系统", "", arrayOf("f", "好友")), FoundHiPluginCommandFunction {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.isEmpty()) {
            sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
            return true
        }
        when (strings[0]) {
            "help" -> {
                if (sender is ConsoleCommandSender || (!this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.PLAYER))) {
                    sender.sendMessage("§c功能被禁用!")
                }
                sender.sendMessage("§6====§l§bS§au§dm§em§4i§6t §8好友·帮助§6====")
                sender.sendMessage("§a - /$s {add/添加/a} <名称> 添加好友")
                sender.sendMessage("§a - /$s {delete/删除/d} <名称> 删除好友")
                sender.sendMessage("§a - /$s {accept/同意} <名称> 同意好友申请")
                sender.sendMessage("§a - /$s {refuse/拒绝/r} <名称> 拒绝好友申请")
            }
            "add", "添加", "a" -> {
                //f a <name>
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (sender is ConsoleCommandSender || (!this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.PLAYER))) {
                    sender.sendMessage("§c功能被禁用!")
                }
                when (FriendUtils.apply(sender.name, strings[1])) {
                    0 -> sender.sendMessage("§3正在处理中...")
                    -1 -> sender.sendMessage("§c操作失败:你已经和他是好友了。")
                }
            }
            "delete", "删除", "d" -> {
                //f d <name>
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (sender is ConsoleCommandSender || (!this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.PLAYER))) {
                    sender.sendMessage("§c功能被禁用!")
                }
                when (FriendUtils.delete(sender.name, strings[1])) {
                    0 -> sender.sendMessage("§3正在处理中...")
                    -1 -> sender.sendMessage("§c操作失败:你和他不是好友。")
                }
            }
            "accept", "同意" -> {
                //f accept <name>
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (sender is ConsoleCommandSender || (!this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.PLAYER))) {
                    sender.sendMessage("§c功能被禁用!")
                }
                when (FriendUtils.accept(sender.name, strings[1])) {
                    0 -> sender.sendMessage("§3正在处理中...")
                    -1 -> sender.sendMessage("§c操作失败:你已经和他是好友了。")
                }
            }
            "refuse", "拒绝", "r" -> {
                //f r <name>
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (sender is ConsoleCommandSender || (!this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.PLAYER))) {
                    sender.sendMessage("§c功能被禁用!")
                }
                FriendUtils.refuse(sender.name, strings[1])
                sender.sendMessage("§3正在处理中...")
            }
        }
        return true
    }
}
