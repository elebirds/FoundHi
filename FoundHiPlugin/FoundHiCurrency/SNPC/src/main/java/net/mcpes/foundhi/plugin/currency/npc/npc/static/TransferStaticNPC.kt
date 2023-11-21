package net.mcpes.foundhi.plugin.currency.npc.npc.static

import cn.nukkit.Player
import net.mcpes.foundhi.plugin.currency.npc.SNonPlayerCharacter
import net.mcpes.foundhi.plugin.currency.npc.config.NPCConfig
import java.net.InetSocketAddress

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/9
 * version 1.0
 */

class TransferStaticNPC(id: String) : StaticNonPlayerCharacter(id) {

    lateinit var ip: String
    var port: Int = 0

    override fun load(): Boolean {
        var flag = false
        try {
            ip = (NPCConfig.transfer.get(this.id) as HashMap<String, Any>)["ip"] as String
            port = (NPCConfig.transfer.get(this.id) as HashMap<String, Any>)["port"] as Int
            flag = true
        } catch (ignored: Exception) {
        }

        return this.load(SNonPlayerCharacter.NPCType.Transfer) && flag
    }

    override fun save() {
        val map = this.baseMap()
        map.put("ip", ip)
        map.put("port", port)
        NPCConfig.transfer.set(this.id, map)
        NPCConfig.transfer.save()
    }

    override fun onTouch(player: Player) {
        player.transfer(InetSocketAddress.createUnresolved(ip, port))
    }

    override fun remove(): Boolean {
        NPCConfig.transfer.remove(this.id)
        NPCConfig.transfer.save()
        return true
    }
}
