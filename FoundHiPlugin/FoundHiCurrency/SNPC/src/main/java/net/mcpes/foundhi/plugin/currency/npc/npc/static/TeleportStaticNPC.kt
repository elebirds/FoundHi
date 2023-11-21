package net.mcpes.foundhi.plugin.currency.npc.npc.static

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.level.Location
import cn.nukkit.utils.TextFormat
import net.mcpes.foundhi.plugin.currency.npc.SNonPlayerCharacter
import net.mcpes.foundhi.plugin.currency.npc.SNonPlayerCharacter.Companion.TITLE
import net.mcpes.foundhi.plugin.currency.npc.config.NPCConfig
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/9
 * version 1.0
 */
class TeleportStaticNPC(id: String) : StaticNonPlayerCharacter(id) {
    var teleportInfo = HashMap<String, Any?>()
        private set
    var location: Location? = null
        private set

    override fun load(): Boolean {
        var flag = false
        try {
            teleportInfo = (NPCConfig.teleport.get(this.id) as HashMap<String, Any>)["teleport"] as HashMap<String, Any?>
            if (!(this.teleportInfo["x"] == null || this.teleportInfo["y"] == null || this.teleportInfo["z"] == null)) {
                location = Location(teleportInfo["x"] as Double, teleportInfo["y"] as Double, teleportInfo["z"] as Double, Server.getInstance().getLevelByName(teleportInfo["level"].toString()))
            }
            flag = true
        } catch (ignored: Exception) {
        }

        return this.load(SNonPlayerCharacter.NPCType.Teleport) && flag
    }

    override fun save() {
        val map = this.baseMap()
        map.put("teleport", teleportInfo)
        NPCConfig.teleport.set(this.id, map)
        NPCConfig.teleport.save()
    }

    override fun onTouch(player: Player) {
        if (location == null) {
            if (!(this.teleportInfo["x"] == null || this.teleportInfo["y"] == null || this.teleportInfo["z"] == null)) {
                location = Location(teleportInfo["x"] as Double, teleportInfo["y"] as Double, teleportInfo["z"] as Double, Server.getInstance().getLevelByName(teleportInfo["level"].toString()))
            } else {
                player.sendMessage(TITLE + TextFormat.RED + "该NPC未设置传送目标")
            }
        }
        if (location!!.level == null) {
            player.sendMessage(TITLE + TextFormat.RED + "目标传送世界不存在")
        }
        player.teleport(location)
        player.sendMessage(TITLE + TextFormat.GREEN + "传送成功")
    }

    fun setTeleport(location: Location?) {
        assert(location != null)
        val info = HashMap<String, Any?>()
        info["x"] = location!!.x
        info["y"] = location.y
        info["z"] = location.z
        info["level"] = location.level.name
        this.teleportInfo = info
        this.location = location
    }

    override fun remove(): Boolean {
        NPCConfig.teleport.remove(this.id)
        NPCConfig.teleport.save()
        return true
    }
}
