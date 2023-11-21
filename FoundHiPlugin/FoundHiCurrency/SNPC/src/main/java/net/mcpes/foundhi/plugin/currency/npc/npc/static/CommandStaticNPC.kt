package net.mcpes.foundhi.plugin.currency.npc.npc.static

import cn.nukkit.Player
import cn.nukkit.Server
import net.mcpes.foundhi.plugin.currency.npc.SNonPlayerCharacter
import net.mcpes.foundhi.plugin.currency.npc.config.NPCConfig
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/9
 * version 1.0
 */
class CommandStaticNPC(id: String) : StaticNonPlayerCharacter(id) {
    var commands = ArrayList<String>()
        private set

    override fun load(): Boolean {
        var flag = false
        try {
            commands = (NPCConfig.command.get(this.id) as HashMap<String, Any>)["commands"] as ArrayList<String>
            flag = true
        } catch (ignored: Exception) {
        }

        return this.load(SNonPlayerCharacter.NPCType.Command) && flag
    }

    override fun save() {
        val map = this.baseMap()
        map.put("commands", commands)
        NPCConfig.command.set(this.id, map)
        NPCConfig.command.save()
    }

    override fun onTouch(player: Player) {
        this.commands.forEach { cmd ->
            var s = cmd.replace("%p", player.name).replace("%x", player.getX().toString()).replace("%y", player.getY().toString()).replace("%z", player.getZ().toString())
            if (!player.isOp && s.contains("%op")) {
                s = s.replace("%op", "")
                player.isOp = true
                Server.getInstance().dispatchCommand(player, s)
                player.isOp = false
            } else {
                s = s.replace("%op", "")
                Server.getInstance().dispatchCommand(player, s)
            }
        }
    }

    fun addCommand(command: String) {
        commands.add(command)
    }

    fun removeCommand(command: String) {
        commands.remove(command)
    }

    override fun remove(): Boolean {
        NPCConfig.command.remove(this.id)
        NPCConfig.command.save()
        return true
    }
}
