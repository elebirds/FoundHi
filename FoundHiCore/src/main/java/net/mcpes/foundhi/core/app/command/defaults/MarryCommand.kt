package net.mcpes.foundhi.core.app.command.defaults


import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.command.Command
import net.mcpes.foundhi.core.app.command.CommandSender
import net.mcpes.foundhi.core.app.command.PacketCommandSender
import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.dao.player.PlayerGuildDatabase
import net.mcpes.foundhi.core.handle.HandleResults
import net.mcpes.foundhi.core.network.protocol.player.PlayerSendMessagePacket

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/30
 * version 1.0
 */
class MarryCommand(name: String) : Command(name, "情侣结婚命令") {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (sender !is PacketCommandSender) return false
        if (strings.isEmpty()) {
            sender.result = HandleResults.PARAMETER_ERROR
            return true
        }
        when (strings[0]) {
            "marry" -> {
                if (strings.size != 3) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                if ((!APIFunction.instance.isPlayerOnline(strings[1])) || (!APIFunction.instance.isPlayerOnline(strings[2]))) {
                    sender.result = HandleResults.THE_OTHER_OFFLINE
                    return true
                }
                val from: Player = APIFunction.instance.getPlayer(strings[1])!!
                val to: Player = APIFunction.instance.getPlayer(strings[2])!!
                if (from.lover != "") {
                    sender.result = HandleResults.ALREADY_HAVE_LOVER
                    return true
                }
                if (to.lover != "") {
                    sender.result = HandleResults.THE_OTHER_HAVE_LOVER
                    return true
                }
                if (to.marry.contains(from.name)) {
                    sender.result = HandleResults.ALREADY_APPLICATION
                    return true
                }
                if (from.marry.contains(to.name)) {
                    sender.result = HandleResults.MARRY_SUCCESS
                    from.delMarry(to.name)
                    to.delMarry(from.name)
                    from.lover = to.name
                    to.lover = from.lover
                    val pk = PlayerSendMessagePacket()
                    pk.id = to.name
                    pk.message = "§e恭喜您和${from.name}缔结契约，成为了情侣"
                    return true
                }
                to.addMarry(from.name)
                to.sendMessage("§2${from.name}申请和您成为情侣,请输入/marry <accept/reject> ${from.name} 来回复邀请")
                //marry marry from to
            }
            "divorce" -> {
                if (strings.size != 2) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                    sender.result = HandleResults.THE_OTHER_OFFLINE
                    return true
                }
                val from: Player = APIFunction.instance.getPlayer(strings[1])!!
                if (from.lover == "") {
                    sender.result = HandleResults.NOT_HAVE_LOVER
                    return true
                }
                if (APIFunction.instance.isPlayerOnline(from.lover)) {
                    val to: Player = APIFunction.instance.getPlayer(from.lover)!!
                    to.lover = ""
                    from.lover = ""
                    to.sendMessage("§2${from.name}和您离婚了")
                } else {
                    PlayerGuildDatabase(from.lover).lover = ""
                }
            }
            "accept" -> {
                if (strings.size != 3) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                    sender.result = HandleResults.UNKNOW_ERROR
                    return true
                }
                val from: Player = APIFunction.instance.getPlayer(strings[1])!!
                if (from.lover != "") {
                    sender.result = HandleResults.ALREADY_HAVE_LOVER
                    return true
                }
                if (!from.marry.contains(strings[2])) {
                    sender.result = HandleResults.NO_APPLICATION
                    return true
                }
                from.delMarry(strings[2])
                from.lover = strings[2]
                if (APIFunction.instance.isPlayerOnline(strings[2])) {
                    val to: Player = APIFunction.instance.getPlayer(strings[2])!!
                    if (to.lover != "") {
                        sender.result = HandleResults.THE_OTHER_HAVE_LOVER
                        return true
                    }
                    to.lover = from.name
                    to.delMarry(from.name)
                    to.sendMessage("§e恭喜您和${from.name}缔结契约，成为了情侣")
                } else {
                    val to = PlayerGuildDatabase(strings[2])
                    if (to.lover != "") {
                        sender.result = HandleResults.THE_OTHER_HAVE_LOVER
                        return true
                    }
                    to.lover = from.name
                    val marry = to.marry
                    marry.remove(from.name)
                    to.marry = marry
                }
            }
            "reject" -> {
                if (strings.size != 3) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                    sender.result = HandleResults.UNKNOW_ERROR
                    return true
                }
                val from: Player = APIFunction.instance.getPlayer(strings[1])!!
                if (!from.marry.contains(strings[2])) {
                    sender.result = HandleResults.THE_OTHER_NO_APPLICATION
                    return true
                }
                from.delMarry(strings[2])
                if (APIFunction.instance.isPlayerOnline(strings[2])) {
                    val to: Player = APIFunction.instance.getPlayer(strings[2])!!
                    to.delMarry(from.name)
                    to.sendMessage("§e${from.name}拒绝和您成为情侣")
                } else {
                    val to = PlayerGuildDatabase(strings[2])
                    val marry = to.marry
                    marry.remove(from.name)
                    to.marry = marry
                }
            }
        }
        return false
    }
}