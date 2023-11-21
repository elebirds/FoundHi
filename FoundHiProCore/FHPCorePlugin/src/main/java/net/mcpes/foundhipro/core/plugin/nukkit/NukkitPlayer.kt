package net.mcpes.foundhipro.core.plugin.nukkit

import cn.nukkit.Player
import cn.nukkit.network.SourceInterface
import cn.nukkit.utils.TextFormat

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/23
 * version 1.0
 */
class NukkitPlayer(interfaz: SourceInterface, clientID: Long?, ip: String, port: Int) : Player(interfaz, clientID, ip, port) {

    override fun sendMessage(message: String) {
        super.sendMessage(TextFormat.colorize(message))
    }

}