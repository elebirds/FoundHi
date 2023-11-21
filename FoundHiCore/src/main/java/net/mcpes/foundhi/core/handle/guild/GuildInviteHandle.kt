package net.mcpes.foundhi.core.handle.guild

import cn.nukkit.Server
import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.data.guild.Guild
import net.mcpes.foundhi.core.handle.HandleClass
import net.mcpes.foundhi.core.handle.HandleResults

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/24
 * version 1.0
 */
class GuildInviteHandle(private val from: String, private val to: String) : HandleClass() {
    override fun handle() {
        if (!APIFunction.instance.isPlayerOnline(from)) return
        val player = Server.getInstance().getPlayerExact(this.from)
        when (result) {
            HandleResults.NORMAL_SUCCESS -> if (player != null && !player.isOnline) {
                player.sendMessage(Guild.GUILD_TITLE + "§a操作成功!")
            }
            HandleResults.PARAMETER_ERROR -> if (player != null && !player.isOnline) {
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:参数错误!")
            }
            HandleResults.NOT_JOIN_GUILD -> if (player != null && !player.isOnline) {
                player.sendMessage(Guild.GUILD_TITLE + "§a你还未加入公会!")
            }
            HandleResults.PLAYER_OFFLINE -> if (player != null && !player.isOnline) {
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:玩家已下线!")
            }
            HandleResults.NO_PERMISSION -> if (player != null && !player.isOnline) {
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:权限不足!")
            }
            HandleResults.THE_OTHER_OFFLINE -> if (player != null && !player.isOnline) {
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:对方不在线!")
            }
            HandleResults.ALREADY_INVITE -> if (player != null && !player.isOnline) {
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:你已经邀请过对方了!")
            }
            HandleResults.ALREADY_JOIN_GUILD -> if (player != null && !player.isOnline) {
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:对方已经加入公会!")
            }
            HandleResults.UNKNOW_ERROR -> if (player != null && !player.isOnline) {
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:未知错误!")
            }
        }
    }
}
