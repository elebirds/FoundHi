/*package net.mcpes.foundhi.plugin.currency.shop.shop.system

import cn.nukkit.Player
import cn.nukkit.form.response.FormResponse
import cn.nukkit.form.window.FormWindow
import net.mcpes.foundhi.plugin.currency.shop.shop.Shop
import net.mcpes.foundhi.plugin.currency.shop.shop.ShopBase
import net.sf.json.JSONObject

class RPGShop(id: Int) : ShopBase(id), Shop {
    override fun processData(player: Player?, data: FormResponse?, window: FormWindow?) {

    }

    override fun open(player: Player?): Boolean {
        return true
    }

    var rpgid: String? = null

    override fun getType(): Int {
        return ShopBase.SYSTEM_RPG_SHOP
    }

    override fun handle(player: Player, count: Int): Boolean {
        return true
    }

    override fun load(data: JSONObject) {
        this.rpgid = data.getString("RPGID")
    }

    override fun getJSONData(): JSONObject {
        val json = JSONObject()
        json["RPGID"] = this.rpgid
        return json
    }
}
*/