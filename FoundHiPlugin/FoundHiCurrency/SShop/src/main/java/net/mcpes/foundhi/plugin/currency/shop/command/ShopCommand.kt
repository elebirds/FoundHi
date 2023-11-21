package net.mcpes.foundhi.plugin.currency.shop.command

import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.command.ConsoleCommandSender
import cn.nukkit.item.Item
import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.command.FoundHiPluginCommandFunction
import net.mcpes.foundhi.plugin.currency.shop.SShopMain
import net.mcpes.foundhi.plugin.currency.shop.ShopDataManager
import net.mcpes.foundhi.plugin.currency.shop.data.ShopDatabase
import net.mcpes.foundhi.plugin.currency.shop.shop.ShopBase
import net.mcpes.foundhi.plugin.currency.shop.shop.`fun`.ItemShop

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/8/4
 * version 1.0
 */
class ShopCommand(name: String) : Command(name, "商店系统", "", arrayOf("商店")), FoundHiPluginCommandFunction {
    override fun execute(sender: CommandSender, s: String, strings: Array<out String>): Boolean {
        if (strings.isEmpty()) {
            sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
            return true
        }
        when (strings[0]) {
            "help" -> {
                if (sender is ConsoleCommandSender && (!this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.LOW_ADMIN))) {
                    sender.sendMessage("§c功能被禁用!")
                }
                sender.sendMessage("§6====§l§bS§au§dm§em§4i§6t §8商店·帮助§6====")
                sender.sendMessage("§a - /$s {add/添加/a} <类型> <物品(id:damage:singleCount) <money1> <money2> <money3> (物品名称) 添加好友")
                sender.sendMessage("§a - /$s {type/类型/t} 查看商店类型")
                sender.sendMessage("§a - /$s {help/帮助} 查看帮助")
                sender.sendMessage("§a - /$s {del/删除/d} <商店id> 删除商店")
            }
            "add", "添加", "a" -> {
                //shop add <type> <item> <m1> <m2> <m3> <itemName>
                if (sender is ConsoleCommandSender && (!this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.LOW_ADMIN))) {
                    sender.sendMessage("§c功能被禁用!")
                }
                if (strings.size !in 6..7) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                try {
                    val type = strings[1].toInt()
                    val m1 = strings[3].toDouble()
                    val m2 = strings[4].toDouble()
                    val m3 = strings[5].toDouble()
                    val itemS = strings[2].split(":")
                    if (itemS.size != 3) {
                        sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                        return true
                    }
                    val item = Item.get(itemS[0].toInt(), itemS[1].toInt(), itemS[2].toInt())!!
                    var shop = ShopBase.getShop(type, -1)!!
                    when (type) {
                        ShopBase.SYSTEM_BUY_SHOP, ShopBase.SYSTEM_SELL_SHOP -> {
                            shop = shop as ItemShop
                            shop.money1 = m1
                            shop.money2 = m2
                            shop.money3 = m3
                            shop.itemID = item.id
                            shop.itemDamage = item.damage
                            shop.singleCount = item.count
                            if (strings.size == 7) shop.itemName = strings[6]
                        }
                    }
                    shop.pos = ""
                    shop.id = ShopDatabase.addShop(shop)
                    ShopDataManager.addShop(shop.id, "", shop)
                    sender.sendMessage(SShopMain.TITLE + "添加商店(id:${shop.id})成功!")
                } catch (e: Exception) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
            }
            "open", "打开", "o" -> {
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                try {
                    val id = strings[1].toInt()
                    if (!ShopDataManager.existsShop(id)) {
                        sender.sendMessage("${SShopMain.TITLE}§c商店不存在!")
                        return true
                    }
                } catch (e: NumberFormatException) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                }
            }
            "del", "删除", "d" -> {
                if (sender is ConsoleCommandSender && (!this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.LOW_ADMIN))) {
                    sender.sendMessage("§c功能被禁用!")
                }
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                try {
                    val id = strings[1].toInt()
                    if (!ShopDataManager.existsShop(id)) {
                        sender.sendMessage("${SShopMain.TITLE}§c商店不存在!")
                        return true
                    }
                    val shop = ShopDataManager.getShop(id)!!
                    ShopDataManager.removeShop(shop)
                    ShopDatabase.delShop(shop.id)
                    sender.sendMessage(SShopMain.TITLE + "删除商店(id:${shop.id})成功!")
                } catch (e: NumberFormatException) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                }
            }
        }
        return true
    }
}