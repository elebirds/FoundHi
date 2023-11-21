package net.mcpes.foundhipro.core.app.data.player

import java.sql.SQLException

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/28
 * version 1.0
 */
class Player @Throws(SQLException::class) constructor(val name: String) {
    /*
    var permission: Permission = Permission.PLAYER
        set(permission) {
            field = permission
            this.update(0)
        }
    var money1: Double = 0.toDouble()
        set(value) {
            field = value
            this.update(1)
        }
    var money2: Double = 0.toDouble()
        set(value) {
            field = value
            this.update(2)
        }
    var money3: Double = 0.toDouble()
        set(value) {
            field = value
            this.update(3)
        }
    var inServer = ""
    var guild: Int = -1
        set(guild) {
            field = guild
            this.update(4)
        }
    var nameColor: String = "§f"
        set(nameColor) {
            field = nameColor
            this.update(5)
        }
    var chatColor: String = "§f"
        set(chatColor) {
            field = chatColor
            this.update(6)
        }
    var titling: Int = 0
        set(titling) {
            field = titling
            this.update(7)
        }
    var title: ArrayList<String> = ArrayList()
        set(title) {
            field = title
            this.update(8)
        }
    var lover: String = ""
        set(lover) {
            field = lover
            this.update(9)
        }
    var invitation: HashMap<String, Int> = HashMap()
    var marry: ArrayList<String> = ArrayList()
    var contribution: Int = 0
        set(contribution) {
            field = contribution
            this.update(11)
        }
    //todo mail cache
    var mailInbox: HashMap<Mail, Int> = HashMap()
        set(value) {
            field = value
            this.update(12)
        }
    var mailOutbox: HashMap<Mail, Int> = HashMap()
        set(value) {
            field = value
            this.update(13)
        }
    var friends: HashMap<String, String> = HashMap()
        set(value) {
            field = value
            this.update(14)
        }
    var head: Int = 0
        set(value) {
            field = value
            this.update(15)
        }
    var autograph: String = ""
        set(value) {
            field = value
            this.update(16)
        }
    var apply: HashMap<String, Int> = HashMap()
        set(value) {
            field = value
            this.update(17)
        }
    var inTeam: Int = -1
        set(value) {
            field = value
            this.update(18)
        }

    var bags = HashMap<String, ByteArray>()
    val original = HashMap<String, Any>()
    val database: PlayerDatabase = PlayerDatabase(this)

    fun addMoney1(money1: Double) {
        this.money1 += money1
        this.update(1)
    }

    fun delMoney1(money1: Double): Boolean {
        if (this.money1 < money1) return false
        this.money1 -= money1
        this.update(1)
        return true
    }

    fun addMoney2(money2: Double) {
        this.money2 += money2
        this.update(2)
    }

    fun delMoney2(money2: Double): Boolean {
        if (this.money2 < money2) return false
        this.money2 -= money2
        this.update(2)
        return true
    }

    fun addMoney3(money3: Double) {
        this.money3 += money3
        this.update(3)
    }

    fun delMoney3(money3: Double): Boolean {
        if (this.money3 < money3) return false
        this.money3 -= money3
        this.update(3)
        return true
    }

    fun addFriend(id: String, date: String) {
        this.friends[id] = date
        this.update(14, 1, JSONObject.fromObject(hashMapOf("id" to id, "date" to date)).toString())
    }

    fun delFriend(id: String) {
        this.friends.remove(id)
        this.update(14, 2, id)
    }

    fun addFriendApply(id: String, type: Int) {
        this.apply[id] = type
        this.update(17, 1, JSONObject.fromObject(hashMapOf("id" to id, "type" to type)).toString())
    }

    fun delFriendApply(id: String) {
        this.apply.remove(id)
        this.update(17, 2, id)
    }

    fun addMarry(id: String) {
        this.marry.add(id)
        this.update(19, 1, id)
    }

    fun delMarry(id: String) {
        this.marry.remove(id)
        this.update(19, 2, id)
    }

    fun addInvitation(id: String, guild: Int) {
        this.invitation[id] = guild
        this.update(20, 1, JSONObject.fromObject(hashMapOf("id" to id, "guild" to guild)).toString())
    }

    fun delInvitation(id: String) {
        this.invitation.remove(id)
        this.update(20, 2, id)
    }

    fun addTitle(title: String) {
        this.title.add(title)
        this.update(8, 1, title)
    }

    fun delTitle(title: String) {
        this.title.remove(title)
        this.update(8, 2, title)
    }

    fun save() {
        run {
            var command = "UPDATE `player_base` SET"
            if (this.original["1"] as Double != this.money1) {
                command += " money1 = '$money1',"
                Database.hashUptate(this.name, "m1", money1.toString())
            }
            if (this.original["2"] as Double != this.money2) {
                command += " money2 = '$money2',"
                Database.hashUptate(this.name, "m2", money2.toString())
            }
            if (this.original["3"] as Double != this.money3) {
                command += " money3 = '$money3',"
                Database.hashUptate(this.name, "m3", money3.toString())
            }
            if (this.permission != this.original["p"]) {
                command += " permission = '${permission.level}',"
                Database.hashUptate(this.name, "p", permission.level.toString())
            }
            if (command != "UPDATE `player_base` SET") {
                command = command.removeLastChar()
                command += " WHERE name = '$name'"
                Database.prepareStatement(command, true)
            }
        }
        run {
            var command = "UPDATE `player_guild` SET"
            if (this.original["guild"] as Int != this.guild) {
                command += " guild = '$guild',"
                Database.hashUptate(this.name, "g", guild.toString())
            }
            if (this.original["ct"].toString() != this.chatColor) {
                command += " `chat-color` = '$chatColor',"
                Database.hashUptate(this.name, "cc", chatColor)
            }
            if (this.original["nt"].toString() != this.nameColor) {
                command += " `name-color` = '$nameColor',"
                Database.hashUptate(this.name, "nc", nameColor)
            }
            if (this.original["title"] != this.title) {
                val data = JSONArray.fromObject(title).toString()
                command += " title = '$data',"
                Database.hashUptate(this.name, "ti", data)
            }
            if (this.original["titling"] as Int != this.titling) {
                command += " titling = '$titling',"
                Database.hashUptate(this.name, "ut", titling.toString())
            }
            if (this.original["lover"].toString() != this.lover) {
                command += " lover = '$lover',"
                Database.hashUptate(this.name, "lo", lover)
            }
            if (this.original["invitation"] == this.invitation) {
                val data = JSONObject.fromObject(invitation).toString()
                command += " `invitation` = '$data',"
                Database.hashUptate(this.name, "inv", data)
            }
            if (this.original["contribution"] as Int != this.contribution) {
                command += " contribution = '$contribution',"
                Database.hashUptate(this.name, "co", contribution.toString())
            }
            if (this.marry != this.original["marry"]) {
                val data = JSONArray.fromObject(marry).toString()
                command += " marry = '$data',"
                Database.hashUptate(this.name, "ma", data)
            }
            if (command != "UPDATE `player_guild` SET") {
                command = command.removeLastChar()
                command += " WHERE name = '$name'"
                Database.prepareStatement(command, true)
            }
        }
        run {
            var command = "UPDATE `player_mail` SET"
            val data1 = JSONObject.fromObject(mailInbox).toString()
            command += " inbox = '$data1',"
            Database.hashUptate(this.name, "in", data1)
            val data2 = JSONObject.fromObject(mailOutbox).toString()
            command += " outbox = '$data2' WHERE name = '$name';"
            Database.hashUptate(this.name, "ou", data2)
            Database.prepareStatement(command, true)
        }
        run {
            var command = "UPDATE `player_friends` SET"
            if (this.original["head"] as Int != this.head) {
                command += " head = '$head',"
            }
            if (this.original["apply"] != this.apply) {
                val data = JSONObject.fromObject(apply).toString()
                command += " apply = '$data',"
                Database.hashUptate(this.name, "fa", data)
            }
            if (this.original["friends"] != this.friends) {
                val data = JSONObject.fromObject(friends).toString()
                command += " friends = '$data',"
                Database.hashUptate(this.name, "fr", data)
            }
            if (this.original["ag"].toString() != this.autograph) {
                command += " autograph = '$autograph',"
                Database.hashUptate(this.name, "ag", autograph)
            }
            if (command != "UPDATE `player_friends` SET") {
                command = command.removeLastChar()
                command += " WHERE name = '$name'"
                Database.prepareStatement(command, true)
            }
        }
    }

    fun sendMessage(msg: String) {
        if (inServer != "" && APIFunction.instance.isServerOnline(this.inServer)) {
            val pk = PlayerSendMessagePacket()
            pk.id = name
            pk.message = msg
            APIFunction.instance.getServer(inServer)?.dataPacket(pk)
        }
    }

    @JvmOverloads
    fun kick(reason: String, api: Boolean = false) {
        if (api) {
            if (!APIFunction.instance.isServerOnline(inServer)) return
            val pk = ServerCommandPacket()
            pk.command = "kick $name $reason"
            APIFunction.instance.getServer(inServer)?.dataPacket(pk)
        } else {
            APIFunction.instance.kickPlayer(this, reason)
        }
    }

    @JvmOverloads
    fun ban(operator: String, endTime: String, online: Boolean = true, reason: String = ""): String {
        val start = Date()
        val bannedInfo = BannedInfo(start, Date(start.time + DateUtils.getTimeDifference(endTime)), operator, this.name, reason, false)
        val pk = AddPlayerBannedPacket()
        pk.data = hashMapOf(bannedInfo.id.toString() to bannedInfo.toString())
        APIFunction.instance.broadcastPacket(pk)
        val to = bannedInfo.endTime.format()
        if (online) this.kick("§c此账户已被封禁 §2持续至:$to\n§6封禁原因:${bannedInfo.reason}")
        return to
    }

    private fun update(id: Int, type: Int = 0, data: String? = null) {
        if (!APIFunction.instance.isServerOnline(inServer)) return
        val upi = UpdatePlayerInformationPacket()
        upi.id = this.name
        when (id) {
            0 -> {
                upi.parameter_name = "p"
                upi.parameter_value = this.permission.level
            }
            1 -> {
                upi.parameter_name = "m1"
                upi.parameter_value = this.money1
            }
            2 -> {
                upi.parameter_name = "m2"
                upi.parameter_value = this.money2
            }
            3 -> {
                upi.parameter_name = "m3"
                upi.parameter_value = this.money3
            }
            4 -> {
                upi.parameter_name = "guild"
                upi.parameter_value = this.guild
            }
            5 -> {
                upi.parameter_name = "nc"
                upi.parameter_value = this.nameColor
            }
            6 -> {
                upi.parameter_name = "cc"
                upi.parameter_value = this.chatColor
            }
            7 -> {
                upi.parameter_name = "titling"
                upi.parameter_value = this.titling
            }
            8 -> {
                when (type) {
                    0 -> {
                        upi.parameter_name = "title"
                        upi.parameter_value = JSONArray.fromObject(this.title)
                    }
                    1 -> {
                        upi.parameter_name = "title_a"
                        upi.parameter_value = data!!
                    }
                    2 -> {
                        upi.parameter_name = "title_b"
                        upi.parameter_value = data!!
                    }
                }
            }
            9 -> {
                upi.parameter_name = "lover"
                upi.parameter_value = this.lover
            }
            11 -> {
                upi.parameter_name = "contribution"
                upi.parameter_value = this.contribution
            }
            12 -> {
                upi.parameter_name = "mi"
                upi.parameter_value = this.mailInbox
            }
            13 -> {
                upi.parameter_name = "mo"
                upi.parameter_value = this.mailOutbox
            }
            14 -> {
                when (type) {
                    0 -> {
                        upi.parameter_name = "fr"
                        upi.parameter_value = JSONObject.fromObject(this.friends)
                    }
                    1 -> {
                        upi.parameter_name = "fr_a"
                        upi.parameter_value = data!!
                    }
                    2 -> {
                        upi.parameter_name = "fr_b"
                        upi.parameter_value = data!!
                    }
                }
            }
            15 -> {
                upi.parameter_name = "he"
                upi.parameter_value = this.head
            }
            16 -> {
                upi.parameter_name = "ag"
                upi.parameter_value = this.autograph
            }
            17 -> {
                when (type) {
                    0 -> {
                        upi.parameter_name = "ap"
                        upi.parameter_value = JSONObject.fromObject(this.apply)
                    }
                    1 -> {
                        upi.parameter_name = "ap_a"
                        upi.parameter_value = data!!
                    }
                    2 -> {
                        upi.parameter_name = "ap_b"
                        upi.parameter_value = data!!
                    }
                }
            }
            18 -> {
                upi.parameter_name = "it"
                upi.parameter_value = this.inTeam
            }
            19 -> {
                when (type) {
                    0 -> {
                        upi.parameter_name = "ma"
                        upi.parameter_value = JSONArray.fromObject(marry)
                    }
                    1 -> {
                        upi.parameter_name = "ma_a"
                        upi.parameter_value = data!!
                    }
                    2 -> {
                        upi.parameter_name = "ma_b"
                        upi.parameter_value = data!!
                    }
                }
            }
            20 -> {
                when (type) {
                    0 -> {
                        upi.parameter_name = "inv"
                        upi.parameter_value = JSONObject.fromObject(this.invitation)
                    }
                    1 -> {
                        upi.parameter_name = "inv_a"
                        upi.parameter_value = data!!
                    }
                    2 -> {
                        upi.parameter_name = "inv_b"
                        upi.parameter_value = data!!
                    }
                }
            }
        }
        APIFunction.instance.getServer(inServer)?.dataPacket(upi)
    }

    fun sendData(sid: String) {
        if (!APIFunction.instance.isServerOnline(sid)) return
        val spip = SendPlayerInformationPacket()
        spip.id = this.name
        spip.permission = this.permission.level
        spip.money3 = this.money3
        spip.money2 = this.money2
        spip.money1 = this.money1
        spip.preference = this.preference
        spip.guild = this.guild
        spip.titling = this.titling
        spip.title = this.title
        spip.nameColor = this.nameColor
        spip.lover = this.lover
        spip.invitation = this.invitation
        spip.contribution = this.contribution
        spip.chatColor = this.chatColor
        spip.permission = this.permission.level
        spip.apply = this.apply
        spip.autograph = this.autograph
        spip.head = this.head
        spip.friends = this.friends
        spip.inbox = HashMap()
        spip.outbox = HashMap()
        this.mailInbox.forEach { m, date -> spip.inbox[m] = date }
        this.mailOutbox.forEach { m, date -> spip.outbox[m] = date }
        this.inServer = sid
        spip.marry = this.marry
        val server = APIFunction.instance.getServer(sid)!!
        server.dataPacket(spip)
        if (this.guild >= 0 && APIFunction.instance.hasGuild(this.guild) && !server.alreadySentGuilds.contains(this.guild)) {
            val guild = APIFunction.instance.getGuild(this.guild)!!
            val pk = SendGuildPacket()
            pk.guild = guild
            server.dataPacket(pk)
            guild.alreadySentServers.add(this.inServer)
            server.alreadySentGuilds.add(this.guild)
        }
    }

    enum class Permission(val level: Int, val permissionName: String) {
        Ban(-1, "禁止玩家"), PLAYER(0, "普通玩家"), LOW_ADMIN(1, "低级管理"), HIGH_ADMIN(2, "高级管理"), OWNER(3, "服主"), DEVELOPER(4, "开发者")
    }

    companion object {
        const val KICK_TIME = 30
    }*/
}