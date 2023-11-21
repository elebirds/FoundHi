package net.mcpes.foundhi.core.dao.player

import com.alibaba.druid.pool.DruidPooledCallableStatement
import com.alibaba.druid.pool.DruidPooledResultSet
import net.mcpes.foundhi.core.dao.Database
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import java.sql.SQLException
import java.util.*
import kotlin.collections.ArrayList

class PlayerGuildDatabase @Throws(SQLException::class)
constructor(val name: String) {
    var allData: HashMap<String, Any>
        @Throws(SQLException::class)
        get() {
            val data: HashMap<String, Any> = HashMap()
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT * FROM `player_guild` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery()
            rs.next()
            data["g"] = rs.getInt("guild")
            data["nc"] = rs.getString("name-color")
            data["cc"] = rs.getString("chat-color")
            data["ti"] = rs.getString("title")
            data["ut"] = rs.getString("titling")
            data["co"] = rs.getInt("contribution")
            data["lo"] = rs.getString("lover")
            data["inv"] = rs.getString("invitation")
            data["ma"] = rs.getString("marry")
            return data
        }
        private set(value) {

        }

    var guild: Int
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT guild FROM `player_guild` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val guild = rs.getInt(1)
            rs.close()
            cs.close()
            connection.close()
            return guild
        }
        @Throws(SQLException::class)
        set(guild) {
            Database.prepareStatement("UPDATE player_guild SET guild = $guild WHERE name = '$name';", true)
            Database.hashUptate(this.name, "g", guild.toString())
        }

    var nameColor: String
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT `name-color` FROM `player_guild` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val color = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            return color
        }
        @Throws(SQLException::class)
        set(value) {
            Database.prepareStatement("UPDATE player_guild SET `name-color` = $value WHERE name = '$name';", true)
            Database.hashUptate(this.name, "nc", value)
        }

    var chatColor: String
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT `chat-color` FROM `player_guild` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val color = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            return color
        }
        @Throws(SQLException::class)
        set(chat) {
            Database.prepareStatement("UPDATE player_guild SET `chat-color` = $chat WHERE name = '$name';", true)
            Database.hashUptate(this.name, "cc", chat)
        }

    var title: ArrayList<String>
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT `title` FROM `player_guild` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val text = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            val real = ArrayList<String>()
            JSONArray.fromObject(text).forEach { real.add(it.toString()) }
            return real
        }
        @Throws(SQLException::class)
        set(title) {
            val data = JSONArray.fromObject(title).toString()
            Database.prepareStatement("UPDATE player_guild SET title = $data WHERE name = '$name';", true)
            Database.hashUptate(this.name, "ti", data)
        }

    var titling: Int
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT titling FROM `player_guild` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val ing = rs.getInt(1)
            rs.close()
            cs.close()
            connection.close()
            return ing
        }
        @Throws(SQLException::class)
        set(ing) {
            Database.prepareStatement("UPDATE player_guild SET titling = $ing WHERE name = '$name';", true)
            Database.hashUptate(this.name, "ut", ing.toString())
        }

    var lover: String
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT lover FROM `player_guild` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val lover = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            return lover
        }
        @Throws(SQLException::class)
        set(lover) {
            Database.prepareStatement("UPDATE player_guild SET lover = $lover WHERE name = '$name';", true)
            Database.hashUptate(this.name, "lo", lover)
        }

    var invitationMessage: HashMap<String, Int>
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT `invitation` FROM `player_guild` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val text = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            val map = HashMap<String, Int>()
            JSONObject.fromObject(text).forEach { t, u -> map[t.toString()] = u.toString().toInt() }
            return map
        }
        @Throws(SQLException::class)
        set(im) {
            val data = JSONObject.fromObject(im).toString()
            Database.prepareStatement("UPDATE player_guild SET `invitation` = '$data' WHERE name = '$name';", true)
            Database.hashUptate(this.name, "inv", data)
        }

    var contribution: Int
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT contribution FROM `player_guild` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val contribution = rs.getInt(1)
            rs.close()
            cs.close()
            connection.close()
            return contribution
        }
        @Throws(SQLException::class)
        set(contribution) {
            Database.prepareStatement("UPDATE player_guild SET contribution = $contribution WHERE name = '$name';", true)
            Database.hashUptate(this.name, "co", contribution.toString())
        }

    var marry: ArrayList<String>
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT marry FROM `player_guild` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val m = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            val array: ArrayList<String> = ArrayList()
            JSONArray.fromObject(m).forEach {
                array.add(it.toString())
            }
            return array
        }
        @Throws(SQLException::class)
        set(marry) {
            val data = JSONArray.fromObject(marry).toString()
            Database.prepareStatement("UPDATE player_guild SET marry = '$data' WHERE name = '$name';", true)
            Database.hashUptate(this.name, "ma", data)
        }

    @Throws(SQLException::class)
    fun exists(): Boolean {
        return exists(this.name)
    }

    @Throws(SQLException::class)
    fun delPlayer() {
        if (!exists()) return
        val connection = Database.getMySQLConnection()
        val cs = connection.prepareCall("DELETE FROM player_guild WHERE name = ?;") as DruidPooledCallableStatement
        cs.setString(1, name)
        cs.execute()
        cs.close()
        connection.close()
    }

    @Throws(SQLException::class)
    fun addPlayer() {
        if (this.exists()) return
        val connection = Database.getMySQLConnection()
        val cs1 = connection.prepareCall("INSERT INTO `player_guild`(`name`,`title`,`invitation`,`marry`) VALUES (?,?,?,?)") as DruidPooledCallableStatement
        cs1.setString(1, name)
        cs1.setString(2, "[]")
        cs1.setString(3, "{}")
        cs1.setString(4, "[]")
        cs1.execute()
        cs1.close()
        connection.close()
    }

    companion object {
        @JvmStatic
        @Throws(SQLException::class)
        fun newTable() {
            Database.newTable("CREATE TABLE IF NOT EXISTS `player_guild`  (" +
                    "  `name` varchar(255) NOT NULL COMMENT '玩家名称'," +
                    "  `guild` int(10) NULL DEFAULT -1 COMMENT '公会id'," +
                    "  `title` text NULL COMMENT '称号列表'," +
                    "  `titling` int(4) NULL DEFAULT -1 COMMENT '佩戴称号'," +
                    "  `lover` varchar(255) NULL DEFAULT '' COMMENT '情侣'," +
                    "  `name-color` varchar(2) NULL DEFAULT '§f' COMMENT '名称颜色'," +
                    "  `chat-color` varchar(2) NULL DEFAULT '§f' COMMENT '聊天颜色'," +
                    "  `contribution` int(10) UNSIGNED NULL DEFAULT 0 COMMENT '贡献'," +
                    "  `invitation` text NULL COMMENT '邀请消息'," +
                    "  `marry` text NULL COMMENT '结婚邀请消息'," +
                    "  PRIMARY KEY (`name`)" +
                    ");")
        }

        @Throws(SQLException::class)
        fun exists(name: String): Boolean {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT COUNT(*) FROM player_guild WHERE name = ?;") as DruidPooledCallableStatement
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
}
