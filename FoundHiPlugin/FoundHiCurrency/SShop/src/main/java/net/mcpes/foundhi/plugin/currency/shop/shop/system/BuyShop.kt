package net.mcpes.foundhi.plugin.currency.shop.shop.system

import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.core.plugin.gui.window.lambda.LambdaCustomGUI
import net.mcpes.foundhi.core.utils.MoneyUtils
import net.mcpes.foundhi.plugin.currency.shop.SShopMain
import net.mcpes.foundhi.plugin.currency.shop.handle.BuyShopHandle
import net.mcpes.foundhi.plugin.currency.shop.shop.ShopBase
import net.mcpes.foundhi.plugin.currency.shop.shop.`fun`.ItemShop
import net.mcpes.foundhi.plugin.currency.shop.utils.Utils
import java.util.function.BiConsumer
import java.util.function.Consumer

class BuyShop(id: Int) : ItemShop(id, true) {
    private val form = LambdaCustomGUI("shop_system_buy_main_$id", SShopMain.TITLE + "§f- §6系统出售商店")

    init {
        form.addText("t1", "§6购买:${this.itemName}}")
        form.addText("t2", "§6购买:§a${this.singleCount}个物品价格为${this.moneyMessage}}")
        form.addText("t3", "§b最少购买${this.singleCount}个 §c最多购买${this.singleCount * 64}个")
        form.addSlider("s1", "数量", this.singleCount.toFloat(), this.singleCount * 64f, this.singleCount, this.singleCount.toFloat())
        form.closedClickedListener = Consumer { }
        form.submittedClickedListener = BiConsumer { data, player ->
            if (player !is NukkitPlayer) return@BiConsumer
            val d = data["s1"] ?: return@BiConsumer
            val count = d.toString().toFloat()
            this.handle(player, count.toInt(), true)
        }
    }

    override fun getType(): Int {
        return ShopBase.SYSTEM_BUY_SHOP
    }

    override fun open(player: NukkitPlayer): Boolean {
        player.showGUI(form)
        return true
    }

    override fun onTouch(player: NukkitPlayer) {

    }

    override fun handle(player: NukkitPlayer, count: Int, gui: Boolean): Boolean {
        if (!super.handle(player, count, gui)) return false
        val item = this.getItem(count)
        if (player.inventory.canAddItem(item)) {
            MoneyUtils.delete("system", player.name, this.countString, this.typeString, BuyShopHandle(player.name, this, item, gui, count))
        } else {
            if (gui) {
                player.showGUI(FoundHiPlugin.instance.getCommonGUI("common_error_bag_full")!!)
            } else {
                player.sendMessage(Utils.getMessage("你背包空余格数不足！"))
            }
        }
        return true
    }
}
