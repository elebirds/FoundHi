package net.mcpes.foundhi.plugin.currency.login.command

import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.plugin.currency.login.SLogin
import net.mcpes.foundhi.plugin.currency.login.SLogin.Companion.TITLE
import net.mcpes.foundhi.plugin.currency.login.utils.LoginUtils

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/26
 * version 1.0
 */
class RegisterCommand(name: String) : Command(name) {

    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (sender !is NukkitPlayer) {
            sender.sendMessage("$TITLE 你不是一个玩家!")
            return false
        }
        if (strings.size != 1) {
            val data = SLogin.instance.pData[sender.name]
            if (SLogin.instance.notLogin.contains(sender.name) && data != null && !data.exists()) {
                SLogin.instance.openRegister(sender)
            }
            return true
        }
        when (LoginUtils.register(sender, strings[0], false, false)) {
            0 -> sender.sendMessage("$TITLE 注册成功,稍后将把您转移到大厅服务器!")
            1 -> sender.sendMessage("$TITLE 你已经注册了!")
            -1 -> sender.sendMessage("$TITLE 发生未知错误!")
        }
        return true
    }
}
