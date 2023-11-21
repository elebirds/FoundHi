package net.mcpes.foundhi.core.dao.player

import com.alibaba.druid.pool.DruidPooledCallableStatement
import com.alibaba.druid.pool.DruidPooledResultSet
import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.data.mail.Mail
import net.mcpes.foundhi.core.dao.Database
import net.mcpes.foundhi.core.utils.cast
import net.sf.json.JSONObject
import java.sql.SQLException

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/19
 * version 1.0
 */
class PlayerMailDatabase @Throws(SQLException::class)
constructor(val name: String) {
    var allData: HashMap<String, Any>
        @Throws(SQLException::class)
        get() {
            val data: HashMap<String, Any> = HashMap()
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT * FROM `player_mail` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery()
            rs.next()
            data["ou"] = rs.getString("outbox")
            data["in"] = rs.getString("inbox")
            return data
        }
        private set(value) {

        }
    var inbox: HashMap<Mail, Int>
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT `inbox` FROM `player_mail` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val data = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            val map = HashMap<Mail, Int>()
            JSONObject.fromObject(data).forEach { k, v ->
                if (k is String) {
                    val time: Int = v.cast()
                    if (!APIFunction.instance.hasMail(k)) {
                        val mail = Mail(time, k)
                        APIFunction.instance.addMail(mail)
                        map[mail] = time
                    } else {
                        map[APIFunction.instance.getMail(k)!!] = time
                    }
                }
            }
            return map
        }
        @Throws(SQLException::class)
        set(value) {
            val data = JSONObject.fromObject(value).toString()
            Database.prepareStatement("UPDATE player_mail SET inbox = '$value' WHERE name = '$name';", true)
            Database.hashUptate(this.name, "in", data)
        }
    var outbox: HashMap<Mail, Int>
        @Throws(SQLException::class)
        get() {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT `outbox` FROM `player_mail` WHERE name = ?;") as DruidPooledCallableStatement
            cs.setString(1, name)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val data = rs.getString(1)
            rs.close()
            cs.close()
            connection.close()
            val map = HashMap<Mail, Int>()
            JSONObject.fromObject(data).forEach { k, v ->
                if (k is String) {
                    val time: Int = v.cast()
                    if (!APIFunction.instance.hasMail(k)) {
                        val mail = Mail(time, k)
                        APIFunction.instance.addMail(mail)
                        map[mail] = time
                    } else {
                        map[APIFunction.instance.getMail(k)!!] = time
                    }
                }
            }
            return map
        }
        @Throws(SQLException::class)
        set(value) {
            val data = JSONObject.fromObject(value).toString()
            Database.prepareStatement("UPDATE player_mail SET outbox = '$value' WHERE name = '$name';", true)
            Database.hashUptate(this.name, "ou", data)
        }

    companion object {
        @Throws(SQLException::class)
        @JvmStatic
        fun newTable() {
            Database.newTable("CREATE TABLE IF NOT EXISTS player_mail(" +
                    "    name varchar(255) PRIMARY KEY NOT NULL," +
                    "    inbox longtext NOT NULL," +
                    "    outbox longtext NOT NULL" +
                    ");")
        }

        @Throws(SQLException::class)
        fun exists(name: String): Boolean {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT COUNT(*) FROM player_mail WHERE name = ?;") as DruidPooledCallableStatement
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
    fun exists(): Boolean {
        return exists(this.name)
    }

    @Throws(SQLException::class)
    fun delPlayer() {
        if (!exists()) return
        val connection = Database.getMySQLConnection()
        val cs = connection.prepareCall("DELETE FROM player_mail WHERE name = ?;") as DruidPooledCallableStatement
        cs.setString(1, name)
        cs.execute()
        cs.close()
        connection.close()
    }

    @Throws(SQLException::class)
    fun addPlayer() {
        if (this.exists()) return
        val connection = Database.getMySQLConnection()
        val cs1 = connection.prepareCall("INSERT INTO `player_mail`(`name`,`inbox`,`outbox`) VALUES (?,?,?)") as DruidPooledCallableStatement
        cs1.setString(1, name)
        cs1.setString(2, "{}")
        cs1.setString(3, "{}")
        cs1.execute()
        cs1.close()
        connection.close()
    }
}
