package net.mcpes.foundhi.core.dao.guild

import com.alibaba.druid.pool.DruidPooledCallableStatement
import com.alibaba.druid.pool.DruidPooledResultSet
import net.mcpes.foundhi.core.app.data.guild.Guild
import net.mcpes.foundhi.core.dao.Database
import net.mcpes.foundhi.core.utils.getGuildPermission
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import java.io.Serializable
import java.sql.SQLException

class GuildDatabase {
    val id: Int

    var all: HashMap<String, Serializable>
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT * FROM `guild_base` WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val m = rs.getString("members")
            val members = ArrayList<String>()
            JSONArray.fromObject(m).forEach {
                members.add(it.toString())
            }
            val b = rs.getString("banned")
            val banned = arrayListOf<String>()
            JSONArray.fromObject(b).forEach {
                banned.add(it.toString())
            }
            val a = rs.getString("admins")
            val jo = JSONObject.fromObject(a)
            val data = HashMap<String, Guild.Permission>()
            for (name in jo.keys) {
                data[name.toString()] = jo.getInt(name.toString()).getGuildPermission()
            }
            val p = rs.getString("apply")
            val array = arrayListOf<String>()
            JSONArray.fromObject(p).forEach {
                array.add(it.toString())
            }
            val map = hashMapOf(
                    "n" to rs.getString("name"),
                    "l" to rs.getString("leader"),
                    "bp" to rs.getString("base"),
                    "c" to rs.getString("color"),
                    "o" to rs.getString("notice"),
                    "m" to members,
                    "b" to banned,
                    "a" to array,
                    "d" to data,
                    "ca" to rs.getDouble("capital"),
                    "lv" to rs.getInt("level"),
                    "exp" to rs.getInt("exp"),
                    "max" to rs.getInt("maximum")
            )
            rs.close()
            cs.close()
            connection.close()
            return map
        }
        set(value) {

        }

    var name: String
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT name FROM `guild_base` WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val name = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            return name
        }
        set(name) = Database.prepareStatement("UPDATE guild_base SET name = '$name' WHERE id = '$id';", true)

    var leader: String
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT leader FROM `guild_base` WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val leader = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            return leader
        }
        @Throws(SQLException::class)
        set(leader) = Database.prepareStatement("UPDATE guild_base SET leader = '$leader' WHERE id = '$id';", true)

    val basePos: String
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT base FROM `guild_base` WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val base = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            return base
        }

    var color: String
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT color FROM `guild_base` WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val color = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            return color
        }
        @Throws(SQLException::class)
        set(color) = Database.prepareStatement("UPDATE guild_base SET color = '$color' WHERE id = '$id';", true)

    var notice: String
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT notice FROM `guild_base` WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val notice = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            return notice
        }
        @Throws(SQLException::class)
        set(notice) = Database.prepareStatement("UPDATE guild_base SET notice = '$notice' WHERE id = '$id';", true)

    var members: ArrayList<String>
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT members FROM `guild_base` WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val m = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            val array = arrayListOf<String>()
            JSONArray.fromObject(m).forEach {
                array.add(it.toString())
            }
            return array
        }
        @Throws(SQLException::class)
        set(members) =
            Database.prepareStatement("UPDATE guild_base SET members = '" + JSONArray.fromObject(members) + "' WHERE id = '" + id + "';", true)

    var banned: ArrayList<String>
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT banned FROM `guild_base` WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val m = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            val array = arrayListOf<String>()
            JSONArray.fromObject(m).forEach {
                array.add(it.toString())
            }
            return array
        }
        @Throws(SQLException::class)
        set(banned) =
            Database.prepareStatement("UPDATE guild_base SET banned = '" + JSONArray.fromObject(banned) + "' WHERE id = '" + id + "';", true)

    var apply: ArrayList<String>
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT apply FROM `guild_base` WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val m = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            val array = arrayListOf<String>()
            JSONArray.fromObject(m).forEach {
                array.add(it.toString())
            }
            return array
        }
        @Throws(SQLException::class)
        set(apply) =
            Database.prepareStatement("UPDATE guild_base SET apply = '" + JSONArray.fromObject(apply) + "' WHERE id = '" + id + "';", true)

    var admins: HashMap<String, Guild.Permission>
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT admins FROM `guild_base` WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val m = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            val jo = JSONObject.fromObject(m)
            val data = HashMap<String, Guild.Permission>()
            for (name in jo.keys) {
                data[name.toString()] = jo.getInt(name.toString()).getGuildPermission()
            }
            return data
        }
        @Throws(SQLException::class)
        set(admins) {
            val a = HashMap<String, Int>()
            admins.forEach { n: String, o: Guild.Permission -> a[n] = o.level }
            Database.prepareStatement("UPDATE guild_base SET admins = '" + JSONObject.fromObject(a) + "' WHERE id = '" + id + "';", true)
        }

    var capital: Double
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT capital FROM `guild_base` WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val capital = rs.getDouble(1)
            rs.close()
            cs.close()
            connection.close()
            return capital
        }
        @Throws(SQLException::class)
        set(capital) = Database.prepareStatement("UPDATE guild_base SET capital = '$capital' WHERE id = '$id';", true)

    var level: Int
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT level FROM `guild_base` WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val l = rs.getInt(1)
            rs.close()
            cs.close()
            connection.close()
            return l
        }
        @Throws(SQLException::class)
        set(level) = Database.prepareStatement("UPDATE guild_base SET level = '$level' WHERE id = '$id';", true)

    var exp: Int
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT exp FROM `guild_base` WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val l = rs.getInt(1)
            rs.close()
            cs.close()
            connection.close()
            return l
        }
        @Throws(SQLException::class)
        set(exp) = Database.prepareStatement("UPDATE guild_base SET exp = '$exp' WHERE id = '$id';", true)

    var maximum: Int
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT maximum FROM `guild_base` WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val l = rs.getInt(1)
            rs.close()
            cs.close()
            connection.close()
            return l
        }
        @Throws(SQLException::class)
        set(maximum) = Database.prepareStatement("UPDATE guild_base SET maximum = '$maximum' WHERE id = '$id';", true)

    constructor(id: Int) {
        this.id = id
    }

    @Throws(SQLException::class)
    constructor(name: String, leader: String) {
        this.id = this.addGuild(name, leader)
    }

    @Throws(SQLException::class)
    fun existsID(): Boolean {
        return existsID(this.id)
    }

    @Throws(SQLException::class)
    private fun addGuild(name: String, leader: String): Int {
        if (existsName(name)) return -1
        val connection = Database.getMySQLConnection()
        val cs1 = connection.prepareCall("INSERT INTO `guild_base`(`name`,`leader`,`members`,`admins`,`banned`,`base`,`apply`) VALUES (?,?,?,?,?,?,?)") as DruidPooledCallableStatement
        cs1.setString(1, name)
        cs1.setString(2, leader)
        cs1.setString(3, "[]")
        cs1.setString(4, "{}")
        cs1.setString(5, "[]")
        cs1.setString(6, "")
        cs1.setString(7, "[]")
        cs1.execute()
        cs1.close()
        val cs2 = connection.prepareCall("SELECT LAST_INSERT_ID();") as DruidPooledCallableStatement
        val rs = cs2.executeQuery() as DruidPooledResultSet
        rs.next()
        val count = rs.getInt(1)
        rs.close()
        cs2.close()
        connection.close()
        return count
    }

    @Throws(SQLException::class)
    fun setBase(base: String) {
        Database.prepareStatement("UPDATE guild_base SET base = '$base' WHERE id = '$id';", true)
    }

    @Throws(SQLException::class)
    fun remove() {
        Database.prepareStatement("DELETE FROM `guild_base` WHERE id = '$id';", true)
    }

    companion object {

        @Throws(SQLException::class)
        fun newTable() {
            Database.newTable("CREATE TABLE IF NOT EXISTS `guild_base`  (" +
                    "  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '公会id'," +
                    "  `name` varchar(255) NOT NULL COMMENT '公会名称'," +
                    "  `leader` varchar(255) NULL COMMENT '公会会长id'," +
                    "  `admins` text NULL COMMENT '公会管理id+职务'," +
                    "  `members` mediumtext NULL COMMENT '公会成员id'," +
                    "  `banned` text NULL COMMENT '公会黑名单id'," +
                    "  `color` char(2) NULL DEFAULT '§f' COMMENT '公会名称颜色'," +
                    "  `base` varchar(255) NULL COMMENT '公会基地pos'," +
                    "  `notice` varchar(255) NULL DEFAULT '无公告' COMMENT '公会公告'," +
                    "  `maximum` int(5) UNSIGNED NULL DEFAULT 20 COMMENT '公会成员最大数量'," +
                    "  `apply` text NULL COMMENT '公会申请人员'," +
                    "  `level` int(3) NULL DEFAULT 1 COMMENT '公会等级'," +
                    "  `exp` int(9) UNSIGNED NULL DEFAULT 0 COMMENT '公会经验数量'," +
                    "  `capital` double(10, 2) NULL DEFAULT 0.00 COMMENT '公会资金'," +
                    "  PRIMARY KEY (`id`,`name`)" +
                    ");")
        }

        @Throws(SQLException::class)
        fun existsID(id: Int): Boolean {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT COUNT(*) FROM guild_base WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val count = rs.getInt(1)
            rs.close()
            cs.close()
            connection.close()
            return count >= 1
        }

        @Throws(SQLException::class)
        fun existsName(name: String): Boolean {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT COUNT(*) FROM guild_base WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val count = rs.getInt(1)
            rs.close()
            cs.close()
            connection.close()
            return count >= 1
        }
    }
}
