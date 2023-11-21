package net.mcpes.foundhi.core.handle.friend

import cn.nukkit.Server
import net.mcpes.foundhi.core.handle.HandleClass
import net.mcpes.foundhi.core.handle.HandleResults

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/7
 * version 1.0
 */
class FriendDeleteHandle(val from: String, val to: String) : HandleClass() {
    override fun handle() {
        val cause = Server.getInstance().getPlayerExact(this.from)
        if (cause == null || !cause.isOnline) return
        when (result) {
            HandleResults.NORMAL_SUCCESS -> {
                cause.sendMessage("§e成功与 $to 解除了好友关系")
            }
            -1 -> {
                cause.sendMessage("§a§l操作失败:参数错误!")
            }
            -3 -> {
                cause.sendMessage("§a§l操作失败:你和他不是好友!")
            }
            -2 -> {
                cause.sendMessage("§a§l操作失败:API端未知错误,请重新登陆")
            }
        }
    }
}