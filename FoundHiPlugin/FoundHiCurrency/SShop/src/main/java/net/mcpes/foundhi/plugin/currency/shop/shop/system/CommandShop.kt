package net.mcpes.foundhi.plugin.currency.shop.shop.system

import cn.nukkit.form.window.FormWindowModal
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.core.utils.MoneyUtils
import net.mcpes.foundhi.plugin.currency.shop.SShopMain
import net.mcpes.foundhi.plugin.currency.shop.handle.CommandShopHandle
import net.mcpes.foundhi.plugin.currency.shop.shop.ShopBase
import net.mcpes.foundhi.plugin.currency.shop.shop.`fun`.MoneyShop
import net.sf.json.JSONObject

class CommandShop(id: Int) : MoneyShop(id, true) {
    var name: String = ""
    var commands: ArrayList<String> = ArrayList()
    var mode: Byte = 0

    override fun getType(): Int {
        return ShopBase.SYSTEM_COMMAND_SHOP
    }

    override fun open(player: NukkitPlayer): Boolean {
        val form = FormWindowModal(SShopMain.TITLE + "§f- §6命令购买商店", "§6购买:$name\n§d花费:$moneyMessage", "确定", "取消")
        //DataManager.inWindow[player.name] = player.showFormWindow(form)
        //DataManager.inShop[player.name] = this.id
        return true
    }

    override fun load(data: JSONObject) {
        super.load(data)
        this.name = data.getString("name")
        data.getJSONArray("commands").forEach {
            commands.add(it.toString())
        }
        this.mode = data.getInt("mode").toByte()
    }

    override fun getJSONData(): JSONObject {
        val json = super.getJSONData()
        json["commands"] = this.commands
        json["mode"] = this.mode
        json["name"] = this.name
        return json
    }

    override fun onTouch(player: NukkitPlayer) {

    }

    override fun handle(player: NukkitPlayer, count: Int, gui: Boolean): Boolean {
        if (!super.handle(player, count, gui)) return false
        MoneyUtils.delete("系统", player.name, this.money1 * count, 1, true, CommandShopHandle(player.name, this))
        return true
    }

    companion object {
        const val OP_COMMAND: Byte = 0
        const val CONSOLE_COMMAND: Byte = 1
        const val PLAYER_COMMAND: Byte = 2
    }
}
