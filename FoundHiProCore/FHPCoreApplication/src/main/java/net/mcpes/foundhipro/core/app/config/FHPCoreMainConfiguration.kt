package net.mcpes.foundhipro.core.app.config

import net.mcpes.foundhipro.core.app.CoreServer
import net.mcpes.foundhipro.utils.cast
import net.mcpes.foundhipro.utils.config.Config
import java.io.File

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/3
 * version 1.0
 */
class FHPCoreMainConfiguration(server: CoreServer) {
    val config: Config = Config(File(server.configPath.toFile().absoluteFile, "FHPCoreMain.yml"), Config.ConfigType.YAML)
    var debug: Boolean = false
    var listenPort: Int = 10086
    var connectSecretKey: String = ""
    var maxPlayers: Int = 100

    init {
        this.load()
    }

    fun load() {
        val baseConfig = config.getSection("设置")
        this.maxPlayers = baseConfig["人数限制"].cast()
        val developConfig = config.getSection("开发")
        this.debug = developConfig["调试模式"].cast()
        val networkConfig = config.getSection("网络")
        this.listenPort = networkConfig["监听端口"].cast()
        this.connectSecretKey = networkConfig["连接秘钥"].cast()
    }
}