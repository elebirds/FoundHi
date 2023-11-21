package net.mcpes.foundhipro.core.app.logger

import net.mcpes.foundhipro.core.app.CoreServer
import net.mcpes.foundhipro.core.logger.LoggerInterface
import net.mcpes.foundhipro.utils.font.ColorFormat
import net.mcpes.foundhipro.utils.font.FontAttribute
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.config.ConfigurationSource
import org.apache.logging.log4j.core.config.Configurator
import java.io.File
import java.io.FileInputStream
import java.io.PrintWriter
import java.io.StringWriter


/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/8/31
 * version 1.0
 *
 * 关于Log等级
 * ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF
 */
class ApplicationLogger(configPath: String) : LoggerInterface {
    private val realLogger: Logger

    init {
        val conFile = File("$configPath/log4j2.xml")
        val source = ConfigurationSource(FileInputStream(conFile), conFile)
        Configurator.initialize(null, source)
        realLogger = LogManager.getLogger("FHPCore")
    }

    private fun String.colorFormat(): String {
        return colorize(this)
    }

    private fun colorize(string: String): String {
        var str = string
        when {
            !str.contains(ColorFormat.ESCAPE) -> return str
            CoreServer.instance.ansiEnabled -> ColorFormat.replacements.values.forEach {
                str = str.replace("§${it.getCode()}", it.getANSIString())
            }
            else -> return ColorFormat.clean(str)
        }
        return str + FontAttribute.RESET.getANSIString()
    }

    /**
     * 输出每个严重的错误事件将会导致应用程序的退出的日志.
     * */
    override fun fatal(message: String) {
        realLogger.fatal(message.colorFormat())
    }

    /**
     * 输出错误信息日志
     * */
    override fun error(message: String) {
        realLogger.error(message.colorFormat())
    }

    /**
     * 输出错误信息日志
     * */
    override fun error(message: String, e: Throwable) {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        e.printStackTrace(printWriter)
        realLogger.error("$message\n$stringWriter".colorFormat())
    }

    /**
     * 输出警告
     * */
    override fun warn(message: String) {
        realLogger.warn(message.colorFormat())
    }

    /**
     * 输出错误信息日志
     * */
    override fun warn(message: String, e: Throwable) {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        e.printStackTrace(printWriter)
        realLogger.warn("$message\n$stringWriter".colorFormat())
    }

    /**
     * 追踪，就是程序推进一下.
     * */
    override fun trace(message: String) {
        realLogger.trace(message.colorFormat())
    }

    /**
     * 输出粗粒度级别上突出强调应用程序的运行过程.
     * */
    override fun info(message: String) {
        realLogger.info(message.colorFormat())
    }

    /**
     * 输出细粒度信息事件
     * 对调试应用程序是非常有帮助的.
     * */
    override fun debug(message: String) {
        realLogger.debug(message.colorFormat())
    }

    override fun logException(e: Exception) {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        e.printStackTrace(printWriter)
        realLogger.warn(stringWriter.toString().colorFormat())
    }
}