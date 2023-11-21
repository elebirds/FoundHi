package net.mcpes.foundhi.plugin.currency.shop.handle

import cn.nukkit.Server
import cn.nukkit.item.Item
import net.mcpes.foundhi.core.handle.Handle
import net.mcpes.foundhi.core.handle.HandleResults
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.core.plugin.gui.window.lambda.defaults.LambdaVariableTipGUI
import net.mcpes.foundhi.plugin.currency.shop.shop.system.SellShop
import net.mcpes.foundhi.plugin.currency.shop.utils.Utils

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/2
 * version 1.0
 */

class SellShopHandle(private val target: String, private val shop: SellShop, private val item: Item, private val gui: Boolean, private val count: Int) : Handle() {
    override fun handle() {
        val target = (Server.getInstance().getPlayerExact(this.target) ?: return) as? NukkitPlayer ?: return
        if (!target.isOnline) return
        val inv = target.inventory
        when (this.result) {
            HandleResults.NORMAL_SUCCESS -> if (gui) {
                target.showGUI(FoundHiPlugin.instance.getCommonGUI("shop_system_sell_tip_success") as LambdaVariableTipGUI, shop.getMoneyMessage(count))
            } else {
                target.sendMessage(Utils.getMessage("成功出售此物品,获得了" + shop.getMoneyMessage(count)))
            }
            HandleResults.THE_OTHER_OFFLINE -> {
                inv.addItem(item)
                if (gui) {
                    target.showGUI(FoundHiPlugin.instance.getCommonGUI("idto")!!)
                } else {
                    target.sendMessage("§a§l操作失败:出现未知错误,请重新进入服务器再次尝试!")
                }
            }
            HandleResults.PARAMETER_ERROR -> {
                inv.addItem(item)
                if (gui) {
                    target.showGUI(FoundHiPlugin.instance.getCommonGUI("common_error_param")!!)
                } else {
                    target.sendMessage("§a§l操作失败:未知参数错误!")
                }
            }
        }
    }
}