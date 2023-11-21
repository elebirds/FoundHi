package net.mcpes.foundhi.core.app.command.defaults

import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.command.Command
import net.mcpes.foundhi.core.app.command.CommandSender
import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.dao.banned.BannedPlayerDatabase
import net.mcpes.foundhi.core.network.protocol.banned.UnblockPlayerBannedPacket
import net.mcpes.foundhi.core.utils.format
import java.text.SimpleDateFormat

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/27
 * version 1.0
 */

class BanCommand(name: String) : Command(name, "封禁系统") {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.isEmpty()) {
            sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
            return false
        }
        when (strings[0]) {
            "block" -> {
                try {
                    if (strings.size !in 3..4) {
                        sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                        return false
                    }
                    if (!APIFunction.instance.hasPlayer(strings[1])) {
                        sender.sendMessage("§c玩家不存在。")
                        return true
                    }
                    val online = APIFunction.instance.isPlayerOnline(strings[1])
                    val player = if (online) {
                        APIFunction.instance.getPlayer(strings[1])!!
                    } else {
                        Player(strings[1])
                    }
                    if (APIFunction.instance.stillBanned(strings[1])) {
                        sender.sendMessage("§c玩家:${strings[1]}已经被封禁了!")
                    }
                    val time = when (strings.size) {
                        3 -> player.ban("System-FoundHiAPI", strings[2], online)
                        4 -> player.ban("System-FoundHiAPI", strings[2], online, strings[3])
                        else -> "1970-01-01"
                    }
                    sender.sendMessage("§6成功封禁玩家:${player.name},原因:${strings[3]},时间至$time,操作者:System-FoundHiAPI")
                } catch (e: NumberFormatException) {
                    sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                    return false
                } catch (e: Exception) {
                    sender.sendMessage("§c出现未知错误。")
                    return false
                }
            }
            "unblock" -> {
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                    return false
                }
                val id = try {
                    strings[1].toInt()
                } catch (e: Exception) {
                    sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                    return false
                }
                if (!APIFunction.instance.hasBanned(id)) {
                    sender.sendMessage("§c封禁记录不存在。")
                    return true
                }
                val banned = APIFunction.instance.getBanned(id)!!
                if (banned.endTime.time > System.currentTimeMillis()) {
                    banned.reviser = "System-FoundHiAPI"
                    banned.beLifted = true
                    val pk = UnblockPlayerBannedPacket()
                    pk.id = id
                    pk.reviser = banned.reviser
                    APIFunction.instance.broadcastPacket(pk)
                    BannedPlayerDatabase.save(banned)
                    sender.sendMessage("§6成功解除封禁记录:${banned.id}")
                } else {
                    sender.sendMessage("§c封禁记录:${banned.id}已经到期")
                }
            }
            "list" -> {
                if (strings.size !in 2..3) {
                    sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                    return false
                }
                val flag = if (strings.size == 3) {
                    try {
                        strings[2].toBoolean()
                    } catch (e: Exception) {
                        false
                    }
                } else {
                    false
                }
                if (!APIFunction.instance.hasPlayer(strings[1])) {
                    sender.sendMessage("§c玩家不存在。")
                    return true
                }
                if (!APIFunction.instance.hasPlayerBanned(strings[1])) {
                    sender.sendMessage("§c玩家没有被封禁的记录")
                    return true
                }
                var message = ""
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val array = APIFunction.instance.getPlayerBanneds(strings[1])!!
                array.forEach {
                    message += if (flag) {
                        "§a封禁ID:${it.id},封禁者:${it.operator},封禁原因:${it.reason},封禁起始时间:${format.format(it.startTime)},封禁结束时间:${format.format(it.endTime)},途中是否手动解封:${it.beLifted},解封者:${it.reviser}\n"
                    } else {
                        "§a封禁ID:${it.id}\n"
                    }
                }
                sender.sendMessage(message + "§3共${array.size}条封禁记录")
            }
            "see" -> {
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                    return false
                }
                if (!APIFunction.instance.hasBanned(strings[1].toInt())) {
                    sender.sendMessage("§c封禁记录不存在。")
                    return true
                }
                val banned = APIFunction.instance.getBanned(strings[1].toInt())!!
                sender.sendMessage("§6===封禁记录:${banned.id}===\n§a操作者:${banned.operator}\n§b被封禁者:${banned.banned}\n§c开始时间:${banned.startTime.format()}\n§d结束时间:${banned.endTime.format()}\n§1封禁原因:${banned.reason}\n§2是否手动解封:${banned.beLifted}\n§3解封者:${banned.reviser}")
            }
        }
        return true
    }
}