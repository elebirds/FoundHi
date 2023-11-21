package net.mcpes.foundhipro.core.app.manager

import io.netty.channel.Channel
import net.mcpes.foundhipro.core.app.data.server.Server
import net.mcpes.foundhipro.core.logger.Logger
import java.util.concurrent.ConcurrentHashMap

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/9
 * version 1.0
 */
object OverTimeManager {
    val overTime = ConcurrentHashMap<Channel, Long>()
    val overCount = ConcurrentHashMap<Channel, Int>()

    fun update(server: String) {
        this.update(ServerManager.getServer(server)!!)
    }

    fun update(server: Server) {
        this.update(server.channel)
    }

    fun update(channel: Channel) {
        overTime[channel] = System.currentTimeMillis()
        overCount[channel] = 0
    }

    fun loop() {
        overTime.keys.forEach {
            val time = overTime[it]!!
            val n = System.currentTimeMillis() - time
            if (n > 15 * 1000) {
                overCount[it] = overCount.getOrDefault(it, 0) + 1
                if (overCount[it]!! >= 3) {
                    for (server in ServerManager.getServers()) {
                        if (server.channel === it) {
                            Logger.instance.info("服务器" + server.name + "(id:" + server.id + ")连接超时,自动断开连接!")
                            server.shutdown(true)
                            continue
                        }
                    }
                } else {
                    overTime[it] = System.currentTimeMillis()
                }
            }
        }
    }

    fun removeServer(channel: Channel) {
        overTime.remove(channel)
        overCount.remove(channel)
    }
}