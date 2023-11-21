package net.mcpes.foundhi.plugin.currency.npc.command

import cn.nukkit.Player
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import net.mcpes.foundhi.plugin.currency.npc.SNonPlayerCharacter.Companion.TITLE
import net.mcpes.foundhi.plugin.currency.npc.SNonPlayerCharacter.Companion.lists
import net.mcpes.foundhi.plugin.currency.npc.SNonPlayerCharacter.Companion.npcs
import net.mcpes.foundhi.plugin.currency.npc.npc.static.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/9
 * version 1.0
 */
class StaticNPCCommand(name: String) : Command(name) {

    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.isEmpty()) {
            sender.sendMessage(TITLE + "输入错误.请输入/fnpc help 查看帮助")
            return false
        }
        if (sender !is Player) {
            sender.sendMessage(TITLE + "后台禁止使用")
            return false
        }
        when (strings[0]) {
            "add" -> {
                if (strings.size != 4) {
                    sender.sendMessage(TITLE + "输入错误.请输入/fnpc help 查看帮助")
                    return false
                }
                if (lists.contains(strings[1])) {
                    sender.sendMessage(TITLE + "已经存在这个ID的npc了")
                    return false
                }
                val npc: StaticNonPlayerCharacter = when (strings[2]) {
                    "normal" -> NormalStaticNPC(strings[1])
                    "teleport" -> TeleportStaticNPC(strings[1])
                    "reply" -> ReplyStaticNPC(strings[1])
                    "command" -> CommandStaticNPC(strings[1])
                    "transfer" -> TransferStaticNPC(strings[1])
                    else -> {
                        sender.sendMessage(TITLE + "输入错误.请输入/fnpc help 查看帮助")
                        return false
                    }
                }
                npc.create = false
                npc.namedTag = strings[3]
                npc.equipmentInfos = HashMap()
                npc.handInfo = (object : HashMap<String, Any>() {
                    init {
                        put("id", 0)
                        put("damage", 0)
                        put("enchant", false)
                    }
                })
                npc.setPos(sender.location)
                npc.save()
                npc.spawnToAll()
                npcs.put(strings[1], npc)
                lists.add(strings[1])
                sender.sendMessage(TITLE + "成功添加ID为:" + strings[1] + "的NPC!")
            }
            "del" -> {
                if (!(lists.contains(strings[1]) && npcs.containsKey(strings[1]))) {
                    sender.sendMessage(TITLE + "不存在这个ID的npc!")
                    return false
                }
                val npc = npcs[strings[1]]!! as? StaticNonPlayerCharacter ?: return false
                npc.getEntity().despawnToAll()
                npc.remove()
                npcs.remove(strings[1])
                lists.remove(strings[1])
                sender.sendMessage(TITLE + "成功删除ID:" + strings[1] + "的NPC!")
            }
            "help" -> {
            }
            "type" -> {
                sender.sendMessage("§l§6===== " + TITLE + "Type List=====\n§l§3")
                sender.sendMessage("§l§3normal : 普通NPC(无实际功能)")
                sender.sendMessage("§l§3teleport : 传送NPC(传送到服务器的某个地点)")
                sender.sendMessage("§l§3transfer : 跨服NPC(跨服)")
                sender.sendMessage("§l§3reply : 回复NPC(对话)")
                sender.sendMessage("§l§3command : 命令NPC(执行命令)")
            }
            else -> sender.sendMessage(TITLE + "输入错误.请输入/fnpc help 查看帮助")
        }
        return true
    }
}