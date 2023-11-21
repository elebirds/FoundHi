package net.mcpes.foundhipro.core.logger

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/5 下午9:45
 * version 1.0
 */
interface LoggerInterface {
    /**
     * 输出每个严重的错误事件将会导致应用程序的退出的日志.
     * */
    fun fatal(message: String)

    /**
     * 输出错误信息日志
     * */
    fun error(message: String)

    /**
     * 输出错误信息日志
     * */
    fun error(message: String, e: Throwable)

    /**
     * 输出警告
     * */
    fun warn(message: String)

    /**
     * 输出错误信息日志
     * */
    fun warn(message: String, e: Throwable)

    /**
     * 追踪，就是程序推进一下.
     * */
    fun trace(message: String)

    /**
     * 输出粗粒度级别上突出强调应用程序的运行过程.
     * */
    fun info(message: String)

    /**
     * 输出细粒度信息事件
     * 对调试应用程序是非常有帮助的.
     * */
    fun debug(message: String)

    fun logException(e: Exception)
}