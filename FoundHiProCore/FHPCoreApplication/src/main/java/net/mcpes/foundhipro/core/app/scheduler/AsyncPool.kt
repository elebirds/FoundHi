package net.mcpes.foundhipro.core.app.scheduler

import net.mcpes.foundhipro.core.logger.Logger

import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class AsyncPool(size: Int) : ThreadPoolExecutor(size, Integer.MAX_VALUE, 60, TimeUnit.MILLISECONDS, SynchronousQueue()) {
    init {
        this.setThreadFactory { runnable ->
            object : Thread(runnable) {
                init {
                    isDaemon = true
                    name = "FoundHiPro 异步任务处理程序 #$poolSize"
                }
            }
        }
    }

    override fun afterExecute(runnable: Runnable, throwable: Throwable) {
        Logger.instance.error("在处理异步任务中发生了异常", throwable)
    }
}