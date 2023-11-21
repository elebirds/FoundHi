package net.mcpes.foundhi.core.app.data.banned

import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.dao.banned.BannedPlayerDatabase
import net.sf.json.JSONObject
import java.sql.ResultSet
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/24 下午6:39
 * version 1.0
 */
class BannedInfo {
    val id: Int
    val startTime: Date
    var endTime: Date
        set(value) {
            field = value
            revised = true
        }
    val operator: String
    val banned: String
    val reason: String
    var beLifted: Boolean
        set(value) {
            field = value
            revised = true
        }
    var reviser: String
        set(value) {
            field = value
            revised = true
        }
    var revised: Boolean = false

    constructor(id: Int, rs: ResultSet) {
        this.id = id
        this.startTime = Date(rs.getTimestamp("start_time").time)
        this.endTime = Date(rs.getTimestamp("end_time").time)
        this.reason = rs.getString("reason")
        this.operator = rs.getString("operator")
        this.banned = rs.getString("banned")
        this.beLifted = rs.getBoolean("be_lifted")
        this.reviser = rs.getString("reviser")
        APIFunction.instance.addBanned(this)
    }

    constructor(startTime: Date, endTime: Date, operator: String, banned: String, reason: String, beLifted: Boolean) {
        this.startTime = startTime
        this.endTime = endTime
        this.operator = operator
        this.beLifted = beLifted
        this.reason = reason
        this.banned = banned
        this.reviser = ""
        this.id = BannedPlayerDatabase.add(this)
        APIFunction.instance.addBanned(this)
    }

    fun save() {
        if (revised) {
            BannedPlayerDatabase.save(this)
        }
    }

    override fun toString(): String {
        val json = JSONObject()
        json["st"] = this.startTime.time
        json["et"] = this.endTime.time
        json["r"] = this.reason
        json["b"] = this.banned
        json["l"] = this.beLifted
        json["e"] = this.reviser
        json["o"] = this.operator
        return json.toString()
    }
}