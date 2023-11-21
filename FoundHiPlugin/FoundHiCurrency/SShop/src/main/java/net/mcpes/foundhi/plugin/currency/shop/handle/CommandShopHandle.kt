package net.mcpes.foundhi.plugin.currency.shop.handle

import cn.nukkit.Player
import cn.nukkit.Server
import net.mcpes.foundhi.core.handle.Handle
import net.mcpes.foundhi.plugin.currency.shop.shop.system.CommandShop
import net.mcpes.foundhi.plugin.currency.shop.utils.Utils

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/3
 * version 1.0
 */
class CommandShopHandle(private val target: String, private val shop: CommandShop) : Handle() {
    override fun handle() {
        val target = Server.getInstance().getPlayerExact(this.target)
        when (this.result) {
            0 -> if (target != null && target.isOnline) {
                dispatchCommands(target)
                target.sendMessage(Utils.getMessage("成功花费" + shop.moneyMessage + "购买命令:" + shop.name))
            }
            -1 -> if (target != null && target.isOnline) target.sendMessage("§a§l操作失败:出现未知错误,请重新进入服务器再次尝试!")
            -2 -> if (target != null && target.isOnline) target.sendMessage("§a§l操作失败:未知参数错误!")
            -3 -> if (target != null && target.isOnline) target.sendMessage("§a§l操作失败:所需货币不足!")
        }
    }

    private fun dispatchCommands(target: Player) {
        shop.commands.forEach {
            when (shop.mode) {
                CommandShop.PLAYER_COMMAND -> Server.getInstance().dispatchCommand(target, it)
                CommandShop.CONSOLE_COMMAND -> Server.getInstance().dispatchCommand(Server.getInstance().consoleSender, it)
                CommandShop.OP_COMMAND -> Server.getInstance().dispatchCommand(target, it)//ToDo
            }
        }
    }
}