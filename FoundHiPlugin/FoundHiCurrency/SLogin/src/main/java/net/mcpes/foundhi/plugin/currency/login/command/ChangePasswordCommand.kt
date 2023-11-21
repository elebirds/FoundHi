package net.mcpes.foundhi.plugin.currency.login.command

import cn.nukkit.Player
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import net.mcpes.foundhi.plugin.currency.login.SLogin
import net.mcpes.foundhi.plugin.currency.login.SLogin.Companion.TITLE

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/26
 * version 1.0
 */
class ChangePasswordCommand(name: String) : Command(name) {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("$TITLE 你不是一个玩家!")
            return false
        }
        if (strings.size != 2) {
            sender.sendMessage("$TITLE 输入错误!请输入/$name <原密码> <新密码>")
            return true
        }
        val data = SLogin.instance.pData[sender.getName()]
        if (data == null) {
            sender.sendMessage("$TITLE 数据加载错误!")
            return true
        }
        if (data.password == strings[0]) {
            SLogin.instance.pData[sender.getName()]!!.password = strings[1]
            SLogin.instance.pData[sender.getName()]!!.save(true)
            sender.sendMessage("$TITLE 修改成功!")
            return true
        } else {
            sender.sendMessage("$TITLE 原密码错误!")
        }
        return true
    }
}
