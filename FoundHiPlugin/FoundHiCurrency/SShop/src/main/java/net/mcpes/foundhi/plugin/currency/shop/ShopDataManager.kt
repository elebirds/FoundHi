package net.mcpes.foundhi.plugin.currency.shop

import net.mcpes.foundhi.plugin.currency.shop.data.ShopDatabase
import net.mcpes.foundhi.plugin.currency.shop.shop.ShopBase
import java.sql.SQLException
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/28
 * version 1.0
 */

object ShopDataManager {
    private val shops = HashMap<Int, ShopBase>()
    private val shopPos = HashMap<String, Int>()
    private val touch = HashMap<String, Int>()

    fun saveAll() {
        shops.values.forEach(ShopBase::save)
    }

    fun existsShop(pos: String) = shopPos.containsKey(pos)

    fun existsShop(id: Int) = shops.containsKey(id)

    fun getShop(id: Int) = shops[id]

    fun getShop(pos: String) = shops[getShopID(pos)!!]

    fun getShopID(pos: String) = shopPos[pos]

    fun addShop(id: Int, pos: String, shop: ShopBase) {
        if (pos != "") shopPos[pos] = id
        shops[id] = shop
    }

    @Throws(SQLException::class)
    fun newShop(pos: String, shop: ShopBase) {
        val id = ShopDatabase.addShop(shop)
        shop.id = id
        this.addShop(id, pos, shop)
    }

    fun removeShop(shop: ShopBase): Boolean {
        if (!this.existsShop(shop.id) || !this.existsShop(shop.pos)) return false
        shops.remove(shop.id)
        shopPos.remove(shop.pos)
        try {
            ShopDatabase.delShop(shop.id)
        } catch (e: SQLException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun isToucher(playerName: String): Boolean {
        return touch.containsKey(playerName)
    }

    fun addToucher(playerName: String, shopId: Int) {
        touch[playerName] = shopId
    }

    fun removeToucher(playerName: String) {
        touch.remove(playerName)
    }

    fun getToucherShopId(playerName: String) = touch[playerName]
}
