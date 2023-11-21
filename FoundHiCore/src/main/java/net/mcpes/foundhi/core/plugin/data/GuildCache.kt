package net.mcpes.foundhi.core.plugin.data

import cn.nukkit.level.Location
import net.mcpes.foundhi.core.app.data.guild.Guild
import net.mcpes.foundhi.core.app.data.guild.GuildInterface
import net.mcpes.foundhi.core.utils.toLocation
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/27
 * version 1.0
 */
class GuildCache(json: JSONObject) : GuildInterface {
    val id: Int = json.getInt("id")

    var name: String = json.getString("name")//公会名称
    var color = "§f"
    var leader: String = json.getString("leader")//会长
    var members: ArrayList<String>//成员
    var admins: HashMap<String, Guild.Permission>//管理
    var banned: ArrayList<String>//黑名单
    var apply: ArrayList<String>//申请
    var maximum: Int = 0//最大人数
    var notice = "无公告"
    var level: Int = 0
    var exp: Int = 0
    var capital: Double = 0.toDouble()
    var basePos = ""
    private var baseLocation: Location? = null

    fun getBaseLocation(): Location? {
        if (baseLocation == null || baseLocation!!.level == null) {
            this.setBaseLocation()
        }
        return baseLocation
    }

    fun setBaseLocation() {
        this.baseLocation = this.basePos.toLocation()
    }

    init {
        this.color = json.getString("color")
        this.members = ArrayList(JSONArray.fromObject(json.getString("members"))) as ArrayList<String>
        this.banned = ArrayList(JSONArray.fromObject(json.getString("banned"))) as ArrayList<String>
        this.apply = ArrayList(JSONArray.fromObject(json.getString("apply"))) as ArrayList<String>
        this.admins = HashMap(JSONObject.fromObject(json.getString("admins"))) as HashMap<String, Guild.Permission>
        this.maximum = json.getInt("maximum")
        this.notice = json.getString("notice")
        this.level = json.getInt("level")
        this.exp = json.getInt("exp")
        this.capital = json.getDouble("capital")
        this.basePos = json.getString("base")
        this.setBaseLocation()
    }
}
