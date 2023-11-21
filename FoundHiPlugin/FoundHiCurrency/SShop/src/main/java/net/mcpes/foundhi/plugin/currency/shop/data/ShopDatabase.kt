package net.mcpes.foundhi.plugin.currency.shop.data

import com.alibaba.druid.pool.DruidPooledCallableStatement
import com.alibaba.druid.pool.DruidPooledResultSet
import net.mcpes.foundhi.core.dao.Database
import net.mcpes.foundhi.plugin.currency.shop.ShopDataManager
import net.mcpes.foundhi.plugin.currency.shop.shop.ShopBase
import net.sf.json.JSONObject
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/21
 * version 1.0
 */
class ShopDatabase {
    companion object {
        @Throws(SQLException::class)
        @JvmStatic
        fun newTable() {
            val ps: PreparedStatement
            val con = Database.getMySQLConnection()
            ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS shop_data(" +
                    "    id int PRIMARY KEY COMMENT '商店id' AUTO_INCREMENT," +
                    "    pos varchar(255) NOT NULL COMMENT '商店位置'," +
                    "    type int DEFAULT 1 NOT NULL COMMENT '商店类型'," +
                    "    data text NOT NULL COMMENT '商店data'" +
                    ");")
            ps.executeUpdate()
            ps.close()
            con.close()
        }

        @Throws(SQLException::class)
        @JvmStatic
        fun exists(id: Int): Boolean {
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall("SELECT COUNT(*) FROM shop_data WHERE id = ?;") as DruidPooledCallableStatement
            cs.setInt(1, id)
            val rs = cs.executeQuery() as DruidPooledResultSet
            rs.next()
            val count = rs.getInt(1)
            rs.close()
            cs.close()
            connection.close()
            return count == 1
        }

        /**
         * 新建商店,返回商店id
         * */
        @Throws(SQLException::class)
        @JvmStatic
        fun addShop(shop: ShopBase): Int {
            val connection = Database.getMySQLConnection()
            val cs1 = connection.prepareCall("INSERT INTO `shop_data`(`type`,`data`) VALUES (?,?)") as DruidPooledCallableStatement
            cs1.setInt(1, shop.type)
            cs1.setString(2, shop.jsonData.toString())
            cs1.execute()
            cs1.close()
            //以上为新建商店数据
            val cs2 = connection.prepareCall("SELECT LAST_INSERT_ID();") as DruidPooledCallableStatement
            val rs = cs2.executeQuery() as DruidPooledResultSet
            rs.next()
            val count = rs.getInt(1)
            rs.close()
            cs2.close()
            connection.close()
            //以上为获取商店数据表自增id
            shop.id = count
            //重新设置商店id.
            return count
        }

        @Throws(SQLException::class)
        @JvmStatic
        fun delShop(id: Int): Int {
            Database.prepareStatement("DELETE FROM `shop_data` WHERE id = '$id';", false)
            return id
        }

        @Throws(SQLException::class)
        @JvmStatic
        fun loadShops() {
            val sql = "SELECT * FROM `shop_data`;"
            val connection = Database.getMySQLConnection()
            val cs = connection.prepareCall(sql) as DruidPooledCallableStatement
            val resultSet = cs.executeQuery()
            while (resultSet.next()) {
                val id = resultSet.getInt("id")
                val type: Int = resultSet.getInt("type")
                val data: JSONObject = JSONObject.fromObject(resultSet.getString("data"))
                val shop = ShopBase.getShop(type, id)!!
                shop.load(data)
                ShopDataManager.addShop(shop.id, shop.pos, shop)
            }
        }

        @Throws(SQLException::class)
        @JvmStatic
        fun setData(id: Int, data: JSONObject) {
            Database.prepareStatement("UPDATE `shop_data` SET data = '$data' WHERE id = '$id';", false)
        }
    }
}