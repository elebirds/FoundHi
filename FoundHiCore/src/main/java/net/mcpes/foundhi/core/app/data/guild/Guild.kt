package net.mcpes.foundhi.core.app.data.guild

import cn.nukkit.level.Location
import com.alibaba.druid.pool.DruidPooledCallableStatement
import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.dao.Database
import net.mcpes.foundhi.core.dao.DatabaseQueue
import net.mcpes.foundhi.core.dao.guild.GuildDatabase
import net.mcpes.foundhi.core.dao.player.PlayerGuildDatabase
import net.mcpes.foundhi.core.network.protocol.guild.RemoveGuildPacket
import net.mcpes.foundhi.core.network.protocol.guild.SendGuildPacket
import net.mcpes.foundhi.core.network.protocol.guild.UpdateGuildInformationPacket
import net.mcpes.foundhi.core.utils.cast
import net.mcpes.foundhi.core.utils.isIdentical
import net.mcpes.foundhi.core.utils.removeLastChar
import net.mcpes.foundhi.core.utils.toLocation
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import java.sql.SQLException

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/27
 * version 1.0
 */
class Guild : GuildInterface {
    val id: Int
    var base: GuildDatabase
        private set

    var name: String = ""//公会名称
        set(value) {
            field = value
            this.update(0)
        }
    var color = "§f"
        set(value) {
            field = value
            this.update(1)
        }
    var leader: String = ""//会长
        set(leader) {
            if (field != "") {
                this.delAdmin(field)
            }
            if (this.isAdmin(leader)) {
                this.delAdmin(leader)
            }
            field = leader
            this.addAdmin(leader, Permission.LEADER)
            this.update(2)
        }
    var members: ArrayList<String> = ArrayList()//成员
    var admins: HashMap<String, Permission> = HashMap()//管理
    var banned: ArrayList<String> = ArrayList()//黑名单
    var apply: ArrayList<String> = ArrayList()//申请
    var maximum: Int = 0//最大人数
        set(value) {
            field = value
            this.update(3)
        }
    var notice = "无公告"
        set(value) {
            field = value
            this.update(4)
        }
    var level: Int = 0
        set(value) {
            field = value
            this.update(5)
        }
    var exp: Int = 0
        set(value) {
            field = value
            this.update(6)
        }
    var capital: Double = 0.toDouble()
        set(value) {
            field = value
            this.update(7)
        }
    var basePos = ""
        set(value) {
            field = value
            this.update(8)
        }
    private var baseLocation: Location? = null
    private val original = HashMap<String, Any>()
    val alreadySentServers = arrayListOf<String>()

    @Throws(SQLException::class)
    constructor(name: String, leader: String) {
        this.leader = leader
        this.name = name
        this.base = GuildDatabase(name, leader)
        this.id = this.base.id
        this.members.add(leader)
        this.level = 1
        this.exp = 0
        this.maximum = 20
        val pk = SendGuildPacket()
        pk.guild = this
        APIFunction.instance.broadcastPacket(pk)
    }

    constructor(id: Int) {
        this.id = id
        this.base = GuildDatabase(this.id)
        DatabaseQueue.instance.addTask(Runnable {
            val all = this.base.all
            this.name = all["n"].cast()
            this.leader = all["l"].cast()
            this.level = all["lv"].cast()
            this.exp = all["exp"].cast()
            this.maximum = all["max"].cast()
            this.members = all["m"].cast()
            this.admins = all["d"].cast()
            this.banned = all["b"].cast()
            this.color = all["c"].cast()
            this.apply = all["a"].cast()
            this.notice = all["o"].cast()
            this.capital = all["ca"].cast()
            this.basePos = all["bp"].cast()
            this.setBaseLocation()
            this.original["name"] = this.name
            this.original["leader"] = this.leader
            this.original["level"] = this.level
            this.original["exp"] = this.exp
            this.original["base"] = this.basePos
            this.original["capital"] = this.capital
            this.original["admins"] = this.admins.clone()
            this.original["color"] = this.color
            this.original["apply"] = this.apply.clone()
            this.original["notice"] = this.notice
            this.original["banned"] = this.banned.clone()
            this.original["member"] = this.members.clone()
            this.original["maximum"] = this.maximum
        }, arrayListOf())
    }

    fun getAdminCount(permission: Permission): Int {
        var n = 0
        this.admins.values.forEach {
            if (it == permission) {
                n++
            }
        }
        return n
    }

    fun addAdmin(admin: String, permission: Permission) {
        this.admins[admin] = permission
        this.update(9, 1, JSONObject.fromObject(kotlin.collections.hashMapOf("key" to admin, "p" to permission.level)).toString())
    }

    fun delAdmin(admin: String) {
        this.admins.remove(admin)
        this.update(9, 2, admin)
    }

    fun isAdmin(admin: String): Boolean {
        return this.admins.containsKey(admin)
    }

    fun getAdminPermission(admin: String): Permission? {
        return this.admins[admin]
    }

    fun addMember(member: String) {
        this.members.add(member)
        this.update(10, 1, member)
    }

    fun delMember(member: String) {
        if (isAdmin(member)) {
            delAdmin(member);
        }
        this.members.remove(member)
        this.update(10, 2, member)
    }

    fun isMember(member: String): Boolean {
        return this.members.contains(member)
    }

    fun addBanned(banned: String) {
        this.banned.add(banned)
        this.update(11, 1, banned)
    }

    fun delBanned(banned: String) {
        this.banned.remove(banned)
        this.update(11, 2, banned)
    }

    fun isBanned(banned: String): Boolean {
        return this.banned.contains(banned)
    }

    fun addApply(apply: String) {
        this.apply.add(apply)
        this.update(12, 1, apply)
    }

    fun delApply(apply: String) {
        this.apply.remove(apply)
        this.update(12, 2, apply)
    }

    fun addLevel(level: Int) {
        this.level += level
        this.update(5)
    }

    fun delLevel(level: Int) {
        this.level -= level
        this.update(5)
    }

    fun addExp(exp: Int) {
        this.exp += exp
        this.update(6)
    }

    fun delExp(exp: Int) {
        this.exp -= exp
        this.update(6)
    }

    fun addCapital(capital: Double) {
        this.capital += capital
        this.update(7)
    }

    fun delCapital(capital: Double) {
        this.capital -= capital
        this.update(7)
    }

    //TODO:base pos
    fun getBaseLocation(): Location? {
        if (baseLocation == null || baseLocation!!.level == null) {
            this.setBaseLocation()
        }
        return baseLocation
    }

    fun setBaseLocation() {
        this.baseLocation = this.basePos.toLocation()
    }

    @JvmOverloads
    fun update(id: Int, type: Int = 0, data: String? = null) {
        if (alreadySentServers.isEmpty()) return
        val pk = UpdateGuildInformationPacket()
        pk.id = this.id
        when (id) {
            0 -> {
                pk.parameter_name = "n"
                pk.parameter_value = this.name
            }
            1 -> {
                pk.parameter_name = "co"
                pk.parameter_value = this.color
            }
            2 -> {
                pk.parameter_name = "le"
                pk.parameter_value = this.leader
            }
            3 -> {
                pk.parameter_name = "max"
                pk.parameter_value = maximum
            }
            4 -> {
                pk.parameter_name = "no"
                pk.parameter_value = notice
            }
            5 -> {
                pk.parameter_name = "lv"
                pk.parameter_value = level
            }
            6 -> {
                pk.parameter_name = "ex"
                pk.parameter_value = exp
            }
            7 -> {
                pk.parameter_name = "ca"
                pk.parameter_value = capital
            }
            8 -> {
                pk.parameter_name = "bp"
                pk.parameter_value = basePos
            }
            9 -> {
                if (data == null) return
                pk.parameter_name = if (type == 1) {
                    "ad_a"
                } else {
                    "ad_b"
                }
                pk.parameter_value = data
            }
            10 -> {
                if (data == null) return
                pk.parameter_name = if (type == 1) {
                    "me_a"
                } else {
                    "me_b"
                }
                pk.parameter_value = data
            }
            11 -> {
                if (data == null) return
                pk.parameter_name = if (type == 1) {
                    "ba_a"
                } else {
                    "ba_b"
                }
                pk.parameter_value = data
            }
            12 -> {
                if (data == null) return
                pk.parameter_name = if (type == 1) {
                    "ap_a"
                } else {
                    "ap_b"
                }
                pk.parameter_value = data
            }
        }
        alreadySentServers.forEach {
            APIFunction.instance.getServer(it)?.dataPacket(pk)
        }
    }

    @Throws(SQLException::class)
    fun save() {
        var command = "UPDATE guild_base SET"
        if (this.original["name"].toString() != this.name) {
            command += " name = '$name',"
        }
        if (this.original["leader"].toString() != this.leader) {
            command += " leader = '$leader',"
        }
        if (this.original["level"] as Int != this.level) {
            command += " level = '$level',"
        }
        if (this.original["exp"] as Int != this.exp) {
            command += " exp = '$exp',"
        }
        if (this.original["base"].toString() != this.basePos) {
            command += " base = '$basePos',"
        }
        if (this.original["capital"] as Double != this.capital) {
            command += " capital = '$capital',"
        }
        if (this.admins == this.original["admins"]) {
            val a = HashMap<String, Int>()
            admins.forEach { n: String, o: Guild.Permission -> a[n] = o.level }
            command += " admins = '${JSONObject.fromObject(a)}',"
        }
        if (this.original["color"].toString() != this.color) {
            command += " color = '$color',"
        }
        if (!this.apply.isIdentical(this.original["apply"].cast())) {
            command += " apply = '${JSONArray.fromObject(apply)}',"
        }
        if (this.original["notice"].toString() != this.notice) {
            command += " notice = '$notice',"
        }
        if (this.original["maximum"] as Int != this.maximum) {
            command += " maximum = '$maximum',"
        }
        if (!this.banned.isIdentical(this.original["banned"].cast())) {
            command += " banned = '${JSONArray.fromObject(this.banned)}',"
        }
        if (!this.members.isIdentical(this.original["member"].cast())) {
            command += " members = '${JSONArray.fromObject(this.members)}',"
        }
        if (command == "UPDATE guild_base SET") return
        command = command.removeLastChar()
        command += " WHERE id = '$id';"
        Database.prepareStatement(command, true)
    }

    override fun toString(): String {
        val json = JSONObject()
        json["id"] = this.id
        json["name"] = this.name
        json["level"] = this.level
        json["exp"] = this.exp
        json["leader"] = this.leader
        json["capital"] = this.capital
        json["banned"] = JSONArray.fromObject(this.banned).toString()
        json["members"] = JSONArray.fromObject(this.members).toString()
        json["notice"] = this.notice
        json["maximum"] = this.maximum
        json["color"] = this.color
        json["apply"] = JSONArray.fromObject(this.apply).toString()
        json["admins"] = JSONObject.fromObject(this.admins).toString()
        json["base"] = basePos
        return json.toString()
    }

    fun dissolve(): Boolean {
        try {
            for (name in this.members) {
                if (APIFunction.instance.isPlayerOnline(name)) {
                    val player = APIFunction.instance.getPlayer(name)!!
                    player.guild = -1
                    player.sendMessage("§l§8┃§bS§au§dm§em§4i§6t§8┃§8 - NOTICE: 你的公会已经解散")
                } else
                    PlayerGuildDatabase(name).guild = -1
            }
            this.base.remove()
            APIFunction.instance.removeGuild(this)
            val pk = RemoveGuildPacket()
            pk.id = this.id
            APIFunction.instance.broadcastPacket(pk)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    enum class Permission(val level: Int, val permissionName: String, val count: Int) {
        PLAYER(0, "普通成员", Int.MAX_VALUE), ADMIN(1, "管理员", 5), HIGH_ADMIN(2, "高级管理", 3), VICE_LEADER(3, "副会长", 2), LEADER(4, "会长", 1)
    }

    companion object {
        const val NEED = 2000
        const val GUILD_TITLE = "§l§e[§6公§c会§e]"

        @Throws(SQLException::class)
        fun load() {
            val sql = "SELECT id FROM `guild_base`;"
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall(sql) as DruidPooledCallableStatement
            val resultSet = cs.executeQuery()
            while (resultSet.next()) {
                val key = resultSet.getInt("id")
                APIFunction.instance.addGuild(Guild(key))
            }
        }
    }
}
