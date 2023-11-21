package net.mcpes.foundhipro.core.app.scheduler

import net.mcpes.foundhipro.core.app.CoreServer
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

abstract class AsyncTask : Runnable {
    var result: Any? = null
    var taskId: Int = 0
    var isFinished = false
        private set

    override fun run() {
        this.result = null
        this.onRun()
        this.isFinished = true
        FINISHED_LIST.offer(this)
    }

    abstract fun onRun()

    open fun onCompletion() {

    }

    companion object {
        val FINISHED_LIST: Queue<AsyncTask> = ConcurrentLinkedQueue()

        fun collectTask() {
            while (!FINISHED_LIST.isEmpty()) {
                val task = FINISHED_LIST.poll()
                try {
                    assert(task != null)
                    task!!.onCompletion()
                } catch (e: Exception) {
                    CoreServer.instance.logger.error("在执行异步任务时发生错误 ", e)
                }
            }
        }
    }
}
