package net.mcpes.foundhi.core.dao.banned

import com.alibaba.druid.pool.DruidPooledCallableStatement
import com.alibaba.druid.pool.DruidPooledResultSet
import net.mcpes.foundhi.core.app.data.banned.BannedInfo
import net.mcpes.foundhi.core.dao.Database
import java.sql.SQLException
import java.sql.Timestamp

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/24 下午6:18
 * version 1.0
 */
class BannedPlayerDatabase {
    companion object {
        @JvmStatic
        @Throws(SQLException::class)
        fun newTable() {
            Database.newTable("CREATE TABLE IF NOT EXISTS player_banned (" +
                    "    id int PRIMARY KEY AUTO_INCREMENT," +
                    "    operator varchar(255) NOT NULL," +
                    "    banned varchar(255) NOT NULL," +
                    "    start_time datetime NOT NULL," +
                    "    end_time datetime NOT NULL," +
                    "    reason varchar(255) NOT NULL," +
                    "    be_lifted bool DEFAULT false," +
                    "    reviser varchar(255) NOT NULL" +
                    ");")
        }

        @JvmStatic
        @Throws(SQLException::class)
        fun load() {
            val sql = "SELECT * FROM `player_banned`;"
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall(sql) as DruidPooledCallableStatement
            val resultSet = cs.executeQuery()
            val map = HashMap<String, String>()
            while (resultSet.next()) {
                val key = resultSet.getInt("id")
                val info = BannedInfo(key, resultSet)
                map[info.id.toString()] = info.toString()
            }
            if (!Database.isEnableRedis()) return
            val redis = Database.getRedisConnection()
            redis.del("banned_player_init")
            redis.hmset("banned_player_init", map)
            redis.close()
        }

        @JvmStatic
        @Throws(SQLException::class)
        fun add(bannedInfo: BannedInfo): Int {
            val connection = Database.getMySQLConnection()
            val cs1 = connection.prepareCall("INSERT INTO `player_banned` (`operator`,`banned`,`reason`,`be_lifted`,`start_time`,`end_time`,`reviser`) VALUES (?,?,?,?,?,?,?)") as DruidPooledCallableStatement
            cs1.setString(1, bannedInfo.operator)
            cs1.setString(2, bannedInfo.banned)
            cs1.setString(3, bannedInfo.reason)
            cs1.setBoolean(4, bannedInfo.beLifted)
            cs1.setTimestamp(5, Timestamp(bannedInfo.startTime.time))
            cs1.setTimestamp(6, Timestamp(bannedInfo.endTime.time))
            cs1.setString(7, bannedInfo.reviser)
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

        @JvmStatic
        @Throws(SQLException::class)
        fun remove(bannedInfo: BannedInfo) {
            val connection = Database.getMySQLConnection()
            val cs1 = connection.prepareCall("DELETE from player_banned WHERE id = ?;") as DruidPooledCallableStatement
            cs1.setInt(1, bannedInfo.id)
            cs1.execute()
            cs1.close()
            connection.close()
        }

        @JvmStatic
        @Throws(SQLException::class)
        fun save(bannedInfo: BannedInfo) {
            val connection = Database.getMySQLConnection()
            val cs1 = connection.prepareCall("UPDATE player_banned SET `end_time` = ?,`reviser` = ?,`be_lifted` = ? WHERE id = ?") as DruidPooledCallableStatement
            cs1.setTimestamp(1, Timestamp(bannedInfo.endTime.time))
            cs1.setBoolean(3, bannedInfo.beLifted)
            cs1.setString(2, bannedInfo.reviser)
            cs1.setInt(4, bannedInfo.id)
            cs1.execute()
            cs1.close()
            connection.close()
            if (!Database.isEnableRedis()) return
            val redis = Database.getRedisConnection()
            redis.hset("banned_player_init", bannedInfo.id.toString(), bannedInfo.toString())
            redis.close()
        }
    }
}