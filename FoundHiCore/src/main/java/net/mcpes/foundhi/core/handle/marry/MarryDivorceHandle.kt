package net.mcpes.foundhi.core.handle.marry

import cn.nukkit.Server
import net.mcpes.foundhi.core.handle.HandleClass
import net.mcpes.foundhi.core.handle.HandleResults

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/1
 * version 1.0
 */
class MarryDivorceHandle(val from: String) : HandleClass() {
    override fun handle() {
        val cause = Server.getInstance().getPlayerExact(this.from)
        if (cause == null || !cause.isOnline) return
        when (result) {
            HandleResults.NORMAL_SUCCESS -> cause.sendMessage("成功离婚")
            -1, -2 -> cause.sendMessage("§a§l操作失败:参数错误")
            -3 -> cause.sendMessage("§a§l操作失败:对方不在线")
            -4 -> cause.sendMessage("§a§l操作失败:你没有结婚")
        }
    }
}