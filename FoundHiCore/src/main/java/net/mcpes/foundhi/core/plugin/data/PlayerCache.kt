package net.mcpes.foundhi.core.plugin.data

import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.app.data.player.Player.Companion.KICK_TIME
import net.mcpes.foundhi.core.app.data.player.PlayerPreferenceSettings
import net.mcpes.foundhi.core.network.protocol.player.PlayerQuitPacket
import net.mcpes.foundhi.core.plugin.FoundHiPlugin

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/28
 * version 1.0
 */
data class PlayerCache(
        val name: String,
        var permission: Player.Permission,
        var money1: Double,
        var money2: Double,
        var money3: Double,
        val preferenceSettings: PlayerPreferenceSettings,
        var guild: Int,
        var nameColor: String,
        var chatColor: String,
        var titling: Int,
        var title: ArrayList<String>,
        var lover: String,
        var contribution: Int,
        var invitation: HashMap<String, Int> = HashMap(),
        var marry: ArrayList<String>,
        var inbox: HashMap<MailCache, Int>,
        var outbox: HashMap<MailCache, Int>,
        var friends: HashMap<String, String>,
        var head: Int,
        var autograph: String,
        var apply: HashMap<String, Int>,
        var inTeam: Int) {
    private var kick = KICK_TIME

    fun resetKick() {
        this.kick = KICK_TIME
    }

    fun kick() {
        this.kick--
        if (this.kick <= 0) {
            FoundHiPlugin.instance.delWillJoin(name)
            FoundHiPlugin.instance.willKick.remove(name)
            val pk = PlayerQuitPacket()
            pk.id = name
            FoundHiPlugin.instance.dataPacket(pk)
            FoundHiPlugin.instance.delPlayer(this.name)
        }
    }
}
