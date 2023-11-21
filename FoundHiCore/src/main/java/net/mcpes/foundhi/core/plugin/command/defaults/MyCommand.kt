package net.mcpes.foundhi.core.plugin.command.defaults

import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.command.ConsoleCommandSender
import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.command.FoundHiPluginCommandFunction

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/17
 * version 1.0
 */
class MyCommand(name: String) : Command(name), FoundHiPluginCommandFunction {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (this.checkPull(sender)) {
            sender.sendMessage("§c数据拉取失败!")
            return true
        }
        if (sender is ConsoleCommandSender || !this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.PLAYER)) {
            sender.sendMessage("§c功能被禁用!")
            return true
        }
        sender.sendMessage("§6====§l§bS§au§dm§em§4i§6t §8Server 我的信息§6====\n§8 我的ID: " + sender.name + "§a 我的权限:" + FoundHiPlugin.instance.getPlayer(sender.name)!!.permission.permissionName)
        return false
    }
}
