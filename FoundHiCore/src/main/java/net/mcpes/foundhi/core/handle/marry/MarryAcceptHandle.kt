package net.mcpes.foundhi.core.handle.marry;

import cn.nukkit.Server
import net.mcpes.foundhi.core.handle.HandleClass
import net.mcpes.foundhi.core.handle.HandleResults

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/1
 * version 1.0
 */
class MarryAcceptHandle(val from: String, val to: String) : HandleClass() {
    override fun handle() {
        val cause = Server.getInstance().getPlayerExact(this.from)
        if (cause == null || !cause.isOnline) return
        when (result) {
            HandleResults.NORMAL_SUCCESS -> cause.sendMessage("§e恭喜您和 $to 缔结契约，成为了情侣")
            HandleResults.PARAMETER_ERROR -> cause.sendMessage("§a§l操作失败:参数错误!")
            HandleResults.UNKNOW_ERROR -> cause.sendMessage("§a§l操作失败:未知错误!")
            HandleResults.ALREADY_HAVE_LOVER -> cause.sendMessage("§a§l操作失败:你已和别人成为情侣")
            HandleResults.THE_OTHER_HAVE_LOVER -> cause.sendMessage("§a§l操作失败:Ta已经和别人成为情侣")
            HandleResults.NO_APPLICATION -> cause.sendMessage("§a§l操作失败:Ta并没有申请")
        }
    }
}
