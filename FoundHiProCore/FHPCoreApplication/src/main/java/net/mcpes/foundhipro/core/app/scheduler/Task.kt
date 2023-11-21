package net.mcpes.foundhipro.core.app.scheduler

import net.mcpes.foundhipro.core.app.CoreServer

abstract class Task : Runnable {
    var handler: TaskHandler? = null
        set(taskHandler) {
            if (this.handler == null || taskHandler == null) {
                field = taskHandler
            }
        }

    val taskId: Int
        get() = if (this.handler != null) this.handler!!.taskId else -1

    abstract fun onRun(currentTick: Int)

    final override fun run() {
        this.onRun(handler!!.lastRunTick)
    }

    fun onCancel() {

    }

    fun cancel() {
        try {
            this.handler!!.cancel()
        } catch (ex: RuntimeException) {
            CoreServer.instance.logger.error("任务:${taskId}在取消时遇到了一个错误!", ex)
        }
    }
}
