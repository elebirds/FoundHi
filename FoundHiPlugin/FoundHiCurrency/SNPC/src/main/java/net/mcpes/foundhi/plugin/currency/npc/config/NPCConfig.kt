package net.mcpes.foundhi.plugin.currency.npc.config

import cn.nukkit.utils.Config
import net.mcpes.foundhi.plugin.currency.npc.SNonPlayerCharacter
import java.io.File

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/9
 * version 1.0
 */

object NPCConfig {
    @JvmStatic
    lateinit var command: Config
        private set
    @JvmStatic
    lateinit var reply: Config
        private set
    @JvmStatic
    lateinit var teleport: Config
        private set
    @JvmStatic
    lateinit var transfer: Config
        private set
    @JvmStatic
    lateinit var normal: Config
        private set

    @JvmStatic
    fun init() {
        val data = SNonPlayerCharacter.instance.dataFolder.toString()
        command = Config(File("$data/npcs/command.yml"), 2)
        reply = Config(File("$data/npcs/reply.yml"), 2)
        teleport = Config(File("$data/npcs/teleport.yml"), 2)
        transfer = Config(File("$data/npcs/transfer.yml"), 2)
        normal = Config(File("$data/npcs/normal.yml"), 2)
    }
}

