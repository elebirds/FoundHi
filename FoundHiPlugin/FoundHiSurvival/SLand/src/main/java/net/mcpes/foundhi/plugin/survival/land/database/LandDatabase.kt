package net.mcpes.foundhi.plugin.survival.land.database

import com.alibaba.druid.pool.DruidPooledCallableStatement
import com.alibaba.druid.pool.DruidPooledResultSet
import net.mcpes.foundhi.core.dao.Database
import net.mcpes.foundhi.core.dao.Database.getMySQLConnection
import net.mcpes.foundhi.plugin.survival.land.SLand
import net.mcpes.foundhi.plugin.survival.land.land.LandWorld
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * SLand
 *
 * @author hhm Copyright (c) 2018/5/3
 * version 1.0
 */
class LandDatabase {
    companion object {
        @Throws(SQLException::class)
        fun initMainTable() {
            val ps: PreparedStatement
            val con = getMySQLConnection()
            ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS `land_main_table`  (" +
                    "  `id` int(255) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '领地世界ID'," +
                    "  `level` varchar(255) NOT NULL DEFAULT 'world' COMMENT '世界名称'," +
                    "  `name` varchar(255) NOT NULL DEFAULT '地皮世界' COMMENT '领地区域名称'," +
                    "  `type` int(2) UNSIGNED NOT NULL DEFAULT 0 COMMENT '领地世界类型'," +
                    "  `money` double(10, 2) NOT NULL DEFAULT 1000.00 COMMENT '花费货币数量'," +
                    "  `mtype` int(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '货币类型'," +
                    "  PRIMARY KEY (`id`)" +
                    ");")
            ps.executeUpdate()
            ps.close()
            con.close()
        }

        @Throws(SQLException::class)
        fun addLandWorld(level: String, name: String, type: Int, money: Double, mtype: Int): Int {
            val connection = Database.getMySQLConnection()
            val cs1 = connection.prepareCall("INSERT INTO `land_main_table`(`level`,`type`,`money`,`mtype`,`name`) VALUES (?,?,?,?,?)") as DruidPooledCallableStatement
            cs1.setString(1, level)
            cs1.setInt(2, type)
            cs1.setDouble(3, money)
            cs1.setInt(4, mtype)
            cs1.setString(5, name)
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
        fun initLandWorlds() {
            val sql = "SELECT * FROM `land_main_table`;"
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall(sql) as DruidPooledCallableStatement
            val resultSet = cs.executeQuery()
            while (resultSet.next()) {
                val id = resultSet.getInt("id")
                val level = resultSet.getString("level")
                val name = resultSet.getString("name")
                val type = resultSet.getInt("type")
                val mt = resultSet.getInt("mtype")
                val money = resultSet.getDouble("money")
                SLand.landWorlds[id] = LandWorld(id, level, name, type, money, mt)
                SLand.landWorldCorresponding[level] = id
            }
        }

        @Throws(SQLException::class)
        fun initLandTable(id: Int) {
            val ps: PreparedStatement
            val con = getMySQLConnection()
            ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS `land_$id`  (" +
                    "  `id` varchar(255) NOT NULL COMMENT '领地id'," +
                    "  `owner` varchar(255) NOT NULL COMMENT '领地主人'," +
                    "  `x1` int(255) NOT NULL COMMENT '领地坐标-X1'," +
                    "  `x2` int(255) NOT NULL COMMENT '领地坐标-X2'," +
                    "  `z1` int(255) NOT NULL COMMENT '领地坐标-Z1'," +
                    "  `z2` int(255) NOT NULL COMMENT '领地坐标-Z2'," +
                    "  `guest` text NOT NULL COMMENT '领地访客'," +
                    "  `permission` text NOT NULL COMMENT '领地权限'," +
                    "  PRIMARY KEY (`id`)" +
                    ");")
            ps.executeUpdate()
            ps.close()
            con.close()
        }

        @Throws(SQLException::class)
        fun setName(id: Int, name: String) {
            val con = getMySQLConnection()
            val ps: PreparedStatement = con.prepareStatement("UPDATE land_main_table SET name = '$name' WHERE id = '$id';")
            ps.executeUpdate()
            ps.close()
            con.close()
        }

        @Throws(SQLException::class)
        fun setMoney(id: Int, money: Double) {
            val con = getMySQLConnection()
            val ps: PreparedStatement = con.prepareStatement("UPDATE land_main_table SET money = '$money' WHERE id = '$id';")
            ps.executeUpdate()
            ps.close()
            con.close()
        }

        @Throws(SQLException::class)
        fun setMoneyType(id: Int, mtype: Int) {
            val con = getMySQLConnection()
            val ps: PreparedStatement = con.prepareStatement("UPDATE land_main_table SET mtype = '$mtype' WHERE id = '$id';")
            ps.executeUpdate()
            ps.close()
            con.close()
        }
    }
}