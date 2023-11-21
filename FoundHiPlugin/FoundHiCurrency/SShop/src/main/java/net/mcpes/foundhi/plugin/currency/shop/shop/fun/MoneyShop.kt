package net.mcpes.foundhi.plugin.currency.shop.shop.`fun`

import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.core.utils.getMoneyName
import net.mcpes.foundhi.plugin.currency.shop.shop.ShopBase
import net.sf.json.JSONArray
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/2
 * version 1.0
 */
abstract class MoneyShop(id: Int, val cost: Boolean) : ShopBase(id) {
    var money1: Double = 0.0
    var money2: Double = 0.0
    var money3: Double = 0.0
    protected var mTypes: ArrayList<Int> = arrayListOf()
    protected var mCounts: ArrayList<Double> = arrayListOf()
    protected var typeString = ""
    protected var countString = ""

    var moneyMessage: String = ""

    override fun load(data: JSONObject) {
        this.money1 = data.getInt("money1").toDouble()
        this.money2 = data.getInt("money2").toDouble()
        this.money3 = data.getInt("money3").toDouble()
        if (this.money1 != 0.0) {
            mTypes.add(1)
            mCounts.add(this.money1)
        }
        if (this.money2 != 0.0) {
            mTypes.add(2)
            mCounts.add(this.money2)
        }
        if (this.money3 != 0.0) {
            mTypes.add(3)
            mCounts.add(this.money3)
        }
        this.typeString = JSONArray.fromObject(mTypes).toString()
        this.countString = JSONArray.fromObject(mCounts).toString()
        if (money1 != 0.0) {
            moneyMessage += "${money1}枚${1.getMoneyName()}"
        }
        if (money2 != 0.0) {
            if (moneyMessage != "") moneyMessage += ","
            moneyMessage += "${money2}枚${2.getMoneyName()}"
        }
        if (money3 != 0.0) {
            if (moneyMessage != "") moneyMessage += ","
            moneyMessage += "${money3}枚${3.getMoneyName()}"
        }
        if (moneyMessage == "") {
            moneyMessage = "免费"
        }
    }

    fun getMoneyMessage(count: Int): String {
        if (count == 0) return "免费"
        if (count == 1) return moneyMessage
        var moneyMessage = ""
        if (money1 != 0.0) {
            moneyMessage += "${money1}枚${1.getMoneyName()}"
        }
        if (money2 != 0.0) {
            if (moneyMessage != "") moneyMessage += ","
            moneyMessage += "${money2}枚${2.getMoneyName()}"
        }
        if (money3 != 0.0) {
            if (moneyMessage != "") moneyMessage += ","
            moneyMessage += "${money3}枚${3.getMoneyName()}"
        }
        if (moneyMessage == "") {
            moneyMessage = "免费"
        }
        return moneyMessage
    }

    override fun getJSONData(): JSONObject {
        val json = JSONObject()
        json["money1"] = this.money1
        json["money2"] = this.money2
        json["money3"] = this.money3
        return json
    }

    override fun handle(player: NukkitPlayer, count: Int, gui: Boolean): Boolean {
        if (!cost) return true
        val data = FoundHiPlugin.instance.getPlayer(player.name)!!
        val need1 = money1 * count
        val need2 = money2 * count
        val need3 = money3 * count
        if (data.money1 < need1 || data.money2 < need2 || data.money3 < need3) {
            if (gui) {
                player.showGUI(FoundHiPlugin.instance.getCommonGUI("common_error_money_not_enough")!!)
            } else {
                player.sendMessage("§a§l操作失败:所需货币不足!")
            }
            return false
        }
        return true
    }
}