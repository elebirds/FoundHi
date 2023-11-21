package net.mcpes.foundhi.core.app.command.defaults

import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.command.Command
import net.mcpes.foundhi.core.app.command.CommandSender
import net.mcpes.foundhi.core.app.command.PacketCommandSender
import net.mcpes.foundhi.core.handle.HandleResults
import net.mcpes.foundhi.core.team.TeamSystem

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/21
 * version 1.0
 */
class TeamCommand(name: String) : Command(name, "好友命令") {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (sender !is PacketCommandSender) return true
        if (strings.isEmpty()) {
            sender.result = HandleResults.PARAMETER_ERROR
            return true
        }
        when (strings[0]) {
            "create" -> {
                if (strings.size != 2) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                //team create <leaderName>
                if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                    sender.result = HandleResults.PLAYER_OFFLINE
                    return true
                }
                sender.result = TeamSystem.instance.addTeam(strings[1])
            }
            "dissolve" -> {
                if (strings.size != 2) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                //team dissolve <leaderName>
                if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                    sender.result = HandleResults.PLAYER_OFFLINE
                    return true
                }
                val player = APIFunction.instance.getPlayer(strings[1])!!
                val team = TeamSystem.instance.getTeam(player.inTeam)
                if (team == null) {
                    sender.result = HandleResults.NOT_JOIN_TEAM
                    return true
                }
                if (team.leader != player.name) {
                    sender.result = HandleResults.TEAM_NOT_LEADER
                    return true
                }
                team.dissolve("队长:${player.name}解散")
            }
            "invite" -> {
                if (strings.size != 3) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                //team dissolve <队长> <被邀请人>
                if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                    sender.result = HandleResults.PLAYER_OFFLINE
                    return true
                }
                val player = APIFunction.instance.getPlayer(strings[1])!!
                val team = TeamSystem.instance.getTeam(player.inTeam)
                if (team == null) {
                    sender.result = HandleResults.NOT_JOIN_TEAM
                    return true
                }
                if (team.leader != player.name) {
                    sender.result = HandleResults.TEAM_NOT_LEADER
                    return true
                }
                team.apply[strings[2]] = 1
                sender.result = HandleResults.TEAM_NOT_LEADER

            }
            "apply" -> {
                if (strings.size != 3) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                //team dissolve <申请者> <小队id>

            }
        }
        return true
    }
}