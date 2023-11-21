package net.mcpes.foundhi.plugin.currency.login.data

import com.alibaba.druid.pool.DruidPooledCallableStatement
import com.alibaba.druid.pool.DruidPooledConnection
import com.alibaba.druid.pool.DruidPooledResultSet
import net.mcpes.foundhi.core.dao.Database
import java.sql.SQLException
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/26
 * version 1.0
 */
class PlayerLoginDatabase(val name: String) {
    var all: HashMap<String, Any>
        @Throws(SQLException::class)
        get() {
            if (!exists()) return hashMapOf()
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT * FROM login WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            return hashMapOf(
                    "pw" to rs.getString("password"),
                    "xbox" to rs.getString("xuid"),
                    "li" to rs.getString("lastip"),
                    "cid" to rs.getLong("cid"),
                    "regdate" to rs.getLong("regdate"),
                    "lastdate" to rs.getLong("lastdate")
            )
        }
        set(value) {}

    var password: String
        @Throws(SQLException::class)
        get() {
            if (!exists()) return ""
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT password FROM login WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val word = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            return word
        }
        @Throws(SQLException::class)
        set(password) {
            val sql = "UPDATE login SET password = $password WHERE name = '$name';"
            Database.prepareStatement(sql, false)
        }

    var lastIP: String
        @Throws(SQLException::class)
        get() {
            if (!exists()) return ""
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT lastip FROM login WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val word = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            return word
        }
        @Throws(SQLException::class)
        set(ip) {
            val sql = "UPDATE login SET lastip = $ip WHERE name = '$name';"
            Database.prepareStatement(sql, false)
        }

    var xboxID: String
        @Throws(SQLException::class)
        get() {
            if (!exists()) return ""
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT xuid FROM login WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val word = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            return word
        }
        @Throws(SQLException::class)
        set(xuid) {
            val sql = "UPDATE login SET xuid = $xuid WHERE name = '$name';"
            Database.prepareStatement(sql, false)
        }

    var clientID: Long
        @Throws(SQLException::class)
        get() {
            if (!exists()) return 0
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT cid FROM login WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val word = rs.getLong(1)
            rs.close()
            cs.close()
            connection.close()
            return word
        }
        @Throws(SQLException::class)
        set(cid) {
            val sql = "UPDATE login SET cid = $cid WHERE name = '$name';"
            Database.prepareStatement(sql, false)
        }

    val registerDate: Date
        @Throws(SQLException::class)
        get() {
            if (!exists()) return Date()
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT regdate FROM login WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val word = rs.getLong(1)
            rs.close()
            cs.close()
            connection.close()
            return Date(word)
        }

    var lastDate: Date
        @Throws(SQLException::class)
        get() {
            if (!exists()) return Date()
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT lastdate FROM login WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val word = rs.getLong(1)
            rs.close()
            cs.close()
            connection.close()
            return Date(word)
        }
        @Throws(SQLException::class)
        set(date) {
            val sql = "UPDATE login SET lastdate = " + date.time + " WHERE name = '" + name + "';"
            Database.prepareStatement(sql, false)
        }

    @Throws(SQLException::class)
    fun exists(): Boolean {
        return exists(this.name)
    }

    @Throws(SQLException::class)
    fun addPlayer(password: String, date: Date, ip: String): Boolean {
        if (this.exists()) return false
        val sql = "INSERT INTO login (name, password, regdate, lastDate, lastIP) VALUES (?,?,?,?,?);"
        val con = connect()
        val cs = con.prepareCall(sql) as DruidPooledCallableStatement
        cs.setString(1, name)
        cs.setString(2, password)
        cs.setLong(3, date.time)
        cs.setLong(4, date.time)
        cs.setString(5, ip)
        cs.execute()
        cs.close()
        con.close()
        return true
    }

    @Throws(SQLException::class)
    fun delPlayer(): Boolean {
        if (!this.exists()) return false
        val sql = "DELETE FROM login WHERE name = ?;"
        val con = connect()
        val cs = con.prepareCall(sql) as DruidPooledCallableStatement
        cs.setString(1, name)
        cs.execute()
        cs.close()
        con.close()
        return true
    }

    companion object {
        @Throws(SQLException::class)
        fun connect(): DruidPooledConnection {
            return Database.getMySQLConnection()
        }

        fun newTable() {
            val sql = "CREATE TABLE IF NOT EXISTS `login`  (" +
                    "  `name` VARCHAR(255) NOT NULL COMMENT '名称'," +
                    "  `password` VARCHAR(255) NULL COMMENT '密码'," +
                    "  `xuid` VARCHAR(255) NULL COMMENT 'Xbox-ID'," +
                    "  `cid` BIGINT(0) NULL COMMENT '客户端ID'," +
                    "  `regdate` BIGINT(0) NULL COMMENT '注册时间'," +
                    "  `lastdate` BIGINT(0) NULL COMMENT '上次登陆时间'," +
                    "  `lastip` VARCHAR(255) NULL COMMENT '上次登陆ip'," +
                    "  PRIMARY KEY (`name`));"
            Database.prepareStatement(sql, false)
        }

        @Throws(SQLException::class)
        fun exists(name: String): Boolean {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT COUNT(*)FROM login WHERE name = ?;") as DruidPooledCallableStatement
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
