package net.mcpes.foundhi.core.handle.money

import cn.nukkit.Server
import net.mcpes.foundhi.core.handle.HandleClass
import net.mcpes.foundhi.core.handle.HandleResults
import net.mcpes.foundhi.core.utils.getMoneyName
import net.mcpes.foundhi.core.utils.moneyFormat

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/13
 * version 1.0
 */
class MoneySetHandle(private val cause: String, private val target: String, private val count: Double, private val type: Int) : HandleClass() {
    override fun handle() {
        val cause = Server.getInstance().getPlayerExact(this.cause)
        val target = Server.getInstance().getPlayerExact(this.target)
        when (this.result) {
            HandleResults.NORMAL_SUCCESS -> {
                if (cause != null && cause.isOnline) {
                    cause.sendMessage("§a§l设置" + target + "的" + type.getMoneyName() + "为" + count.moneyFormat() + "枚成功!")
                }
                if (target != null && target.isOnline) {
                    target.sendMessage("§l§8┃§bS§au§dm§em§4i§6t§8┃§8 - NOTICE: " + this.cause + "§a§l设置你的" + type.getMoneyName() + "为" + count.moneyFormat() + "枚")
                }
            }
            HandleResults.THE_OTHER_OFFLINE -> if (cause != null && cause.isOnline) {
                cause.sendMessage("§a§l操作失败:玩家:" + this.target + "不在线!")
            }
            HandleResults.PARAMETER_ERROR -> if (cause != null && cause.isOnline) {
                cause.sendMessage("§a§l操作失败:未知参数错误!")
            }
        }
    }
}
