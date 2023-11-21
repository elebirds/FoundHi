package net.mcpes.foundhi.core.dao

import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.scheduler.AsyncTask
import java.util.*
import kotlin.collections.ArrayList


/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/27
 * version 1.0
 */
class DatabaseQueue {
    init {
        instance = this
        APIFunction.instance.scheduler.scheduleAsyncTask(DatabaseTaskQueue())
    }

    private val queue: Queue<ArrayList<Any>> = LinkedList()

    fun addTask(runnable: Runnable, params: ArrayList<Any>) {
        val p = ArrayList<Any>()
        p.add(runnable)
        p.addAll(params)
        this.queue.offer(p)
    }

    class DatabaseTaskQueue : AsyncTask() {
        override fun onRun() {
            while (APIFunction.instance.isRunning) {
                Thread.sleep(100)
                this.handle()
            }
            while (DatabaseQueue.instance.queue.isNotEmpty()) {
                this.handle()
            }
        }

        fun handle() {
            try {
                val element = DatabaseQueue.instance.queue.poll() ?: return
                val runnable = element[0] as Runnable
                runnable.run()
            } catch (e: Exception) {
                APIFunction.instance.logger.critical("数据库队列操作出现错误!", e)
            }
        }
    }

    companion object {
        lateinit var instance: DatabaseQueue
    }
}