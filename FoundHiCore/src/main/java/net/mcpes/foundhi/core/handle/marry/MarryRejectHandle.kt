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
class MarryRejectHandle(val from: String, val to: String) : HandleClass() {
    override fun handle() {
        val cause = Server.getInstance().getPlayerExact(this.from)
        if (cause == null || !cause.isOnline) return
        when (result) {
            HandleResults.NORMAL_SUCCESS -> cause.sendMessage("§e成功拒绝 $to 的申请")
            HandleResults.PARAMETER_ERROR -> cause.sendMessage("§a§l操作失败:参数错误!")
            HandleResults.UNKNOW_ERROR -> cause.sendMessage("§a§l操作失败:未知错误!")
            HandleResults.THE_OTHER_NO_APPLICATION -> cause.sendMessage("§a§l操作失败:Ta并没有申请")
        }
    }
}