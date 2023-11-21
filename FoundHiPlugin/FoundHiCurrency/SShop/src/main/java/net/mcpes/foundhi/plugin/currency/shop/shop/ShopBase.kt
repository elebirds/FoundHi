package net.mcpes.foundhi.plugin.currency.shop.shop

import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.plugin.currency.shop.SShopMain.Companion.TITLE
import net.mcpes.foundhi.plugin.currency.shop.ShopDataManager
import net.mcpes.foundhi.plugin.currency.shop.data.ShopDatabase
import net.mcpes.foundhi.plugin.currency.shop.shop.system.BuyShop
import net.mcpes.foundhi.plugin.currency.shop.shop.system.CommandShop
import net.mcpes.foundhi.plugin.currency.shop.shop.system.SellShop
import java.sql.SQLException

abstract class ShopBase(var id: Int) : Shop {
    var pos: String = ""

    override fun onTouch(player: NukkitPlayer) {
        val name = player.name
        if (ShopDataManager.isToucher(name) && ShopDataManager.getToucherShopId(name) == id) {
            this.open(player)
        } else {
            ShopDataManager.addToucher(name, id)
            player.sendMessage("$TITLE 请再点击一次来确认!")
        }
    }

    override fun save() {
        try {
            ShopDatabase.setData(this.id, this.jsonData)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    companion object {
        private val shopPool = arrayOfNulls<Class<*>>(256)

        @JvmStatic
        val SYSTEM_BUY_SHOP: Int = 0
        @JvmStatic
        val SYSTEM_SELL_SHOP: Int = 1
        @JvmStatic
        val SYSTEM_COMMAND_SHOP: Int = 2
        @JvmStatic
        val SYSTEM_RPG_SHOP: Int = 3

        @JvmStatic
        fun init() {
            shopPool[SYSTEM_BUY_SHOP] = BuyShop::class.java
            shopPool[SYSTEM_SELL_SHOP] = SellShop::class.java
            shopPool[SYSTEM_COMMAND_SHOP] = CommandShop::class.java
            //shopPool[SYSTEM_RPG_SHOP] = RPGShop::class.java
        }

        fun getShop(type: Int, id: Int): ShopBase? {
            val clazz = shopPool[type]
            if (clazz != null) {
                try {
                    return clazz.getConstructor(Int::class.java).newInstance(id) as ShopBase
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return null
        }
    }
}
