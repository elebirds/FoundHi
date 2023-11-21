package net.mcpes.foundhipro.core.plugin.manager

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/9
 * version 1.0
 */
object OverTimeManager {
    var overTime: Long = 0
    var overCount: Int = 0

    fun updateOverTime() {
        this.overTime = System.currentTimeMillis()
        this.overCount = 0
    }
}