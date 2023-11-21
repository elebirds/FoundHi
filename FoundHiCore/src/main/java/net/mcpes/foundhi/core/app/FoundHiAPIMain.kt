package net.mcpes.foundhi.core.app

import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.utils.MainLogger

/**
 * FoundHi的启动类 包含`main`函数。<br></br>
 *
 * @author hhm Copyright (c) 2018/4/10
 * @version 1.0
 * @since FoundHi Dev Version
 */

object FoundHiAPIMain {
    const val VERSION = "1.0.0-alpha.2"
    const val CODENAME = "Dalao"
    val PATH = System.getProperty("user.dir") + "/"
    val DATA_PATH = System.getProperty("user.dir") + "/"
    val START_TIME = System.currentTimeMillis()
    var ANSI = true

    /**
     * FoundHi Main方法
     *
     * @author hhm
     */
    @JvmStatic
    fun main(strings: Array<String>) {
        ServerType.init(ServerType.API)
        for (string in strings) {
            when (string) {
                "disable-ansi" -> ANSI = false
            }
        }
        System.setProperty("jline.WindowsTerminal.directConsole", "false")
        val logger = MainLogger()
        try {
            APIFunction(logger, PATH, DATA_PATH)
        } catch (e: Exception) {
            logger.logException(e)
        }
    }
}
