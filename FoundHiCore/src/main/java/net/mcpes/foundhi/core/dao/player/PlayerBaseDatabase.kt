package net.mcpes.foundhi.core.dao.player

import com.alibaba.druid.pool.DruidPooledCallableStatement
import com.alibaba.druid.pool.DruidPooledResultSet
import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.dao.Database
import net.mcpes.foundhi.core.utils.getPlayerPermission
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import java.sql.SQLException

class PlayerBaseDatabase @Throws(SQLException::class)
constructor(private val name: String) {
    var allData: HashMap<String, Any>
        @Throws(SQLException::class)
        get() {
            val data: HashMap<String, Any> = HashMap()
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT * FROM `player_base` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery()
            rs.next()
            data["p"] = rs.getInt("permission")
            data["m1"] = rs.getDouble("money1")
            data["m2"] = rs.getDouble("money2")
            data["m3"] = rs.getDouble("money3")
            data["b"] = rs.getString("bags")
            data["pre"] = rs.getString("preference_settings")
            return data
        }
        private set(value) {

        }

    var permission: Player.Permission
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT permission FROM `player_base` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val permission = rs.getInt(1)
            rs.close()
            cs.close()
            connection.close()
            return permission.getPlayerPermission()
        }
        @Throws(SQLException::class)
        set(permission) {
            val sql = "UPDATE player_base SET permission = ${permission.level} WHERE name = '$name';"
            Database.prepareStatement(sql, true)
            Database.hashUptate(this.name, "p", permission.level.toString())
        }

    var money1: Double
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT money1 FROM `player_base` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val money = rs.getDouble(1)
            rs.close()
            cs.close()
            connection.close()
            return money
        }
        @Throws(SQLException::class)
        set(money) {
            val sql = "UPDATE player_base SET money1 = $money WHERE name = '$name';"
            Database.prepareStatement(sql, true)
            Database.hashUptate(this.name, "m1", money.toString())
        }

    var money2: Double
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT money2 FROM `player_base` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val money = rs.getDouble(1)
            rs.close()
            cs.close()
            connection.close()
            return money
        }
        @Throws(SQLException::class)
        set(money) {
            val sql = "UPDATE player_base SET money2 = $money WHERE name = '$name';"
            Database.prepareStatement(sql, true)
            Database.hashUptate(this.name, "m2", money.toString())
        }

    var money3: Double
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT money3 FROM `player_base` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val money = rs.getDouble(1)
            rs.close()
            cs.close()
            connection.close()
            return money
        }
        @Throws(SQLException::class)
        set(money) {
            val sql = "UPDATE player_base SET money3 = $money WHERE name = '$name';"
            Database.prepareStatement(sql, true)
            Database.hashUptate(this.name, "m3", money.toString())
        }

    var bags: HashMap<String, ByteArray>
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT bags FROM `player_base` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val data = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            val json = JSONObject.fromObject(data)
            val real = HashMap<String, ByteArray>()
            json.forEach { o, o2 ->
                val values = o2 as JSONArray
                val realValue = ByteArray(values.size)
                for (i in values.indices) {
                    realValue[i] = values.getInt(i).toByte()
                }
                real[o.toString()] = realValue
            }
            return real
        }
        @Throws(SQLException::class)
        set(bags) {
            val data = JSONObject.fromObject(bags).toString()
            Database.prepareStatement("UPDATE player_base SET bags = '$data' WHERE name = '$name';", true)
            Database.hashUptate(this.name, "b", data)
        }
    var preference: String
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT preference_settings FROM `player_base` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val data = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            return data
        }
        @Throws(SQLException::class)
        set(bags) {
            val data = JSONObject.fromObject(bags).toString()
            Database.prepareStatement("UPDATE player_base SET `preference_settings` = '$data' WHERE name = '$name';", true)
            Database.hashUptate(this.name, "b", data)
        }

    @Throws(SQLException::class)
    fun exists(): Boolean {
        val connection = Database.getMySQLConnection()
        val cs = connection.prepareCall("SELECT COUNT(*)FROM player_base WHERE name = ?;") as DruidPooledCallableStatement
        cs.setString(1, name)
        val rs = cs.executeQuery() as DruidPooledResultSet
        rs.next()
        val count = rs.getInt(1)
        rs.close()
        cs.close()
        connection.close()
        return count == 1
    }

    @Throws(SQLException::class)
    fun addPlayer() {
        if (exists()) return
        val connection = Database.getMySQLConnection()
        val cs = connection.prepareCall("INSERT INTO `player_base`(`name`,`bags`,`preference_settings`) VALUES (?,?,?)") as DruidPooledCallableStatement
        cs.setString(1, name)
        cs.setString(2, "{}")
        cs.setString(3, "{}")
        cs.execute()
        cs.close()
        connection.close()
    }

    @Throws(SQLException::class)
    fun delPlayer() {
        if (!exists()) return
        val connection = Database.getMySQLConnection()
        val cs = connection.prepareCall("DELETE FROM player_base WHERE name = ?;") as DruidPooledCallableStatement
        cs.setString(1, name)
        cs.execute()
        cs.close()
        connection.close()
    }

    companion object {
        @JvmStatic
        @Throws(SQLException::class)
        fun newTable() {
            Database.newTable("CREATE TABLE IF NOT EXISTS `player_base`  (" +
                    "  `name` varchar(255) NOT NULL COMMENT '玩家名称'," +
                    "  `permission` int(1) NULL DEFAULT 0 COMMENT '权限'," +
                    "  `money1` double(10, 2) NULL DEFAULT 0.00 COMMENT '货币1'," +
                    "  `money2` double(10, 2) NULL DEFAULT 0.00 COMMENT '货币2'," +
                    "  `money3` double(10, 2) NULL DEFAULT 0.00 COMMENT '货币3'," +
                    "  `bags` LONGTEXT NULL COMMENT '云背包'," +
                    "  `preference_settings` TEXT NULL COMMENT '偏好设置'," +
                    "  PRIMARY KEY (`name`)" +
                    ");")
        }
    }
}
