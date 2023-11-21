package net.mcpes.foundhipro.core.logger

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/5
 * version 1.0
 *
 * 关于Log等级
 * ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF
 */
class Logger(private val realLogger: LoggerInterface) : LoggerInterface by realLogger {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: Logger
    }
}