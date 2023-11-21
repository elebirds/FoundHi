package net.mcpes.foundhi.core.dao.mail

import com.alibaba.druid.pool.DruidPooledCallableStatement
import com.alibaba.druid.pool.DruidPooledResultSet
import net.mcpes.foundhi.core.app.data.mail.Mail
import net.mcpes.foundhi.core.dao.Database
import net.sf.json.JSONObject
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.collections.HashMap
import kotlin.collections.set

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/19
 * version 1.0
 */
object MailDatabase {
    private var dbName: String = ""
    private val sdf = SimpleDateFormat("yyyyMMdd")
    private var lastDate: String = ""

    @Throws(SQLException::class)
    @JvmStatic
    fun setDatabaseName() {
        val date = sdf.format(Date())
        if (date != lastDate) {
            MailDatabase.dbName = "mail_$date"
            lastDate = date
            MailDatabase.newTable()
        }
    }

    @Throws(SQLException::class)
    @JvmStatic
    private fun newTable() {
        Database.newTable("CREATE TABLE IF NOT EXISTS " + dbName + "(" +
                "    id varchar(255) PRIMARY KEY NOT NULL," +
                "    type int DEFAULT 1," +
                "    sender varchar(255) NOT NULL," +
                "    content text NOT NULL," +
                "    attachment text NOT NULL," +
                "    addressee varchar(255) NOT NULL," +
                "    `read` int DEFAULT 0" +
                ");")
    }

    @Throws(SQLException::class)
    @JvmStatic
    fun addMail(mail: Mail) {
        val connection = Database.getMySQLConnection()
        val cs1 = connection.prepareCall("INSERT INTO `$dbName`(`id`,`type`,`sender`,`content`,`addressee`,`attachment`) VALUES (?,?,?,?,?,?)") as DruidPooledCallableStatement
        cs1.setString(1, mail.id)
        cs1.setInt(2, mail.type)
        cs1.setString(3, mail.sender)
        cs1.setString(4, mail.content)
        cs1.setString(5, mail.addressee)
        cs1.setString(6, JSONObject.fromObject(mail.attachment).toString())
        cs1.execute()
        cs1.close()
        connection.close()
    }

    @Throws(SQLException::class)
    @JvmStatic
    fun getType(id: String, dbName: String): Int {
        val connection = Database.getMySQLConnection()
        val cs = connection.prepareCall("SELECT `type` FROM `$dbName` WHERE id = ?;") as DruidPooledCallableStatement
        cs.setString(1, id)
        val rs = cs.executeQuery() as DruidPooledResultSet
        rs.next()
        val type = rs.getInt(1)
        rs.close()
        cs.close()
        connection.close()
        return type
    }

    @Throws(SQLException::class)
    @JvmStatic
    fun getReadStatus(id: String, dbName: String): Int {
        val connection = Database.getMySQLConnection()
        val cs = connection.prepareCall("SELECT `read` FROM `$dbName` WHERE id = ?;") as DruidPooledCallableStatement
        cs.setString(1, id)
        val rs = cs.executeQuery() as DruidPooledResultSet
        rs.next()
        val type = rs.getInt(1)
        rs.close()
        cs.close()
        connection.close()
        return type
    }

    @Throws(SQLException::class)
    @JvmStatic
    fun setReadStatus(id: String, dbName: String, read: Boolean) {
        val status = if (read) {
            1
        } else {
            0
        }
        Database.prepareStatement("UPDATE `$dbName` SET `read`= '$status' WHERE id = $id;", true)
    }

    @Throws(SQLException::class)
    @JvmStatic
    fun getSender(id: String, dbName: String): String {
        val connection = Database.getMySQLConnection()
        val cs = connection.prepareCall("SELECT `sender` FROM `$dbName` WHERE id = ?;") as DruidPooledCallableStatement
        cs.setString(1, id)
        val rs = cs.executeQuery() as DruidPooledResultSet
        rs.next()
        val sender = rs.getString(1)
        rs.close()
        cs.close()
        connection.close()
        return sender
    }

    @Throws(SQLException::class)
    @JvmStatic
    fun getAttachment(id: String, dbName: String): String {
        val connection = Database.getMySQLConnection()
        val cs = connection.prepareCall("SELECT `attachment` FROM `$dbName` WHERE id = ?;") as DruidPooledCallableStatement
        cs.setString(1, id)
        val rs = cs.executeQuery() as DruidPooledResultSet
        rs.next()
        val attachment = rs.getString(1)
        rs.close()
        cs.close()
        connection.close()
        return attachment
    }

    @Throws(SQLException::class)
    @JvmStatic
    fun getAttachmentMap(id: String, dbName: String): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        JSONObject.fromObject(getAttachment(id, dbName)).forEach { k, v -> map[k.toString()] = v.toString().toInt() }
        return map
    }

    @Throws(SQLException::class)
    @JvmStatic
    fun getContent(id: String, dbName: String): String {
        val connection = Database.getMySQLConnection()
        val cs = connection.prepareCall("SELECT `content` FROM `$dbName` WHERE id = ?;") as DruidPooledCallableStatement
        cs.setString(1, id)
        val rs = cs.executeQuery() as DruidPooledResultSet
        rs.next()
        val content = rs.getString(1)
        rs.close()
        cs.close()
        connection.close()
        return content
    }

    @Throws(SQLException::class)
    @JvmStatic
    fun getAddressee(id: String, dbName: String): String {
        val connection = Database.getMySQLConnection()
        val cs = connection.prepareCall("SELECT `addressee` FROM `$dbName` WHERE id = ?;") as DruidPooledCallableStatement
        cs.setString(1, id)
        val rs = cs.executeQuery() as DruidPooledResultSet
        rs.next()
        val addressee = rs.getString(1)
        rs.close()
        cs.close()
        connection.close()
        return addressee
    }
}
