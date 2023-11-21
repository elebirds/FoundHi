package net.mcpes.foundhi.plugin.currency.login.data

import cn.nukkit.scheduler.AsyncTask
import net.mcpes.foundhi.core.utils.toDate
import net.mcpes.foundhi.plugin.currency.login.SLogin
import java.sql.SQLException
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/26
 * version 1.0
 */
class PlayerLoginCache(val name: String) {
    val loginDatabase: PlayerLoginDatabase = PlayerLoginDatabase(this.name)
    var password: String = ""
    var xuid: String = ""
    var cid: Long = 0
    var regDate: Date = Date()
    var lastDate: Date = Date()
    var lastIP: String = ""
    private val original = HashMap<String, Any>()
    private var loaded: Boolean = false

    init {
        try {
            SLogin.instance.server.scheduler.scheduleAsyncTask(SLogin.instance, PlayerLoginCacheLoadTask(this))
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    @Throws(SQLException::class)
    fun load() {

        if (this.loaded) return
        val data = this.loginDatabase.all
        if (data.isEmpty()) return
        this.password = data["pw"].toString()
        if (data["xbox"] == null) this.xuid = ""
        else this.xuid = data["xbox"].toString()
        this.cid = data["cid"] as Long
        this.regDate = data["regdate"].toString().toLong().toDate()
        this.lastDate = data["lastdate"].toString().toLong().toDate()
        this.lastIP = data["li"].toString()
        this.original["password"] = this.password
        this.original["lastIP"] = this.lastIP
        this.original["xuid"] = this.xuid
        this.loaded = true
    }

    fun exists(): Boolean {
        return loaded
    }

    fun save(async: Boolean) {
        if (async) {
            SLogin.instance.server.scheduler.scheduleAsyncTask(SLogin.instance, PlayerLoginCacheSaveTask(this))
        }
    }

    class PlayerLoginCacheLoadTask(private val cache: PlayerLoginCache) : AsyncTask() {
        override fun onRun() {
            try {
                cache.load()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    class PlayerLoginCacheSaveTask(private val cache: PlayerLoginCache) : AsyncTask() {
        override fun onRun() {
            try {
                if (!cache.exists()) return
                if (cache.lastIP != cache.original["lastIP"]) cache.loginDatabase.lastIP = cache.lastIP
                if (cache.password != cache.original["password"]) cache.loginDatabase.password = cache.password
                if (cache.xuid != cache.original["xuid"]) cache.loginDatabase.xboxID = cache.xuid
                cache.loginDatabase.clientID = cache.cid
                cache.loginDatabase.lastDate = cache.lastDate
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }
}
