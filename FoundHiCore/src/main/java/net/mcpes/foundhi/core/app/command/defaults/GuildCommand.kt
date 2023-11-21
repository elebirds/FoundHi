package net.mcpes.foundhi.core.app.command.defaults

import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.command.Command
import net.mcpes.foundhi.core.app.command.CommandSender
import net.mcpes.foundhi.core.app.command.PacketCommandSender
import net.mcpes.foundhi.core.app.data.guild.Guild
import net.mcpes.foundhi.core.dao.guild.GuildDatabase
import net.mcpes.foundhi.core.dao.player.PlayerGuildDatabase
import net.mcpes.foundhi.core.handle.HandleResults
import net.mcpes.foundhi.core.utils.getGuildPermission
import java.sql.SQLException

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/27
 * version 1.0
 */
class GuildCommand(name: String) : Command(name, "公会系统") {

    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.isEmpty()) {
            if (sender is PacketCommandSender) {
                sender.result = HandleResults.PARAMETER_ERROR
            } else {
                sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
            }
            return true
        }
        when (strings[0]) {
            "ban" -> {
                //guild ban <bebaner> <admin>

            }
            "unban" -> {
                //guild ban <unbaned> <admin>
            }
            "base" -> {
                //guild base <SummitPosition> <leader>
            }
            "appoint" -> {
                //guild appoint <appointTo> <leader> <position>
                if (sender !is PacketCommandSender) return false
                if (strings.size != 4) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                val leader = strings[2]
                try {
                    if (!APIFunction.instance.isPlayerOnline(leader)) {
                        sender.result = HandleResults.PLAYER_OFFLINE
                        return true
                    }
                    val position = strings[3].toInt()
                    if (position < 1 || position > 3) {
                        sender.result = HandleResults.NOT_EXIST_POSITION
                    }
                    val player = APIFunction.instance.getPlayer(leader)!!
                    val guild = APIFunction.instance.getGuild(player.guild)
                    if (guild != null) {
                        if (guild.leader != player.name) {
                            sender.result = HandleResults.NO_PERMISSION
                            return true
                        }
                        val appointTo = strings[1]
                        if (!guild.isMember(appointTo)) {
                            sender.result = HandleResults.NOT_MEMBER
                            return true
                        }
                        val permission = position.getGuildPermission()
                        if (permission.count < guild.getAdminCount(permission)) {
                            sender.result = HandleResults.TOO_MUCH_ADMIN
                            return true
                        }
                        if (guild.getAdminPermission(appointTo) == permission) {
                            sender.result = HandleResults.ALREADY_IS_THIS_POSITION
                            return true
                        }
                        guild.addAdmin(appointTo, permission)
                    } else {
                        sender.result = HandleResults.NOT_EXIST_GUILD
                    }
                } catch (e: NumberFormatException) {
                    sender.result = HandleResults.PARAMETER_ERROR
                }
            }
            "transfer" -> {
                //guild transfer <leaderTo> <leaderNow>
                if (sender !is PacketCommandSender) return false
                if (strings.size != 3) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                val leaderTo = strings[1]
                val leaderNow = strings[2]
                if (!APIFunction.instance.isPlayerOnline(leaderNow)) {
                    sender.result = HandleResults.PLAYER_OFFLINE
                    return true
                }
                val player = APIFunction.instance.getPlayer(leaderNow)!!
                if (player.guild == -1) {
                    sender.result = HandleResults.NOT_JOIN_GUILD
                    return true
                }
                val guild = APIFunction.instance.getGuild(player.guild)
                if (guild != null) {
                    if (guild.leader != leaderNow) {
                        sender.result = HandleResults.NOT_LEADER
                        return true
                    }
                    if (!guild.isMember(leaderTo)) {
                        sender.result = HandleResults.NOT_MEMBER
                        return true
                    }
                    guild.leader = leaderTo
                } else {
                    sender.result = HandleResults.NOT_EXIST_GUILD
                }
            }
            "invite" -> {
                //guild invite <from> <to>
                if (sender !is PacketCommandSender) return false
                if (strings.size != 3) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                    sender.result = HandleResults.THE_OTHER_OFFLINE
                    return true
                }
                run {
                    val player = APIFunction.instance.getPlayer(strings[1])!!
                    val guild = APIFunction.instance.getGuild(player.guild)
                    if (guild == null) {
                        sender.result = HandleResults.NOT_JOIN_GUILD
                        return true
                    }
                    //todo 可邀请人权限未定
                    if (!guild.admins.containsKey(player.name)) {
                        sender.result = HandleResults.NO_PERMISSION
                        return true
                    }
                    val to = strings[2]
                    if (!APIFunction.instance.hasPlayer(to)) {
                        sender.result = HandleResults.THE_OTHER_OFFLINE
                        return true
                    }
                    if (APIFunction.instance.isPlayerOnline(to)) {
                        //玩家在线
                        val player2 = APIFunction.instance.getPlayer(to)!!
                        if (player2.guild != -1) {
                            sender.result = HandleResults.THE_OTHER_ALREADY_JOIN_GUILD
                            return true
                        }
                        if (player2.invitation.containsKey(player.name)) {
                            sender.result = HandleResults.ALREADY_INVITE
                            return true
                        }
                        player2.addInvitation(player.name, player.guild)
                        player2.sendMessage("§l§8┃§bS§au§dm§em§4i§6t§8┃§2 - NOTICE: 玩家:" + player.name + " 邀请你加入他的公会:" + guild.name + "!请输入/公会 <接受邀请/拒绝邀请> " + player.name + " 来回复邀请!")
                    } else {
                        try {
                            val gd = PlayerGuildDatabase(to)
                            if (gd.guild != -1) {
                                sender.result = HandleResults.THE_OTHER_ALREADY_JOIN_GUILD
                                return true
                            }
                            val invite = gd.invitationMessage
                            if (invite.containsKey(player.name)) {
                                sender.result = HandleResults.ALREADY_INVITE
                                return true
                            }
                            invite[player.name] = player.guild
                            gd.invitationMessage = invite
                        } catch (e: SQLException) {
                            sender.result = HandleResults.UNKNOW_ERROR
                        }
                    }
                }
            }
            "accept" -> {
                if (sender !is PacketCommandSender) return false
                //guild accept <from> <to>
                if (strings.size != 3) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                val to = strings[2]
                val from = strings[1]
                if (!APIFunction.instance.isPlayerOnline(from)) {
                    sender.result = HandleResults.THE_OTHER_OFFLINE
                    return true
                }
                val player = APIFunction.instance.getPlayer(from)!!
                if (!player.invitation.containsKey(to)) {
                    sender.result = HandleResults.NO_INVITATION
                    return true
                }
                if (player.guild != -1) {
                    sender.result = HandleResults.ALREADY_JOIN_GUILD
                    return true
                }
                val gid: Int = player.invitation[to]!!
                val guild: Guild? = APIFunction.instance.getGuild(gid)
                if (guild == null) {
                    sender.result = HandleResults.NOT_EXIST_GUILD
                    return true
                }
                if (guild.members.contains(from)) {
                    sender.result = HandleResults.ALREADY_IS_MEMBER
                    return true
                }
                if (guild.members.size >= guild.maximum) {
                    sender.result = HandleResults.GUILD_ALREADY_FULL
                    return true
                }
                if (guild.isAdmin(to)) {
                    guild.addMember(from)
                } else {
                    sender.result = HandleResults.SCCESS_SEND_APPLY
                    //TODO 发送申请
                }
            }
            "reject" -> {
                if (sender !is PacketCommandSender) return false
                //guild reject <rejecter> <inviter>
                if (strings.size != 2) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                val inviter = strings[2]
                val rejecter = strings[1]
                if (!APIFunction.instance.isPlayerOnline(rejecter)) {
                    sender.result = HandleResults.THE_OTHER_OFFLINE
                    return true
                }
                val player = APIFunction.instance.getPlayer(rejecter)!!
                if (player.guild != -1) {
                    sender.result = HandleResults.ALREADY_JOIN_GUILD
                }
                if (player.guild == player.invitation[inviter]) {
                    sender.result = HandleResults.ALREADY_IS_MEMBER
                }
                try {
                    player.delInvitation(inviter)
                } catch (e: SQLException) {
                    sender.result = HandleResults.UNKNOW_ERROR
                }
            }
            "add" -> {
                //guild add <leader> <name>
                if (sender is PacketCommandSender) {
                    if (strings.size != 3) {
                        sender.result = HandleResults.PARAMETER_ERROR
                        return true
                    }
                    if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                        sender.result = HandleResults.PLAYER_OFFLINE
                        return true
                    }
                    val player = APIFunction.instance.getPlayer(strings[1])!!
                    try {
                        if (GuildDatabase.existsName(strings[2])) {
                            sender.result = HandleResults.GUILD_NAME_EXIST
                            return true
                        }
                        player.delMoney1(Guild.NEED.toDouble())
                        val guild = Guild(strings[2], strings[1])
                        player.guild = guild.id
                        APIFunction.instance.addGuild(guild)
                        sender.result = guild.id
                    } catch (e: SQLException) {
                        sender.result = HandleResults.UNKNOW_ERROR
                    }

                } else {
                    if (strings.size != 3) {
                        sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                        return true
                    }
                    if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                        sender.sendMessage("§c玩家不在线。")
                        return true
                    }
                    val player = APIFunction.instance.getPlayer(strings[1])!!
                    if (player.money1 < Guild.NEED) {
                        sender.sendMessage("§c玩家没有足够的余额。")
                        return true
                    }
                    if (player.guild != -1) {
                        sender.sendMessage("§c玩家已经加入工会。")
                        return true
                    }
                    try {
                        if (GuildDatabase.existsName(strings[2])) {
                            sender.sendMessage("§c公会名称已被占有!")
                            return true
                        }
                        player.delMoney1(Guild.NEED.toDouble())
                        val guild = Guild(strings[2], strings[1])
                        player.guild = guild.id
                        APIFunction.instance.addGuild(guild)
                        sender.sendMessage("§2创建成功,公会ID为:" + guild.id + "!")
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }

                }
            }
            "remove" -> {
            }
            "kick" -> {
                if (sender !is PacketCommandSender) return false
                //guild kick <kicker> <be kicker>
                if (strings.size != 2) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                    sender.result = HandleResults.PLAYER_OFFLINE
                    return true
                }
                val kicker = APIFunction.instance.getPlayer(strings[1])!!
                if (kicker.guild > -1 && APIFunction.instance.hasGuild(kicker.guild)) {
                    val guild = APIFunction.instance.getGuild(kicker.guild)!!
                    val beKicker = strings[2]
                    if (!guild.isMember(beKicker)) {
                        sender.result = HandleResults.NOT_MEMBER
                        return true
                    }
                    if (!guild.isAdmin(name)) {
                        sender.result = HandleResults.NO_PERMISSION
                        return true
                    }
                    if (guild.isAdmin(beKicker)) {
                        if (guild.getAdminPermission(beKicker)!!.level >= guild.getAdminPermission(kicker.name)!!.level) {
                            sender.result = HandleResults.NO_PERMISSION
                            return true
                        }
                    }
                    try {
                        guild.delMember(beKicker)
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }
                } else {
                    sender.result = HandleResults.NOT_JOIN_GUILD
                    return true
                }
            }
            "dissolve" -> if (sender is PacketCommandSender) {
                if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                    sender.result = -2
                    return true
                }
                val player = APIFunction.instance.getPlayer(strings[1])!!
                if (!APIFunction.instance.hasGuild(player.guild)) {
                    sender.result = -3
                    return true
                }
                if (APIFunction.instance.getGuild(player.guild)!!.leader != strings[1]) {
                    sender.result = -4
                    return true
                }
                sender.result = if (APIFunction.instance.getGuild(player.guild)!!.dissolve()) 0 else -5
            } else {
                if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                    sender.sendMessage("§c玩家不在线。")
                    return true
                }
                val player = APIFunction.instance.getPlayer(strings[1])
                if (!APIFunction.instance.hasGuild(player!!.guild)) {
                    sender.sendMessage("§c玩家未加入公会。")
                    return true
                }
                if (APIFunction.instance.getGuild(player.guild)!!.leader != strings[1]) {
                    sender.sendMessage("§c玩家不是公会会长。")
                    return true
                }
                if (APIFunction.instance.getGuild(player.guild)!!.dissolve()) {
                    sender.sendMessage("§e成功解散 玩家:" + strings[1] + " 的公会")
                } else {
                    sender.sendMessage("§c解散公会时遇到了未知错误!")
                }
            }
            "designate" -> {
            }
            else -> if (sender is PacketCommandSender) {
                sender.result = -1
            } else {
                sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
            }
        }
        return true
    }
}