package net.mcpes.foundhipro.core.db.queue

import net.mcpes.foundhipro.core.db.io.FHPMySQLIO
import net.mcpes.foundhipro.core.logger.Logger

/**
 * FoundHiPro
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class SQLQueueThread(private val dao: FHPMySQLIO) : Runnable {
    override fun run() {
        while (true) {
            if (!dao.sqlQueue.isEmpty()) {
                if (dao.currentRequest < dao.maxRequest) {
                    val pair = dao.sqlQueue.poll()
                    val action = pair.first
                    val dbCallback = pair.second
                    if (dao.launcher.isAPI) {
                        /*
                        /

                        CoreServer.instance.scheduler.scheduleAsyncTask(object : AsyncTask() {
                            override fun onRun() {
                                dao.currentRequest++
                                try {
                                    action.execute()
                                } catch (e : Exception) {
                                    dao.sqlQueue.offer(pair)
                                }
                            }

                            override fun onCompletion() {
                                action.callback(dbCallback)
                                dao.currentRequest--
                            }
                        })                         */
                    } else {
                        /*
                        *
                        Server.getInstance().scheduler.scheduleAsyncTask(object : cn.nukkit.scheduler.AsyncTask() {
                            override fun onRun() {
                                dao.currentRequest++
                                try {
                                    action.execute()
                                } catch (e : Exception) {
                                    dao.sqlQueue.offer(pair)
                                }
                            }

                            override fun onCompletion(server: Server) {
                                action.callback(dbCallback)
                                dao.currentRequest--
                            }

                        })
                        * */
                    }
                } else {
                    Logger.instance.warn("当前请求数量已经超过最大预设!!")
                    Thread.sleep(100)
                }
            }
        }
    }

}