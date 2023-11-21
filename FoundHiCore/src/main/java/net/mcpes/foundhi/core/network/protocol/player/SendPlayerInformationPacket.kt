package net.mcpes.foundhi.core.network.protocol.player

import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.app.data.mail.Mail
import net.mcpes.foundhi.core.app.data.mail.MailInterfate
import net.mcpes.foundhi.core.app.data.player.PlayerPreferenceSettings
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.data.MailCache
import net.mcpes.foundhi.core.plugin.data.PlayerCache
import net.mcpes.foundhi.core.utils.getPlayerPermission
import net.sf.json.JSONArray
import net.sf.json.JSONObject

class SendPlayerInformationPacket : DataPacket() {
    lateinit var id: String
    @JvmField
    var permission: Int = 0
    @JvmField
    var money1: Double = 0.0
    @JvmField
    var money2: Double = 0.0
    @JvmField
    var money3: Double = 0.0
    lateinit var preference: PlayerPreferenceSettings

    @JvmField
    var guild: Int = 0
    lateinit var nameColor: String
    lateinit var chatColor: String
    @JvmField
    var titling: Int = 0
    lateinit var title: ArrayList<String>
    lateinit var lover: String
    lateinit var marry: ArrayList<String>
    @JvmField
    var contribution: Int = 0
    lateinit var inbox: HashMap<MailInterfate, Int>
    lateinit var outbox: HashMap<MailInterfate, Int>
    lateinit var friends: HashMap<String, String>
    @JvmField
    var head: Int = 0
    @JvmField
    var autograph: String = ""
    lateinit var apply: HashMap<String, Int>
    lateinit var invitation: HashMap<String, Int>
    @JvmField
    var inTeam: Int = 0

    override fun pid(): Int {
        return ProtocolInfo.PLAYER_SEND_INFORMATION_PACKET
    }

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["id"] = id
        json["p"] = permission
        json["m1"] = money1
        json["m2"] = money2
        json["m3"] = money3
        json["pre"] = preference.toString()
        json["guild"] = guild
        json["nc"] = nameColor
        json["cc"] = chatColor
        json["ti"] = titling
        json["te"] = JSONArray.fromObject(this.title)
        json["lover"] = lover
        json["co"] = contribution
        json["in"] = JSONObject.fromObject(this.invitation).toString()
        val ir = HashMap<String, Int>()
        val or = HashMap<String, Int>()
        inbox.forEach { t, u -> ir[(t as Mail).toJSON().toString()] = u }
        outbox.forEach { t, u -> or[(t as Mail).toJSON().toString()] = u }
        json["ir"] = JSONObject.fromObject(ir).toString()
        json["or"] = JSONObject.fromObject(or).toString()
        json["fr"] = JSONObject.fromObject(friends).toString()
        json["ag"] = this.autograph
        json["ap"] = JSONObject.fromObject(apply).toString()
        json["he"] = head
        json["it"] = inTeam
        json["ma"] = JSONArray.fromObject(this.marry)
        return json.toString()
    }

    override fun decode(json: JSONObject): DataPacket {
        this.id = json.getString("id")
        this.permission = json.getInt("p")
        this.money1 = json.getDouble("m1")
        this.money2 = json.getDouble("m2")
        this.money3 = json.getDouble("m3")
        this.guild = json.getInt("guild")
        this.nameColor = json.getString("nc")
        this.chatColor = json.getString("cc")
        this.titling = json.getInt("ti")
        this.preference = PlayerPreferenceSettings(this.id)
        this.preference.load(json.getString("pre"))
        this.inTeam = json.getInt("it")
        val real = arrayListOf<String>()
        JSONArray.fromObject(json.getString("te")).forEach { real.add(it.toString()) }
        this.title = real
        this.lover = json.getString("lover")
        this.contribution = json.getInt("co")
        this.invitation = HashMap()
        json.getJSONObject("in").forEach {
            this.invitation[it.key.toString()] = it.value as Int
        }
        this.inbox = HashMap()
        this.outbox = HashMap()
        json.getJSONObject("ir").forEach { t, u ->
            val j = JSONObject.fromObject(t.toString())
            if (FoundHiPlugin.instance.hasMail(j.getString("i"))) {
                this.inbox[FoundHiPlugin.instance.getMail(j.getString("i"))!!] = u.toString().toInt()
            } else {
                val mail = MailCache(j.getString("i"), j.getInt("t"), "mail_$t", j.getString("d"), j.getString("s"), j.getString("c"), HashMap(JSONObject.fromObject(j.getString("a"))) as HashMap<String, Any>, j.getBoolean("r"));
                this.inbox[mail] = u.toString().toInt()
                FoundHiPlugin.instance.addMail(mail)
            }
        }
        JSONObject.fromObject(json["or"].toString()).forEach { t, u ->
            val g = JSONObject.fromObject(t.toString())
            if (FoundHiPlugin.instance.hasMail(g.getString("i"))) {
                this.outbox[FoundHiPlugin.instance.getMail(g.getString("i"))!!] = u.toString().toInt()
            } else {
                val mail = MailCache(g.getString("i"), g.getInt("t"), "mail_$t", g.getString("d"), g.getString("s"), g.getString("c"), HashMap(JSONObject.fromObject(g.getString("a"))) as HashMap<String, Any>, g.getBoolean("r"));
                this.outbox[mail] = u.toString().toInt()
                FoundHiPlugin.instance.addMail(mail)
            }
        }
        this.autograph = json.getString("ag")
        this.head = json.getInt("he")
        this.friends = HashMap()
        this.apply = HashMap()
        JSONObject.fromObject(json.getString("fr")).forEach { t, u -> this.friends[t.toString()] = u.toString() }
        JSONObject.fromObject(json.getString("ap")).forEach { t, u -> this.apply[t.toString()] = u.toString().toInt() }
        this.marry = arrayListOf()
        JSONArray.fromObject(json.getString("ma")).forEach { this.marry.add(it.toString()) }
        return this
    }

    override fun handle() {
        if (ServerType.getType() == ServerType.API) return
        FoundHiPlugin.instance.addPlayer(PlayerCache(id, permission.getPlayerPermission(), money1, money2, money3, preference, guild, nameColor, chatColor, titling, title, lover, contribution, invitation, marry, inbox as HashMap<MailCache, Int>, outbox as HashMap<MailCache, Int>, friends, head, autograph, apply, inTeam))
        FoundHiPlugin.instance.logger.notice("§b加载FoundHi玩家缓存(id:$id)")
    }
}
