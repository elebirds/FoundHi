package net.mcpes.foundhi.core.app.command.defaults

import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.command.Command
import net.mcpes.foundhi.core.app.command.CommandSender
import net.mcpes.foundhi.core.app.command.PacketCommandSender
import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.utils.format
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/7
 * version 1.0
 */
class FriendCommand(name: String) : Command(name, "好友命令") {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.isEmpty()) {
            if (sender is PacketCommandSender) {
                sender.result = -1
            } else {
                sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
            }
            return true
        }
        when (strings[0]) {
            "apply" -> {
                //friend apply <申请者> <被申请者>
                if (sender is PacketCommandSender) {
                    if (strings.size != 3) {
                        sender.result = -1
                    } else {
                        if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                            sender.result = -2
                            return true
                        }
                        if (!APIFunction.instance.hasPlayer(strings[2])) {
                            sender.result = -4
                            return true
                        }
                        var online = false
                        val player = APIFunction.instance.getPlayer(strings[1])!!
                        if (player.friends.containsKey(strings[2])) {
                            sender.result = -3
                            return true
                        }
                        val was = if (APIFunction.instance.isPlayerOnline(strings[2])) {
                            online = true
                            APIFunction.instance.getPlayer(strings[2])!!
                        } else {
                            Player(strings[2])
                        }
                        if (was.apply[player.name] == 1) {
                            was.delFriendApply(player.name)
                            player.delFriendApply(was.name)
                            val date = Date().format()
                            player.addFriend(was.name, date)
                            was.addFriend(player.name, date)
                            if (!online) was.save()
                            sender.result = 1
                            return true
                        }
                        player.addFriendApply(was.name, 1)
                        was.addFriendApply(player.name, 2)
                        if (online) {
                            was.sendMessage("§l§8┃§bS§au§dm§em§4i§6t§8┃§2 - NOTICE: 玩家:${player.name}申请加你为好友。")
                        } else {
                            was.save()
                        }
                    }
                }
            }
            "delete" -> {
                //friend delete <申请者> <被删者>
                if (sender is PacketCommandSender) {
                    if (strings.size != 3) {
                        sender.result = -1
                    } else {
                        if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                            sender.result = -2
                            return true
                        }
                        var online = false
                        val player = APIFunction.instance.getPlayer(strings[1])!!
                        if (!player.friends.containsKey(strings[2])) {
                            sender.result = -3
                            return true
                        }
                        if (APIFunction.instance.hasPlayer(strings[2])) {
                            val was = if (APIFunction.instance.isPlayerOnline(strings[2])) {
                                online = true
                                APIFunction.instance.getPlayer(strings[2])!!
                            } else {
                                Player(strings[2])
                            }
                            was.delFriend(player.name)
                            if (online) {
                                was.sendMessage("§l§8┃§bS§au§dm§em§4i§6t§8┃§2 - NOTICE: 玩家:${player.name}主动和你解除了好友关系。")
                            } else {
                                was.save()
                            }
                        }
                        player.delFriend(strings[2])
                    }
                }
            }
            "accept" -> {
                //friend accept <操作者> <申请者>
                if (sender is PacketCommandSender) {
                    if (strings.size != 3) {
                        sender.result = -1
                    } else {
                        if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                            sender.result = -2
                            return true
                        }
                        if (!APIFunction.instance.hasPlayer(strings[2])) {
                            sender.result = -4
                            return true
                        }
                        var online = false
                        val player = APIFunction.instance.getPlayer(strings[1])!!
                        if (player.friends.containsKey(strings[2])) {
                            sender.result = -3
                            return true
                        }
                        val was = if (APIFunction.instance.isPlayerOnline(strings[2])) {
                            online = true
                            APIFunction.instance.getPlayer(strings[2])!!
                        } else {
                            Player(strings[2])
                        }
                        was.delFriendApply(player.name)
                        player.delFriendApply(was.name)
                        val date = Date().format()
                        player.addFriend(was.name, date)
                        was.addFriend(player.name, date)
                        if (online) {
                            was.sendMessage("§l§8┃§bS§au§dm§em§4i§6t§8┃§2 - NOTICE: 玩家:${player.name}同意了你的好友请求。")
                        } else {
                            was.save()
                        }
                    }
                }
            }
            "refuse" -> {
                //friend refuse <操作者> <申请者>
                if (sender is PacketCommandSender) {
                    if (strings.size != 3) {
                        sender.result = -1
                    } else {
                        if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                            sender.result = -2
                            return true
                        }
                        var online = false
                        val player = APIFunction.instance.getPlayer(strings[1])!!
                        if (player.friends.containsKey(strings[2])) {
                            sender.result = -3
                            return true
                        }
                        if (APIFunction.instance.hasPlayer(strings[2])) {
                            val was = if (APIFunction.instance.isPlayerOnline(strings[2])) {
                                online = true
                                APIFunction.instance.getPlayer(strings[2])!!
                            } else {
                                Player(strings[2])
                            }
                            was.delFriendApply(player.name)
                            if (online) {
                                was.sendMessage("§l§8┃§bS§au§dm§em§4i§6t§8┃§2 - NOTICE: 玩家:${player.name}拒绝了你的好友请求。")
                            } else {
                                was.save()
                            }
                        }
                        player.delFriendApply(strings[2])
                    }
                }
            }
        }
        return true
    }
}