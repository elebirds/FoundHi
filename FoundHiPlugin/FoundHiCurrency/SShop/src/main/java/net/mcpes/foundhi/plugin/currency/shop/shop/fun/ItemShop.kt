package net.mcpes.foundhi.plugin.currency.shop.shop.`fun`

import cn.nukkit.item.Item
import net.mcpes.foundhi.plugin.currency.shop.utils.ItemNames
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/2
 * version 1.0
 */
abstract class ItemShop(id: Int, cost: Boolean) : MoneyShop(id, cost) {
    var itemID: Int = 0
    var itemDamage: Int = 0
    var singleCount: Int = 0

    var itemName: String = ""
        get() = if (field == "") ItemNames.getItemName(itemID, itemDamage) else field

    fun getItem(count: Int): Item {
        return Item.get(itemID, itemDamage, singleCount * count)
    }

    override fun load(data: JSONObject) {
        super.load(data)
        this.singleCount = data.getInt("count")
        this.itemID = data.getInt("id")
        this.itemDamage = data.getInt("damage")
        this.itemName = data.getString("iname")
    }

    override fun getJSONData(): JSONObject {
        val json = super.getJSONData()
        json["id"] = itemID
        json["damage"] = itemDamage
        json["count"] = singleCount
        json["iname"] = itemName
        return json
    }
}