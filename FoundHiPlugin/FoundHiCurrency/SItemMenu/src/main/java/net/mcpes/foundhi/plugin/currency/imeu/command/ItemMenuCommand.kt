package net.mcpes.foundhi.plugin.currency.imeu.command

import cn.nukkit.Player
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import net.mcpes.foundhi.core.plugin.command.FoundHiPluginCommandFunction
import net.mcpes.foundhi.plugin.currency.imeu.SItemMenu

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/13
 * version 1.0
 */
class ItemMenuCommand(name: String) : Command(name), FoundHiPluginCommandFunction {
    init {
        this.commandParameters.clear()
        this.commandParameters["default"] = arrayOf(CommandParameter("菜单名称", CommandParamType.TARGET, false))
    }

    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§c控制台禁止运行!")
            return false
        }
        if (strings.size != 1) {
            sender.sendMessage("§c输入格式错误!")
            return false
        }
        if (!SItemMenu.menus.containsKey(strings[0])) {
            sender.sendMessage("§c没有这个菜单")
            return true
        }
        SItemMenu.menus[strings[0]]!!.open(sender, true, true)
        return false
    }
}