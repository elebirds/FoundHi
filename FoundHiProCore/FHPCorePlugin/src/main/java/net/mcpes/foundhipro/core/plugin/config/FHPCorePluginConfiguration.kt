package net.mcpes.foundhipro.core.plugin.config

import cn.nukkit.Server
import net.mcpes.foundhipro.utils.cast
import net.mcpes.foundhipro.utils.config.Config
import java.io.File

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/6
 * version 1.0
 */
class FHPCorePluginConfiguration {
    val config: Config = Config(File(Server.getInstance().dataPath, "FHPCorePlugin.toml"))

    var enabled: Boolean = false
    var debug: Boolean = false

    var nukkitServerIP: String = "127.0.0.1"
    var nukkitServerPort: Int = 10086

    var serverID: String = ""
    var serverName: String = ""

    //var cloudKnapsackEnabled: Boolean = false
    //var cloudKnapsackID: String = ""

    var apiServerIP: String = "127.0.0.1"
    var apiServerPort: Int = 10086

    var connectSecretKey: String = ""

    init {
        this.load()
        instance = this
    }

    fun load() {
        val baseConfig = config.getSection("设置")
        val infoConfig = config.getSection("信息")
        this.serverID = infoConfig["ID"].cast()
        this.serverName = infoConfig["名称"].cast()
        this.nukkitServerIP = infoConfig["IP"].cast()
        this.nukkitServerPort = infoConfig["端口"].cast()
        val cloudKnapsackConfig = infoConfig.getSection("背包同步")
        //this.cloudKnapsackEnabled = cloudKnapsackConfig["启用"].cast()
        //this.cloudKnapsackID = cloudKnapsackConfig["ID"].cast()
        val developConfig = config.getSection("开发")
        this.debug = developConfig["调试模式"].cast()
        val networkConfig = config.getSection("网络")
        val apiServerConfig = networkConfig.getSection("API服务器")
        this.apiServerIP = apiServerConfig["IP"].cast()
        this.apiServerPort = apiServerConfig["端口"].cast()
        this.connectSecretKey = apiServerConfig["连接秘钥"].cast()
        this.enabled = baseConfig["启用"].cast()
    }

    companion object {
        lateinit var instance: FHPCorePluginConfiguration
    }
}