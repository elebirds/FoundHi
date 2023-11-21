package net.mcpes.foundhi.plugin.currency.npc

import net.mcpes.foundhi.core.plugin.modal.FoundHiPluginBase
import net.mcpes.foundhi.plugin.currency.npc.command.StaticNPCCommand
import net.mcpes.foundhi.plugin.currency.npc.config.NPCConfig
import net.mcpes.foundhi.plugin.currency.npc.listener.StaticNPCListener
import net.mcpes.foundhi.plugin.currency.npc.npc.NonPlayerCharacter
import net.mcpes.foundhi.plugin.currency.npc.npc.static.*
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/9
 * version 1.0
 */
class SNonPlayerCharacter : FoundHiPluginBase() {
    init {
        instance = this
    }

    override fun onLoad() {
        NPCConfig.init()
    }

    override fun onEnable() {
        this.server.commandMap.register("snpc", StaticNPCCommand("snpc"))
        this.server.pluginManager.registerEvents(StaticNPCListener(), this)
        this.loadNPCs()
    }

    override fun onDisable() {

    }

    private fun loadNPCs() {
        this.server.logger.info(TITLE + "加载NPC中....")
        var all = 0
        var command = 0
        var reply = 0
        var teleport = 0
        var transfer = 0
        var normal = 0
        for (key in NPCConfig.command.all.keys) {
            if (this.loadNPC(key, NPCType.Command)) {
                all++
                command++
                lists.add(key)
            } else {
                this.server.logger.info(TITLE + "加载Command型NPC:" + key + "失败!")
            }
        }
        for (key in NPCConfig.reply.all.keys) {
            if (this.loadNPC(key, NPCType.Reply)) {
                all++
                reply++
                lists.add(key)
            } else {
                this.server.logger.info(TITLE + "加载Reply型NPC:" + key + "失败!")
            }
        }
        for (key in NPCConfig.teleport.all.keys) {
            if (this.loadNPC(key, NPCType.Teleport)) {
                all++
                teleport++
                lists.add(key)
            } else {
                this.server.logger.info(TITLE + "加载Teleport型NPC:" + key + "失败!")
            }
        }
        for (key in NPCConfig.transfer.all.keys) {
            if (this.loadNPC(key, NPCType.Transfer)) {
                all++
                transfer++
                lists.add(key)
            } else {
                this.server.logger.info(TITLE + "加载Transfer型NPC:" + key + "失败!")
            }
        }
        for (key in NPCConfig.normal.all.keys) {
            if (this.loadNPC(key, NPCType.Normal)) {
                all++
                normal++
                lists.add(key)
            } else {
                this.server.logger.info(TITLE + "加载Normal型NPC:" + key + "失败!")
            }
        }
        this.server.logger.info(TITLE + "加载完成!共计加载" + all + "个NPC.其中Command型" + command + "个,Reply型" + reply + "个,Teleport型" + teleport + "个,Transfer型" + transfer + "个,Normal型" + normal + "个.")
    }

    fun loadNPC(key: String, type: NPCType): Boolean {
        when (type) {
            NPCType.Reply -> {
                val reply = ReplyStaticNPC(key)
                return reply.load()
            }
            NPCType.Command -> {
                val command = CommandStaticNPC(key)
                return command.load()
            }
            NPCType.Teleport -> {
                val teleport = TeleportStaticNPC(key)
                return teleport.load()
            }
            NPCType.Transfer -> {
                val transfer = TransferStaticNPC(key)
                return transfer.load()
            }
            NPCType.Normal -> {
                val normal = NormalStaticNPC(key)
                return normal.load()
            }
            else -> return false
        }
    }

    companion object {
        @JvmStatic
        val lists: ArrayList<String> = ArrayList()
        @JvmStatic
        var TITLE = "§l§7>>§3SNPC§7<< §6"
        @JvmStatic
        var npcs: HashMap<String, NonPlayerCharacter> = HashMap()
        @JvmStatic
        lateinit var instance: SNonPlayerCharacter
            private set
    }

    enum class NPCType {
        Command, Reply, Teleport, Transfer, Normal
    }
}