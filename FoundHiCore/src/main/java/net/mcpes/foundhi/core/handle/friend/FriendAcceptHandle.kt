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
class FriendAcceptHandle(val from: String, val to: String) : HandleClass() {
    override fun handle() {
        val cause = Server.getInstance().getPlayerExact(this.from)
        if (cause == null || !cause.isOnline) return
        when (result) {
            HandleResults.NORMAL_SUCCESS -> {
                cause.sendMessage("§e成功添加 $to 为好友")
            }
            -1 -> {
                cause.sendMessage("§a§l操作失败:参数错误!")
            }
            -3 -> {
                cause.sendMessage("§a§l操作失败:你已和他是好友了!")
            }
            -2 -> {
                cause.sendMessage("§a§l操作失败:API端未知错误,请重新登陆")
            }
            -4 -> {
                cause.sendMessage("§c操作失败:目标玩家不存在")
            }
        }
    }
}