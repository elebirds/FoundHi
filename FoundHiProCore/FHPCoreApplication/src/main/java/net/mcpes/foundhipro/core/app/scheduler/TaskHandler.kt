package net.mcpes.foundhipro.core.app.scheduler

import net.mcpes.foundhipro.core.app.CoreServer

class TaskHandler(val task: Runnable, val taskId: Int, val isAsynchronous: Boolean) {
    var delay: Int = 0
    var period: Int = 0

    var lastRunTick: Int = 0
    var nextRunTick: Int = 0

    var isCancelled: Boolean = false
        private set

    val isDelayed: Boolean
        get() = this.delay > 0

    val isRepeating: Boolean
        get() = this.period > 0

    fun cancel() {
        if (!this.isCancelled && this.task is Task) {
            this.task.onCancel()
        }
        this.isCancelled = true
    }

    fun run(currentTick: Int) {
        try {
            lastRunTick = currentTick
            task.run()
        } catch (ex: RuntimeException) {
            CoreServer.instance.logger.error("任务:${taskId}在运行时遇到了一个错误!", ex)
        }
    }
}
