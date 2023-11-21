package net.mcpes.foundhi.plugin.currency.npc.npc.static

import cn.nukkit.Player
import net.mcpes.foundhi.plugin.currency.npc.SNonPlayerCharacter
import net.mcpes.foundhi.plugin.currency.npc.config.NPCConfig
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/9
 * version 1.0
 */
class ReplyStaticNPC(id: String) : StaticNonPlayerCharacter(id) {
    lateinit var messages: ArrayList<String>
        private set

    override fun load(): Boolean {
        var flag = false
        try {
            messages = (NPCConfig.reply.get(this.id)!! as HashMap<String, Any>)["messages"] as ArrayList<String>
            flag = true
        } catch (ignored: Exception) {
        }

        return this.load(SNonPlayerCharacter.NPCType.Reply) && flag
    }

    override fun save() {
        val map = this.baseMap()
        map.put("message", messages)
        NPCConfig.reply.set(this.id, map)
        NPCConfig.reply.save()
    }

    override fun onTouch(player: Player) {
        if (messages.size > 0) {
            player.sendMessage("§l§6<" + this.namedTag + "> " + messages[Random().nextInt(messages.size)])
        }
    }

    fun addMessage(message: String) {
        messages.add(message)
    }

    fun removeMessage(message: String) {
        messages.remove(message)
    }

    override fun remove(): Boolean {
        NPCConfig.reply.remove(this.id)
        NPCConfig.reply.save()
        return true
    }
}
