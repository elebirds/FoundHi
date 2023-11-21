package net.mcpes.foundhi.plugin.currency.npc.npc.static

import cn.nukkit.Player
import net.mcpes.foundhi.plugin.currency.npc.SNonPlayerCharacter
import net.mcpes.foundhi.plugin.currency.npc.config.NPCConfig

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/9
 * version 1.0
 */
class NormalStaticNPC(id: String) : StaticNonPlayerCharacter(id) {
    override fun load(): Boolean {
        return this.load(SNonPlayerCharacter.NPCType.Normal)
    }

    override fun save() {
        NPCConfig.normal.set(this.id, this.baseMap())
        NPCConfig.normal.save()
    }

    override fun onTouch(player: Player) {
        return
    }

    override fun remove(): Boolean {
        NPCConfig.normal.remove(this.id)
        NPCConfig.normal.save()
        return true
    }
}