package net.mcpes.foundhi.core.dao.player

import com.alibaba.druid.pool.DruidPooledCallableStatement
import com.alibaba.druid.pool.DruidPooledResultSet
import net.mcpes.foundhi.core.dao.Database
import net.sf.json.JSONObject
import java.sql.SQLException

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/25
 * version 1.0
 */
class PlayerFriendsDatabase @Throws(SQLException::class) constructor(val name: String) {
    var allData: HashMap<String, Any>
        @Throws(SQLException::class)
        get() {
            val data: HashMap<String, Any> = HashMap()
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT * FROM `player_friends` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery()
            rs.next()
            data["he"] = rs.getInt("head")
            data["ag"] = rs.getString("autograph")
            data["fr"] = rs.getString("friends")
            data["fa"] = rs.getString("apply")
            return data
        }
        private set(value) {

        }
    var head: Int
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT head FROM `player_friends` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val value = rs.getInt(1)
            rs.close()
            cs.close()
            connection.close()
            return value
        }
        @Throws(SQLException::class)
        set(value) {
            Database.prepareStatement("UPDATE player_friends SET head = $value WHERE name = '$name';", true)
            Database.hashUptate(this.name, "he", value.toString())
        }
    var autograph: String
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT autograph FROM `player_friends` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val value = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            return value
        }
        @Throws(SQLException::class)
        set(value) {
            Database.prepareStatement("UPDATE player_friends SET autograph = $value WHERE name = '$name';", true)
            Database.hashUptate(this.name, "ag", value)
        }
    var friends: HashMap<String, String>
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT friends FROM `player_friends` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val value = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            val map = HashMap<String, String>()
            JSONObject.fromObject(value).forEach { t, u -> map[t.toString()] = u.toString() }
            return map
        }
        @Throws(SQLException::class)
        set(value) {
            val data = JSONObject.fromObject(value).toString()
            Database.prepareStatement("UPDATE player_friends SET friends = $data WHERE name = '$name';", true)
            Database.hashUptate(this.name, "fr", data)
        }
    var apply: HashMap<String, Int>
        get () {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT apply FROM `player_friends` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val value = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            val map = HashMap<String, Int>()
            JSONObject.fromObject(value).forEach { t, u -> map[t.toString()] = u.toString().toInt() }
            return map
        }
        @Throws(SQLException::class)
        set(value) {
            val data = JSONObject.fromObject(value).toString()
            Database.prepareStatement("UPDATE player_friends SET apply = $data WHERE name = '$name';", true)
            Database.hashUptate(this.name, "fa", data)
        }


    companion object {
        @JvmStatic
        @Throws(SQLException::class)
        fun newTable() {
            Database.newTable("CREATE TABLE IF NOT EXISTS player_friends(" +
                    "    name varchar(255) PRIMARY KEY NOT NULL," +
                    "    head int DEFAULT 0," +
                    "    autograph varchar(255) DEFAULT '这个人没有想说的话'," +
                    "    friends text," +
                    "    apply text" +
                    ");")
        }

        @JvmStatic
        @Throws(SQLException::class)
        fun exists(name: String): Boolean {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT COUNT(*) FROM player_friends WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val count = rs.getInt(1)
            rs.close()
            cs.close()
            connection.close()
            return count == 1
        }
    }

    @Throws(SQLException::class)
    fun delPlayer() {
        if (!exists()) return
        val connection = Database.getMySQLConnection()
        val cs = connection.prepareCall("DELETE FROM player_friends WHERE name = ?;") as DruidPooledCallableStatement
        cs.setString(1, name)
        cs.execute()
        cs.close()
        connection.close()
    }

    @Throws(SQLException::class)
    fun exists(): Boolean {
        return exists(this.name)
    }

    @Throws(SQLException::class)
    fun addPlayer() {
        if (this.exists()) return
        val connection = Database.getMySQLConnection()
        val cs1 = connection.prepareCall("INSERT INTO `player_friends`(`name`,`friends`,`apply`) VALUES (?,?,?);") as DruidPooledCallableStatement
        cs1.setString(1, name)
        cs1.setString(2, "{}")
        cs1.setString(3, "{}")
        cs1.execute()
        cs1.close()
        connection.close()
    }
}