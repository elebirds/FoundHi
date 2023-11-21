package net.mcpes.foundhi.core.plugin.command.defaults

import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.command.FoundHiPluginCommandFunction
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.core.utils.PrefixUtils

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/28
 * version 1.0
 */
class PrefixCommand(name: String) : Command(name, "称号系统", "", arrayOf("p", "称号")), FoundHiPluginCommandFunction {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.isEmpty()) {
            sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
            return true
        }
        when (strings[0]) {
            "add", "a", "添加" -> {

            }
            "del", "d", "删除" -> {

            }
            "list", "l", "列表" -> {
                if (strings.size !in 1..2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (sender !is NukkitPlayer || !this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.PLAYER)) {
                    sender.sendMessage("§c功能被禁用!")
                    return true
                }
                val player = FoundHiPlugin.instance.getPlayer(sender.name)!!
                sender.sendMessage("§6====§l§bS§au§dm§em§4i§6t §8Server 我的称号§6====")
                player.title.forEach {
                    sender.sendMessage("§a - $it")
                }
                sender.sendMessage("§6共${player.title.size}个称号")
            }
            "choose", "c", "选择", "佩戴" -> {
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (sender !is NukkitPlayer || !this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.PLAYER)) {
                    sender.sendMessage("§c功能被禁用!")
                    return true
                }
                try {
                    val chose = strings[1].toInt()
                    val player = FoundHiPlugin.instance.getPlayer(sender.name)!!
                    if (chose < 0 || (player.title.size - 1) > chose) {
                        sender.sendMessage("§e你没有这个称号!")
                        return true
                    }
                    PrefixUtils.choose(sender.name, chose)
                    sender.sendMessage("§b正在处理中...")
                } catch (e: NumberFormatException) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
            }
        }
        return false
    }
}
