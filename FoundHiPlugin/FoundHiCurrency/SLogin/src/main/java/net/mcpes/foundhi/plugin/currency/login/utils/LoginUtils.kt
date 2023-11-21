package net.mcpes.foundhi.plugin.currency.login.utils

import cn.nukkit.Player
import cn.nukkit.Server
import net.mcpes.foundhi.core.plugin.event.PlayerLoginEvent
import net.mcpes.foundhi.plugin.currency.login.SLogin
import net.mcpes.foundhi.plugin.currency.login.config.MainConfig
import net.mcpes.foundhi.plugin.currency.login.data.PlayerLoginCache
import java.sql.SQLException
import java.util.*
import java.util.concurrent.ExecutionException

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/26
 * version 1.0
 */
object LoginUtils {
    //0免登录，-2滚去注册，-1 sql错误，-3cid和xuid都不匹配并没两小时免登录，1 cid相同,2 xuid相同,-4已经登陆
    @JvmStatic
    fun login(player: Player): Int {
        val name = player.name
        if (SLogin.instance.logged.contains(name)) return -4
        val data: PlayerLoginCache
        try {
            data = if (!SLogin.instance.pData.containsKey(name)) {
                val cache = PlayerLoginCache(name)
                SLogin.instance.pData[name] = cache
                cache
            } else {
                SLogin.instance.pData[name]!!
            }
            if (!data.exists()) return -2
            if (SLogin.instance.freeLogin.containsKey(name)) {
                if (data.lastIP != player.address) {
                    SLogin.instance.freeLogin.remove(name)
                } else {
                    SLogin.instance.pData.put(name, data)
                    SLogin.instance.logged.add(name)
                    SLogin.instance.notLogin.remove(name)
                    SLogin.instance.freeLogin.remove(name)
                    data.lastDate = Date()
                    data.load()
                    val event = PlayerLoginEvent(player)
                    Server.getInstance().pluginManager.callEvent(event)
                    return 0
                }
            }
            val time = System.currentTimeMillis()
            if (time - data.lastDate.time < MainConfig.free_login_time) {
                if (data.lastIP != player.address) {
                    SLogin.instance.freeLogin.remove(name)
                } else {
                    SLogin.instance.pData[name] = data
                    SLogin.instance.logged.add(name)
                    SLogin.instance.notLogin.remove(name)
                    SLogin.instance.freeLogin.remove(name)
                    data.lastDate = Date()
                    data.load()
                    val event = PlayerLoginEvent(player)
                    Server.getInstance().pluginManager.callEvent(event)
                    return 0
                }
            }
            if (data.cid == player.loginChainData.clientId) {
                SLogin.instance.pData[name] = data
                SLogin.instance.logged.add(name)
                SLogin.instance.notLogin.remove(name)
                data.lastDate = Date()
                data.load()
                val event = PlayerLoginEvent(player)
                Server.getInstance().pluginManager.callEvent(event)
                return 1
            }
            if (data.xuid == player.loginChainData.xuid) {
                SLogin.instance.pData[name] = data
                SLogin.instance.logged.add(name)
                SLogin.instance.notLogin.remove(name)
                data.lastDate = Date()
                data.load()
                val event = PlayerLoginEvent(player)
                Server.getInstance().pluginManager.callEvent(event)
                return 2
            }
            return -3
        } catch (e: SQLException) {
            e.printStackTrace()
            return -1
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return -1
        } catch (e: ExecutionException) {
            e.printStackTrace()
            return -1
        }
    }

    //0成功,-1密码不对,-2滚去注册,-3sql错误,-4已经登陆
    @JvmStatic
    fun login(player: Player, password: String): Int {
        val name = player.name
        if (SLogin.instance.logged.contains(name)) return -4
        val data: PlayerLoginCache
        try {
            data = if (!SLogin.instance.pData.containsKey(name)) {
                val cache = PlayerLoginCache(name)
                SLogin.instance.pData[name] = cache
                cache
            } else {
                SLogin.instance.pData[name]!!
            }
            if (!data.exists()) return -2
            if (data.password == password) {
                SLogin.instance.notLogin.remove(name)
                SLogin.instance.logged.add(name)
                try {
                    data.load()
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                data.lastDate = Date()
                SLogin.instance.pData[name] = data
                val event = PlayerLoginEvent(player)
                Server.getInstance().pluginManager.callEvent(event)
                return 0
            } else {
                return -1
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            return -3
        }

    }

    @JvmStatic
    fun register(player: Player, password: String, cid: Boolean, xuid: Boolean): Int {
        val data: PlayerLoginCache
        try {
            val name = player.name
            data = if (!SLogin.instance.pData.containsKey(name)) {
                val cache = PlayerLoginCache(name)
                SLogin.instance.pData[name] = cache
                cache
            } else {
                SLogin.instance.pData[name]!!
            }
            if (data.exists()) {
                return 1
            }
            data.loginDatabase.addPlayer(password, Date(), player.address)
            if (cid) data.loginDatabase.clientID = player.loginChainData.clientId
            if (xuid && player.loginChainData.xuid != null && player.loginChainData.xuid != "")
                data.loginDatabase.xboxID = player.loginChainData.xuid
            SLogin.instance.notLogin.remove(name)
            SLogin.instance.logged.add(name)
            data.load()
            SLogin.instance.pData.put(name, data)
            val event = PlayerLoginEvent(player)
            Server.getInstance().pluginManager.callEvent(event)
            return 0
        } catch (e: SQLException) {
            e.printStackTrace()
            return -1
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return -1
        } catch (e: ExecutionException) {
            e.printStackTrace()
            return -1
        }

    }
}