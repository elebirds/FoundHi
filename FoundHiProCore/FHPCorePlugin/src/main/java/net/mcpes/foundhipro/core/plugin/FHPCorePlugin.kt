package net.mcpes.foundhipro.core.plugin

import cn.nukkit.Server
import cn.nukkit.plugin.PluginBase
import net.mcpes.foundhipro.core.db.FHPDatabaseLauncher
import net.mcpes.foundhipro.core.db.io.FHPBaseIO
import net.mcpes.foundhipro.core.plugin.config.FHPCorePluginConfiguration
import net.mcpes.foundhipro.core.plugin.network.PluginNetwork
import net.mcpes.foundhipro.utils.file.FileOtherUtils

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/5 下午8:59
 * version 1.0
 */
class FHPCorePlugin : PluginBase() {
    lateinit var config: FHPCorePluginConfiguration
    lateinit var network: PluginNetwork
    lateinit var databaseLauncher: FHPDatabaseLauncher
    lateinit var dao: FHPBaseIO
    var proved: Boolean = false


    override fun onLoad() {
        FileOtherUtils.saveResource(Server.getInstance().dataPath, "FHPCorePlugin.toml", "FHPCorePlugin.toml", false, this.javaClass.classLoader)
        this.config = FHPCorePluginConfiguration()
    }

    override fun onEnable() {
        this.network = PluginNetwork()
        this.databaseLauncher = FHPDatabaseLauncher
                .Builder()
                .setIsAPI(true)
                .build()
        this.databaseLauncher.launch()
        this.dao = databaseLauncher.dao
    }

    init {
        instance = this
    }

    companion object {
        lateinit var instance: FHPCorePlugin
        var isRunning: Boolean = true
    }
}