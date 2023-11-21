package net.mcpes.foundhipro.core.app.bootstrap

import net.mcpes.foundhipro.core.ServerType
import net.mcpes.foundhipro.core.app.CoreServer
import net.mcpes.foundhipro.utils.file.FileOtherUtils

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/8/31 下午2:12
 * version 1.0
 */
object FoundHiProCoreBootstrap {
    @JvmStatic
    val START_TIME = System.currentTimeMillis()
    @JvmStatic
    val VERSION = "0.0.1-DEV"
    @JvmStatic
    val CODENAME = "Powerful"
    @JvmStatic
    val TITLE = "§4Found§6Hi§2Pro§bCore§f"

    @JvmStatic
    fun main(strings: Array<String>) {
        ServerType.init(ServerType.ServerType.API)
        val server: CoreServer
        val parameters = this.loadParameters(strings)
        FileOtherUtils.saveResource(parameters["cp"].toString(), "log4j2.xml")
        try {
            server = CoreServer(parameters["path"].toString(), parameters["dp"].toString(), parameters["cp"].toString(), parameters["ansi"] as Boolean)
        } catch (e: Exception) {
            throw AssertionError("无法初始化CoreServer类", e)
        }
        try {
            server.boot()
        } catch (e: Exception) {
            throw RuntimeException("在${TITLE}运行时发生了一个错误!", e)
        }
    }

    private fun loadParameters(strings: Array<String>): HashMap<String, Any> {
        var ansi = true
        val path = System.getProperty("user.dir")
        var dataPath = path
        var configPath = path
        strings.forEach {
            when (it) {
                "disable-ansi" -> ansi = false
                else -> {
                    if (it.startsWith("data-path=")) {
                        dataPath = it.substring(11, it.length)
                    } else if (it.startsWith("config-path=")) {
                        configPath = it.substring(12, it.length)
                    }
                }
            }
        }
        return hashMapOf(
                "ansi" to ansi,
                "path" to path,
                "dp" to dataPath,
                "cp" to configPath
        )
    }
}