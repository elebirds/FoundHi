package net.mcpes.foundhipro.core.app.scheduler

import net.mcpes.foundhipro.core.logger.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Nukkit Project Team
 */
class ServerScheduler {
    private val asyncPool: AsyncPool

    private val taskMap: MutableMap<Int, TaskHandler>
    private val currentTaskId: AtomicInteger = AtomicInteger()

    @Volatile
    private var tick = -1

    init {
        this.taskMap = ConcurrentHashMap()
        this.asyncPool = AsyncPool(WORKERS)
    }

    fun scheduleTask(task: Runnable): TaskHandler {
        return addTask(task, 0, 0, false)
    }

    fun scheduleTask(task: Runnable, asynchronous: Boolean): TaskHandler {
        return addTask(task, 0, 0, asynchronous)
    }

    fun scheduleAsyncTask(task: AsyncTask): TaskHandler {
        return addTask(task, 0, 0, true)
    }

    fun scheduleDelayedTask(task: Runnable, delay: Int, asynchronous: Boolean = false): TaskHandler {
        return this.addTask(task, delay, 0, asynchronous)
    }

    fun scheduleDelayedTask(task: Runnable, time: Long, unit: TimeUnit, asynchronous: Boolean = false): TaskHandler {
        return this.scheduleDelayedTask(task, (unit.toSeconds(time).toInt() * 100), asynchronous)
    }

    fun scheduleRepeatingTask(task: Runnable, period: Int, asynchronous: Boolean = false): TaskHandler {
        return addTask(task, 0, period, asynchronous)
    }

    fun scheduleRepeatingTask(task: Runnable, time: Long, unit: TimeUnit, asynchronous: Boolean = false): TaskHandler {
        return this.scheduleRepeatingTask(task, (unit.toSeconds(time).toInt() * 100), asynchronous)
    }

    fun scheduleDelayedRepeatingTask(task: Runnable, delay: Int, period: Int, asynchronous: Boolean = false): TaskHandler {
        return addTask(task, delay, period, asynchronous)
    }

    fun scheduleDelayedRepeatingTask(task: Runnable, delay: Long, period: Long, unit: TimeUnit, asynchronous: Boolean = false): TaskHandler {
        return this.scheduleDelayedRepeatingTask(task, (unit.toSeconds(delay).toInt() * 100), (unit.toSeconds(period).toInt() * 100), asynchronous)
    }

    fun cancelTask(taskId: Int) {
        if (taskMap.containsKey(taskId)) {
            try {
                taskMap.remove(taskId)!!.cancel()
            } catch (ex: RuntimeException) {
                Logger.instance.error("在取消一个任务时遇到错误", ex)
            }
        }
    }

    fun cancelAllTasks() {
        this.taskMap.forEach {
            try {
                it.value.cancel()
            } catch (ex: RuntimeException) {
                Logger.instance.error("在取消一个任务时遇到错误", ex)
            }
        }
        this.taskMap.clear()
        this.currentTaskId.set(0)
    }

    fun isQueued(taskId: Int): Boolean {
        return this.taskMap.containsKey(taskId)
    }

    private fun addTask(task: Runnable, delay: Int, period: Int, asynchronous: Boolean): TaskHandler {
        if (delay < 0 || period < 0) {
            Logger.instance.error("尝试以负延迟或周期注册任务。")
        }
        val taskHandler = TaskHandler(task, nextTaskId(), asynchronous)
        taskHandler.delay = delay
        taskHandler.period = period
        taskHandler.nextRunTick = if (taskHandler.isDelayed) tick + taskHandler.delay else tick
        if (task is Task) {
            task.handler = taskHandler
        }
        taskMap[taskHandler.taskId] = taskHandler
        return taskHandler
    }

    fun mainThreadHeartbeat(currentTick: Int) {
        this.tick = currentTick
        runTasks(currentTick)
        AsyncTask.collectTask()
    }

    private fun runTasks(currentTick: Int) {
        taskMap.filter { it.value.nextRunTick <= tick }.forEach { id, handler ->
            when {
                handler.isCancelled -> {
                    this.cancelTask(id)
                }
                else -> {
                    if (handler.isAsynchronous) {
                        asyncPool.execute(handler.task)
                    } else {
                        try {
                            handler.run(currentTick)
                        } catch (e: Throwable) {
                            Logger.instance.error("无法执行任务处理程序${handler.taskId}", e)
                            Logger.instance.logException(e as? Exception ?: RuntimeException(e))
                        }
                    }
                    if (handler.isRepeating) {
                        handler.nextRunTick = currentTick + handler.period
                    } else {
                        this.cancelTask(id)
                    }
                }
            }
        }
    }

    private fun nextTaskId(): Int {
        return currentTaskId.incrementAndGet()
    }

    companion object {
        var WORKERS = 4
    }
}