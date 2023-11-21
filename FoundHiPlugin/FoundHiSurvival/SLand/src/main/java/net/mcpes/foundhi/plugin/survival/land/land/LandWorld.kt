package net.mcpes.foundhi.plugin.survival.land.land

import com.alibaba.druid.pool.DruidPooledCallableStatement
import net.mcpes.foundhi.core.dao.Database
import net.mcpes.foundhi.plugin.survival.land.SLand
import net.mcpes.foundhi.plugin.survival.land.database.LandDatabase
import net.mcpes.foundhi.plugin.survival.land.utils.LandUtils
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * SLand
 *
 * @author hhm Copyright (c) 2018/5/2
 * version 1.0
 */

class LandWorld {
    val id: Int
    val level: String
    var name: String
    val type: Int
    var money: Double
    var moneyType: Int
    val lands: HashMap<String, Land> = HashMap()

    companion object {
        fun init() {
            LandDatabase.initMainTable()
            LandDatabase.initLandWorlds()
        }
    }

    constructor(id: Int, level: String, name: String, type: Int, money: Double, moneyType: Int) {
        this.id = id
        this.level = level
        this.name = name
        this.type = type
        this.money = money
        this.moneyType = moneyType
        LandDatabase.initLandTable(this.id)
        this.initLand()
    }

    constructor(level: String, name: String, type: Int, money: Double, moneyType: Int) {
        this.level = level
        this.name = name
        this.type = type
        this.money = money
        this.moneyType = moneyType
        this.id = LandDatabase.addLandWorld(level, name, type, money, moneyType)
        LandDatabase.initLandTable(this.id)
        this.initLand()
    }

    @Throws(SQLException::class)
    private fun initLand() {
        val sql = "SELECT * FROM `land_$id`;"
        val connection = Database.getMySQLConnection()
        val cs = connection.prepareCall(sql) as DruidPooledCallableStatement
        val resultSet = cs.executeQuery()
        while (resultSet.next()) {
            val id = resultSet.getString("id")
            val x1 = resultSet.getInt("x1")
            val x2 = resultSet.getInt("x2")
            val z1 = resultSet.getInt("z1")
            val z2 = resultSet.getInt("z2")
            val owner = resultSet.getString("owner")
            val guestText = resultSet.getString("guest")
            val permissionText = resultSet.getString("permission")
            val guest = ArrayList<String>()
            val permission = HashMap<Land.Companion.PlayerType, HashMap<String, Boolean>>()
            JSONArray.fromObject(guestText).forEach { guest.add(it.toString()) }
            JSONObject.fromObject(permissionText).forEach {
                val values = it.value as JSONObject
                val data = HashMap<String, Boolean>()
                values.forEach { data[it.key.toString()] = it.value.toString().toBoolean() }
                permission[Land.getPlayerType(it.key.toString())] = data
            }
            val land = Land(id, x1, x2, z1, z2, owner, permission, guest, this.level)
            LandUtils.addPhLand(owner, land)
            guest.forEach {
                LandUtils.addPgLand(it, land)
            }
            SLand.lands["$id-$level"] = land
            this.lands[id] = land
        }
    }

    private fun saveLands() {
        val connection = Database.getMySQLConnection()
        var ps: PreparedStatement
        lands.forEach { id, land ->
            ps = connection.prepareStatement("UPDATE `land_$id` SET guest = '${JSONArray.fromObject(land.guests)}' WHERE id = '$id';")
            ps.executeUpdate()
            ps.close()
            ps = connection.prepareStatement("UPDATE `land_$id` SET permission = '${JSONObject.fromObject(land.permission)}' WHERE id = '$id';")
            ps.executeUpdate()
            ps.close()
            ps = connection.prepareStatement("UPDATE `land_$id` SET owner = '${land.owner}' WHERE id = '$id';")
            ps.executeUpdate()
            ps.close()
        }
        connection.close()
    }

    fun save() {
        LandDatabase.setMoney(this.id, this.money)
        LandDatabase.setMoneyType(this.id, this.moneyType)
        LandDatabase.setName(this.id, this.name)
        this.saveLands()
    }
}