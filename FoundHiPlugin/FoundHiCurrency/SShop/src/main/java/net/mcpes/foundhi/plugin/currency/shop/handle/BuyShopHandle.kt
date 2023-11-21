package net.mcpes.foundhi.plugin.currency.shop.handle

import cn.nukkit.Server
import cn.nukkit.item.Item
import net.mcpes.foundhi.core.handle.Handle
import net.mcpes.foundhi.core.handle.HandleResults
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.core.plugin.gui.window.lambda.defaults.LambdaVariableTipGUI
import net.mcpes.foundhi.plugin.currency.shop.shop.system.BuyShop
import net.mcpes.foundhi.plugin.currency.shop.utils.Utils

class BuyShopHandle(private val target: String, private val shop: BuyShop, private val item: Item, private val gui: Boolean, private val count: Int) : Handle() {
    override fun handle() {
        val target = (Server.getInstance().getPlayerExact(this.target) ?: return) as? NukkitPlayer ?: return
        if (!target.isOnline) return
        when (this.result) {
            HandleResults.NORMAL_SUCCESS -> {
                val inv = target.inventory
                if (inv.canAddItem(item)) {
                    inv.addItem(item)
                    if (gui) {
                        target.showGUI(FoundHiPlugin.instance.getCommonGUI("shop_system_buy_tip_success") as LambdaVariableTipGUI, shop.getMoneyMessage(count), item.count, shop.itemName)
                    } else {
                        target.sendMessage(Utils.getMessage("成功花费" + shop.getMoneyMessage(count) + "购买了" + item.count + "个" + shop.itemName))
                    }
                }
            }
            HandleResults.THE_OTHER_OFFLINE -> if (gui) target.showGUI(FoundHiPlugin.instance.getCommonGUI("common_error_idto")!!) else target.sendMessage("§a§l操作失败:出现未知错误,请重新进入服务器再次尝试!")
            HandleResults.PARAMETER_ERROR -> if (gui) target.showGUI(FoundHiPlugin.instance.getCommonGUI("common_error_param")!!) else target.sendMessage("§a§l操作失败:未知参数错误!")
            HandleResults.NOT_ENOUGH_MONEY -> if (gui) target.showGUI(FoundHiPlugin.instance.getCommonGUI("common_error_money_not_enough")!!) else target.sendMessage("§a§l操作失败:所需货币不足!")
        }
    }
}