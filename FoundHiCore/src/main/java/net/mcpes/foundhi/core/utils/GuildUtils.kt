package net.mcpes.foundhi.core.utils

import net.mcpes.foundhi.core.app.data.guild.Guild
import net.mcpes.foundhi.core.handle.HandleResults
import net.mcpes.foundhi.core.handle.HandleSystem
import net.mcpes.foundhi.core.handle.guild.*
import net.mcpes.foundhi.core.network.protocol.command.CommandPacket
import net.mcpes.foundhi.core.plugin.FoundHiPlugin

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/22
 * version 1.0
 */

fun Int.getGuildPermission(): Guild.Permission {
    return when (this) {
        1 -> Guild.Permission.ADMIN
        2 -> Guild.Permission.HIGH_ADMIN
        3 -> Guild.Permission.VICE_LEADER
        4 -> Guild.Permission.LEADER
        else -> Guild.Permission.PLAYER
    }
}

object GuildUtils {
    private val main = FoundHiPlugin.instance

    @JvmStatic
    fun create(player: String, name: String): Int {
        if (!main.isPlayerOnline(player)) return HandleResults.PLAYER_OFFLINE
        val pd = main.getPlayer(player)!!
        if (pd.guild != -1) return HandleResults.ALREADY_JOIN_GUILD
        if (pd.money1 < Guild.NEED) return HandleResults.NOT_ENOUGH_MONEY
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + main.id
        cp.event = true
        cp.sid = main.id
        cp.cmd = "guild add $player $name"
        main.addHandle(evid, GuildCreateHandle(player, name))
        main.dataPacket(cp)
        return HandleResults.NORMAL_SUCCESS
    }

    @JvmStatic
    fun dissolve(name: String): Int {
        if (!main.isPlayerOnline(name)) return HandleResults.PLAYER_OFFLINE
        val pd = main.getPlayer(name)!!
        if (!main.hasGuild(pd.guild)) return HandleResults.NOT_EXIST_GUILD
        if (main.getGuild(pd.guild)!!.leader != name) return HandleResults.NOT_LEADER
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + main.id
        cp.event = true
        cp.sid = main.id
        cp.cmd = "guild dissolve $name"
        main.addHandle(evid, GuildDissolveHandle(name, pd.guild))
        main.dataPacket(cp)
        return HandleResults.NORMAL_SUCCESS
    }

    @JvmStatic
    fun invite(from: String, to: String): Int {
        if (!main.isPlayerOnline(from)) return HandleResults.PLAYER_OFFLINE
        val pd = main.getPlayer(from)!!
        val guild = main.getGuild(pd.guild) ?: return HandleResults.NOT_EXIST_GUILD
        //todo 可邀请人权限未定
        if (!guild.admins.containsKey(from)) {
            return HandleResults.NO_PERMISSION
        }
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + main.id
        cp.event = true
        cp.sid = main.id
        cp.cmd = "guild invite $from $to"
        main.addHandle(evid, GuildInviteHandle(from, to))
        main.dataPacket(cp)
        return HandleResults.NORMAL_SUCCESS
    }

    @JvmStatic
    fun accept(name: String, inviter: String): Int {
        if (!main.isPlayerOnline(name)) return HandleResults.PLAYER_OFFLINE
        val player = main.getPlayer(name)!!
        if (!player.invitation.containsKey(inviter)) return HandleResults.NO_INVITATION
        val guild = player.invitation[inviter]!!
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + main.id
        cp.event = true
        cp.sid = main.id
        cp.cmd = "guild accept $name $inviter"
        main.addHandle(evid, GuildAcceptHandle(name, inviter, main.getGuild(guild)!!.name))
        main.dataPacket(cp)
        return HandleResults.NORMAL_SUCCESS
    }

    @JvmStatic
    fun reject(rejecter: String, inviter: String): Int {
        val main = main
        if (!main.isPlayerOnline(rejecter)) return HandleResults.PLAYER_OFFLINE
        val player = main.getPlayer(rejecter)!!
        if (!player.invitation.containsKey(inviter)) return HandleResults.NO_INVITATION
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + main.id
        cp.event = true
        cp.sid = main.id
        cp.cmd = "guild reject $rejecter $inviter"
        main.addHandle(evid, GuildRejectHandle(rejecter))
        main.dataPacket(cp)
        return HandleResults.NORMAL_SUCCESS
    }

    @JvmStatic
    fun kick(kicker: String, beKicker: String): Int {
        if (!main.isPlayerOnline(kicker)) return HandleResults.PLAYER_OFFLINE
        val player = main.getPlayer(kicker)!!
        if (player.guild == -1) {
            return HandleResults.NOT_JOIN_GUILD
        }
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + main.id
        cp.event = true
        cp.sid = main.id
        cp.cmd = "guild kick $kicker $beKicker"
        main.addHandle(evid, GuildRejectHandle(kicker))
        main.dataPacket(cp)
        return HandleResults.NORMAL_SUCCESS
    }

    @JvmStatic
    fun transfer(leaderTo: String, leaderNow: String): Int {
        if (!main.isPlayerOnline(leaderNow)) return HandleResults.PLAYER_OFFLINE
        val player = main.getPlayer(leaderNow)!!
        if (player.guild == -1) {
            return HandleResults.NOT_JOIN_GUILD
        }
        if (leaderTo == leaderNow) {
            return HandleResults.PARAMETER_ERROR
        }
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + main.id
        cp.event = true
        cp.sid = main.id
        cp.cmd = "guild transfer $leaderTo $leaderNow"
        main.addHandle(evid, GuildRejectHandle(leaderNow))
        main.dataPacket(cp)
        return HandleResults.NORMAL_SUCCESS
    }

    @JvmStatic
    fun appoint(appointTo: String, leader: String, position: Int): Int {
        if (!main.isPlayerOnline(leader)) return HandleResults.PLAYER_OFFLINE
        val player = main.getPlayer(leader)!!
        if (player.guild == -1) {
            return HandleResults.NOT_JOIN_GUILD
        }
        val cp = CommandPacket()
        val evid = Event.eventID++
        cp.eventID = evid.toString() + main.id
        cp.event = true
        cp.sid = main.id
        cp.cmd = "guild appoint $appointTo $leader $position"
        main.addHandle(evid, GuildAppointHandle(leader))
        main.dataPacket(cp)
        return HandleResults.NORMAL_SUCCESS
    }

    @JvmStatic
    fun base(player: String): Int {
        if (!main.isPlayerOnline(player)) return HandleResults.PLAYER_OFFLINE
        val player = main.getPlayer(player)!!
        return HandleResults.NORMAL_SUCCESS
    }
}
