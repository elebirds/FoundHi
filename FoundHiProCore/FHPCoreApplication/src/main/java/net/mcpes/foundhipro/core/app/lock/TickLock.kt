package net.mcpes.foundhipro.core.app.lock

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/25
 * version 1.0
 */
class TickLock {
    private var nextTick: Long = 0
    var tick: Int = 0

    fun loop(): Boolean {
        val tickTime = System.currentTimeMillis()
        if (tickTime - this.nextTick < -5) {
            return false
        }
        this.tick++
        if (this.nextTick - tickTime < -1000) {
            this.nextTick = tickTime
        } else {
            this.nextTick += 10
        }
        return true
    }

    fun lock() {
        Thread.sleep(1)
    }
}