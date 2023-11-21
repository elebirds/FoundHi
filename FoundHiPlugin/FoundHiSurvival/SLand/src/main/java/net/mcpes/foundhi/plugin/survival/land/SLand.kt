package net.mcpes.foundhi.plugin.survival.land

import cn.nukkit.Server
import cn.nukkit.level.generator.Generator
import cn.nukkit.scheduler.AsyncTask
import cn.nukkit.utils.Config
import net.mcpes.foundhi.core.plugin.modal.FoundHiPluginBase
import net.mcpes.foundhi.core.utils.cast
import net.mcpes.foundhi.plugin.survival.land.command.LandCommand
import net.mcpes.foundhi.plugin.survival.land.generate.ContractibleLand
import net.mcpes.foundhi.plugin.survival.land.generate.ExpandLand
import net.mcpes.foundhi.plugin.survival.land.land.LandWorld
import net.mcpes.foundhi.plugin.survival.land.listener.LandBlockListener
import net.mcpes.foundhi.plugin.survival.land.listener.PlayerListener
import net.mcpes.foundhi.plugin.survival.land.land.Land as LandData

/**
 * SLand
 *
 * 此项目部分由 SimpleLand 改写
 *
 * @author hhm Copyright (c) 2018/5/1
 * version 1.0
 */

class SLand : FoundHiPluginBase() {
    private lateinit var generateConfig: Config

    init {
        init(this)
    }

    companion object {
        const val TITLE: String = ""
        lateinit var instance: SLand
        val lands: HashMap<String, net.mcpes.foundhi.plugin.survival.land.land.Land> = HashMap()
        val landWorlds: HashMap<Int, LandWorld> = HashMap()
        val landWorldCorresponding: HashMap<String, Int> = HashMap()
        val phLands: HashMap<String, ArrayList<LandData>> = HashMap()
        val pgLands: HashMap<String, ArrayList<LandData>> = HashMap()
        val defaultPermissions: HashMap<LandData.Companion.PlayerType, HashMap<String, Boolean>> = HashMap()

        fun init(instance: SLand) {
            SLand.instance = instance
            val normal = HashMap<String, Boolean>()
            normal["build"] = false
            normal["chest"] = false
            normal["sign"] = true
            defaultPermissions[net.mcpes.foundhi.plugin.survival.land.land.Land.Companion.PlayerType.NORMAL] = normal
            val guest = HashMap<String, Boolean>()
            guest["build"] = true
            guest["chest"] = true
            guest["sign"] = true
            defaultPermissions[net.mcpes.foundhi.plugin.survival.land.land.Land.Companion.PlayerType.GUEST] = guest
        }

        fun save() {
            landWorlds.forEach { _, u -> u.save() }
        }
    }

    override fun onLoad() {
        this.initConfig()
    }

    override fun onEnable() {
        this.initGenerate()
        LandWorld.init()
        this.server.commandMap.register("SLand", LandCommand("land"))
        this.server.pluginManager.registerEvents(PlayerListener(), this)
        this.server.pluginManager.registerEvents(LandBlockListener(), this)
        this.server.scheduler.scheduleAsyncTask(this, object : AsyncTask() {
            override fun onRun() {
                while (Server.getInstance().isRunning) {
                    Thread.sleep(1200000)
                    SLand.save()
                }
            }
        })
    }

    override fun onDisable() {
        SLand.save()
    }

    private fun initConfig() {
        this.saveResource("generate.yml")
        this.generateConfig = Config(this.dataFolder.toString() + "/generate.yml", 2)
    }

    private fun initGenerate() {
        ExpandLand.init(this.generateConfig["ExpandLand"].cast())
        ContractibleLand.init(this.generateConfig["ContractibleLand"].cast())
        Generator.addGenerator((ContractibleLand::class.java) as Class<out Generator>, "ContractibleLand", 5)
        Generator.addGenerator((ExpandLand::class.java) as Class<out Generator>, "ExpandLand", 6)
    }
}