package net.mcpes.foundhi.plugin.currency.shop

import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.gui.window.lambda.defaults.LambdaTipGUI
import net.mcpes.foundhi.core.plugin.gui.window.lambda.defaults.LambdaVariableTipGUI
import net.mcpes.foundhi.core.plugin.modal.FoundHiPluginBase
import net.mcpes.foundhi.plugin.currency.shop.command.ShopCommand
import net.mcpes.foundhi.plugin.currency.shop.data.ShopDatabase
import net.mcpes.foundhi.plugin.currency.shop.listener.PlayerListener
import net.mcpes.foundhi.plugin.currency.shop.shop.ShopBase
import java.sql.SQLException

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/8/1
 * version 1.0
 */

class SShopMain : FoundHiPluginBase() {
    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {
        ShopBase.init()
        try {
            ShopDatabase.newTable()
            ShopDatabase.loadShops()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        initGUI()
        this.server.commandMap.register("shop", ShopCommand("shop"))
        this.server.pluginManager.registerEvents(PlayerListener(), this)
    }

    override fun onDisable() {
        ShopDataManager.saveAll()
    }

    private fun initGUI() {
        FoundHiPlugin.instance.addCommonGUI("common_error_money_not_enough", LambdaTipGUI("shop_money_tip_mde", "§a§l操作失败:所需货币不足!"))
        FoundHiPlugin.instance.addCommonGUI("common_error_bag_full", LambdaTipGUI("shop_system_buy_tip_bag", "你背包空余格数不足！"))
        FoundHiPlugin.instance.addCommonGUI("common_error_item_not_exists", LambdaTipGUI("shop_system_sell_tip_ine", "你的物品不足!"))
        FoundHiPlugin.instance.addCommonGUI("shop_system_sell_tip_success", LambdaVariableTipGUI("shop_system_sell_tip_success", "成功出售此物品,获得了%0"))
        FoundHiPlugin.instance.addCommonGUI("shop_system_buy_tip_success", LambdaVariableTipGUI("shop_system_buy_tip_success", "成功花费%0购买了%1个%2"))
    }

    companion object {
        const val TITLE = "§l§7|§bS§ah§co§ep§7| §6"
        lateinit var instance: SShopMain
            private set
    }
}
