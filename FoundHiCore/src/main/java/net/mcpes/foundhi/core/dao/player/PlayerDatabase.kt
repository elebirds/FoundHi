package net.mcpes.foundhi.core.dao.player

import com.alibaba.druid.pool.DruidPooledCallableStatement
import com.alibaba.druid.pool.DruidPooledResultSet
import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.data.mail.Mail
import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.dao.Database
import net.mcpes.foundhi.core.dao.DatabaseQueue
import net.mcpes.foundhi.core.utils.cast
import net.mcpes.foundhi.core.utils.getPlayerPermission
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import java.sql.SQLException

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/10
 * version 1.0
 */
class PlayerDatabase @Throws(SQLException::class)
constructor(private val player: Player) {
    val name = player.name
    val baseDatabase: PlayerBaseDatabase = PlayerBaseDatabase(name)
    val guildDatabase: PlayerGuildDatabase = PlayerGuildDatabase(name)
    val mailDatabase: PlayerMailDatabase = PlayerMailDatabase(name)
    val friendsDatabase: PlayerFriendsDatabase = PlayerFriendsDatabase(name)

    init {
        this.init()
    }

    fun init() {
        DatabaseQueue.instance.addTask(Runnable {
            try {
                val flag = (!baseDatabase.exists()) || (!guildDatabase.exists()) || (!mailDatabase.exists()) || (!friendsDatabase.exists())
                if (flag) {
                    this.addPlayer()
                }
                val cache = if (Database.isEnableRedis()) {
                    this.getCache()
                } else {
                    HashMap()
                }
                val data = if (cache.isNotEmpty()) {
                    cache
                } else {
                    val r = player.database.getAllData()
                    player.database.setRedisData(r)
                    r
                }
                player.permission = data["p"].toString().toInt().getPlayerPermission()
                player.money1 = data["m1"].toString().toDouble()
                player.money2 = data["m2"].toString().toDouble()
                player.money3 = data["m3"].toString().toDouble()
                JSONObject.fromObject(data["b"]).forEach { o, o2 ->
                    val values = o2 as JSONArray
                    val realValue = ByteArray(values.size)
                    for (i in values.indices) {
                        realValue[i] = values.getInt(i).toByte()
                    }
                    player.bags[o.toString()] = realValue
                }
                player.preference.load(data["pre"].toString())
                player.guild = data["g"].toString().toInt()
                val marry = ArrayList<String>()
                JSONArray.fromObject(data["ma"]).forEach { marry.add(it.toString()) }
                player.marry = marry
                player.nameColor = data["nc"].toString()
                player.chatColor = data["cc"].toString()
                player.contribution = data["co"].toString().toInt()
                val title: ArrayList<String> = arrayListOf()
                JSONArray.fromObject(data["ti"]).forEach { title.add(it.toString()) }
                player.title = title
                player.titling = data["ut"].toString().toInt()
                val invitationMessage = HashMap<String, Int>()
                JSONObject.fromObject(data["inv"].cast()).forEach { t, u -> invitationMessage[t.toString()] = u.toString().toInt() }
                player.invitation = invitationMessage
                player.lover = data["lo"].toString()
                JSONObject.fromObject(data["in"].cast()).forEach { k, v ->
                    if (k is String) {
                        val time: Int = v.cast()
                        if (!APIFunction.instance.hasMail(k)) {
                            val mail = Mail(time, k)
                            APIFunction.instance.addMail(mail)
                            player.mailInbox[mail] = time
                        } else {
                            player.mailInbox[APIFunction.instance.getMail(k)!!] = time
                        }
                    }
                }
                JSONObject.fromObject(data["ou"].cast()).forEach { k, v ->
                    if (k is String) {
                        val time: Int = v.cast()
                        if (!APIFunction.instance.hasMail(k)) {
                            val mail = Mail(time, k)
                            APIFunction.instance.addMail(mail)
                            player.mailOutbox[mail] = time
                        } else {
                            player.mailOutbox[APIFunction.instance.getMail(k)!!] = time
                        }
                    }
                }
                val apply: HashMap<String, Int> = HashMap()
                val friends: HashMap<String, String> = HashMap()
                JSONObject.fromObject(data["fr"]).forEach { t, u -> friends[t.toString()] = u.toString() }
                JSONObject.fromObject(data["ap"]).forEach { t, u -> apply[t.toString()] = u.toString().toInt() }
                player.friends = friends
                player.apply = apply
                player.head = data["he"].toString().toInt()
                player.autograph = data["ag"].toString()
                player.original["p"] = player.permission
                player.original["1"] = player.money1
                player.original["2"] = player.money2
                player.original["3"] = player.money3
                player.original["invitation"] = player.invitation.clone()
                player.original["title"] = player.title
                player.original["titling"] = player.titling
                player.original["lover"] = player.lover
                player.original["contribution"] = player.contribution
                player.original["ct"] = player.chatColor
                player.original["nt"] = player.nameColor
                player.original["guild"] = player.guild
                player.original["marry"] = player.marry
                player.original["head"] = player.head
                player.original["friends"] = player.friends.clone()
                player.original["ag"] = player.autograph
                player.original["apply"] = player.apply.clone()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }, arrayListOf())
    }

    fun addPlayer() {
        baseDatabase.addPlayer()
        guildDatabase.addPlayer()
        mailDatabase.addPlayer()
        friendsDatabase.addPlayer()
    }

    fun delPlayer() {
        baseDatabase.delPlayer()
        guildDatabase.delPlayer()
        mailDatabase.delPlayer()
        friendsDatabase.delPlayer()
        if (!Database.isEnableRedis()) return
        val redis = Database.getRedisConnection()
        redis.del(this.name)
        redis.close()
    }

    private fun getCache(): MutableMap<String, String> {
        if (!Database.isEnableRedis()) throw RuntimeException("Redis未启用")
        val con = Database.getRedisConnection()
        val map = con.hgetAll("player_$name")
        con.close()
        return map
    }

    fun isCached() = Database.isEnableRedis() && !this.getCache().isEmpty()

    fun getAllData(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map.putAll(this.baseDatabase.allData)
        map.putAll(this.friendsDatabase.allData)
        map.putAll(this.guildDatabase.allData)
        map.putAll(this.mailDatabase.allData)
        return map
    }

    fun setRedisData(data: HashMap<String, Any>) {
        if (!Database.isEnableRedis()) return
        val redis = Database.getRedisConnection()
        val real = HashMap<String, String>()
        data.forEach { t, u -> real[t] = u.toString() }
        redis.hmset("player_$name", real)
        redis.close()
    }

    companion object {
        @JvmStatic
        @Throws(SQLException::class)
        fun loadAllPlayers() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT name FROM `player_base`;") as DruidPooledCallableStatement
            val rs = cs.executeQuery() as DruidPooledResultSet
            while (rs.next()) {
                APIFunction.instance.addAllPlayer(rs.getString("name"))
            }
            rs.close()
            cs.close()
            connection.close()
        }
    }
}