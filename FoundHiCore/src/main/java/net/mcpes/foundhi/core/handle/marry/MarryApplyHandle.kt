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
class MarryApplyHandle(val from: String, val to: String) : HandleClass() {
    override fun handle() {
        val cause = Server.getInstance().getPlayerExact(this.from)
        if (cause == null || !cause.isOnline) return
        when (result) {
            HandleResults.NORMAL_SUCCESS -> {
                cause.sendMessage("§e成功申请与 $to 成为情侣，请静待佳音")
            }
            HandleResults.MARRY_SUCCESS -> {
                cause.sendMessage("§e恭喜您和 $to 缔结契约，成为了情侣")
            }
            HandleResults.PARAMETER_ERROR -> {
                cause.sendMessage("§a§l操作失败:参数错误!")
            }
            HandleResults.THE_OTHER_OFFLINE -> {
                cause.sendMessage("§a§l操作失败:对方不在线!")
            }
            HandleResults.ALREADY_HAVE_LOVER -> {
                cause.sendMessage("§a§l操作失败:你已和别人成为情侣")
            }
            HandleResults.THE_OTHER_HAVE_LOVER -> {
                cause.sendMessage("§a§l操作失败:Ta已经和别人成为情侣")
            }
            HandleResults.ALREADY_APPLICATION -> {
                cause.sendMessage("§a§l操作失败:你已经申请过了,请静待佳音")
            }
        }
    }
}
