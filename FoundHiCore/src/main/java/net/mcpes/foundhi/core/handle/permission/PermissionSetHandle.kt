package net.mcpes.foundhi.core.handle.permission

import cn.nukkit.Server
import net.mcpes.foundhi.core.handle.HandleClass
import net.mcpes.foundhi.core.handle.HandleResults
import net.mcpes.foundhi.core.network.protocol.player.PlayerSendMessagePacket
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.utils.getPlayerPermission

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/13
 * version 1.0
 */
class PermissionSetHandle(private val cause: String, private val target: String, private val type: Int) : HandleClass() {
    override fun handle() {
        val cause = Server.getInstance().getPlayerExact(this.cause)
        val target = Server.getInstance().getPlayerExact(this.target)
        when (this.result) {
            HandleResults.NORMAL_SUCCESS -> {
                if (cause != null && cause.isOnline) {
                    cause.sendMessage("§a成功设置玩家 " + this.target + "的全局权限为 " + type.getPlayerPermission().permissionName)
                }
                if (target != null && target.isOnline) {
                    target.sendMessage("§l§8┃§bS§au§dm§em§4i§6t§8┃§8 - NOTICE: 你的全局权限已被调整为:" + type.getPlayerPermission().permissionName)
                } else {
                    val pk = PlayerSendMessagePacket()
                    pk.id = this.target
                    pk.message = "§l§8┃§bS§au§dm§em§4i§6t§8┃§8 - NOTICE: 你的全局权限已被调整为:" + type.getPlayerPermission().permissionName
                    FoundHiPlugin.instance.dataPacket(pk)
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
