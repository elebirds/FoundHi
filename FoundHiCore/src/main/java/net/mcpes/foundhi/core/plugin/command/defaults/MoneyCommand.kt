package net.mcpes.foundhi.core.plugin.command.defaults

import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.command.FoundHiPluginCommandFunction
import net.mcpes.foundhi.core.utils.MoneyUtils
import net.mcpes.foundhi.core.utils.moneyFormat

class MoneyCommand(name: String) : Command(name), FoundHiPluginCommandFunction {

    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.isEmpty()) {
            sender.sendMessage("§c错误的指令用法。请使用 /money help 来显示指令列表。")
            return true
        }
        when (strings[0]) {
            "help" -> {
                var permission = -1
                sender.sendMessage("§6====§l§bS§au§dm§em§4i§6t §8经济·帮助§6====")
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (sender.isPlayer) {
                    permission = FoundHiPlugin.instance.getPlayer(sender.name)!!.permission.level
                }
                if (!sender.isPlayer || permission >= Player.Permission.OWNER.level) {
                    //owner
                }
                if (!sender.isPlayer || permission >= Player.Permission.HIGH_ADMIN.level) {
                    //high
                }
                if (!sender.isPlayer || permission >= Player.Permission.LOW_ADMIN.level) {
                    //low
                    sender.sendMessage("§8 - /money give <目标玩家名称(在线)> <货币类型> <数量> 给予别人一定数量的货币")
                }
                if (!sender.isPlayer || permission >= Player.Permission.PLAYER.level) {
                    //player
                    sender.sendMessage("§a - /money my 查看我的经济信息")
                    sender.sendMessage("§b - /money pay <目标玩家名称(在线)> <货币类型> <数量> 支付给别人一定数量的货币")
                    sender.sendMessage("§c - /money type 查看货币类型")
                    sender.sendMessage("§e - /money top <货币类型> 查看排行榜")
                    sender.sendMessage("§d - /money help 查看帮助")
                } else {
                    sender.sendMessage("§c功能被禁用!")
                }
            }
            "pay" -> {
                if (!sender.isPlayer) {
                    sender.sendMessage("§c控制台禁用此命令!")
                    return true
                }
                if (strings.size != 4) {
                    sender.sendMessage("§c错误的指令用法。请使用 /money help 来显示指令列表。")
                    return true
                }
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.Ban)) {
                    if (strings[1] == sender.name) {
                        sender.sendMessage("§c禁止和自己进行交易!")
                        return true
                    }
                    var has = 0.00
                    when (Integer.parseInt(strings[2])) {
                        1 -> has = FoundHiPlugin.instance.getPlayer(sender.name)!!.money1
                        2 -> has = FoundHiPlugin.instance.getPlayer(sender.name)!!.money2
                        3 -> has = FoundHiPlugin.instance.getPlayer(sender.name)!!.money3
                    }
                    if (java.lang.Double.parseDouble(strings[3]) >= has) {
                        sender.sendMessage("§c所交易的数量大于自己所拥有的最大值!")
                        return true
                    }
                    try {
                        when (MoneyUtils.pay(sender.name, strings[1], java.lang.Double.parseDouble(strings[3]), Integer.parseInt(strings[2]))) {
                            0 -> sender.sendMessage("§3正在处理中...")
                            -1 -> sender.sendMessage("§c类型输入错误!请使用 /money help 来查看指令帮助。")
                            -2 -> sender.sendMessage("§c数据拉取失败!")
                        }
                    } catch (e: NumberFormatException) {
                        sender.sendMessage("§c错误的指令用法。请使用 /money help 来显示指令列表。")
                    }

                } else {
                    sender.sendMessage("§c功能被禁用!")
                }
            }
            "my" -> {
                if (!sender.isPlayer) {
                    sender.sendMessage("§c控制台禁用此命令!")
                    return true
                }
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.Ban)) {
                    sender.sendMessage("§l§6===== " + sender.name + "の个人信息 §6=====\n" +
                            "§a❤金币数量:" + FoundHiPlugin.instance.getPlayer(sender.name)!!.money1.moneyFormat() + "\n" +
                            "§b❤钻石数量:" + FoundHiPlugin.instance.getPlayer(sender.name)!!.money2.moneyFormat() + "\n" +
                            "§d❤点券数量:" + FoundHiPlugin.instance.getPlayer(sender.name)!!.money3.moneyFormat())
                } else {
                    sender.sendMessage("§c功能被禁用!")
                }
            }
            "top" -> {
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /money help 来显示指令列表。")
                    return true
                }
                try {
                    var i = 0
                    when (Integer.parseInt(strings[1])) {
                        1 -> {
                            sender.sendMessage("§l§6===== §l§bS§au§dm§em§4i§6t の 金币排行榜 §6=====")
                            for (name in FoundHiPlugin.instance.money1Top.keys) {
                                i++
                                if (i > 10) return true
                                sender.sendMessage("§3■TOP" + i + "■ §c金币数量:§6" + FoundHiPlugin.instance.money1Top[name]!!.moneyFormat() + " §a玩家: §6" + name)
                            }
                        }
                        2 -> {
                            sender.sendMessage("§l§6===== §l§bS§au§dm§em§4i§6t の 钻石排行榜 §6=====")
                            for (name in FoundHiPlugin.instance.money2Top.keys) {
                                i++
                                if (i > 10) return true
                                sender.sendMessage("§3■TOP" + i + "■ §c钻石数量:§6" + FoundHiPlugin.instance.money2Top[name]!!.moneyFormat() + " §a玩家: §6" + name)
                            }
                        }
                        3 -> {
                            sender.sendMessage("§l§6===== §l§bS§au§dm§em§4i§6t の 点券排行榜 §6=====")
                            for (name in FoundHiPlugin.instance.money3Top.keys) {
                                i++
                                if (i > 10) return true
                                sender.sendMessage("§3■TOP" + i + "■ §c点券数量:§6" + FoundHiPlugin.instance.money3Top[name]!!.moneyFormat() + " §a玩家: §6" + name)
                            }
                        }
                    }
                } catch (e: NumberFormatException) {
                    sender.sendMessage("§c错误的指令用法。请使用 /money help 来显示指令列表。")
                }

            }
            "give" -> {
                if (strings.size != 4) {
                    sender.sendMessage("§c错误的指令用法。请使用 /money help 来显示指令列表。")
                    return true
                }
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (!sender.isPlayer || this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.LOW_ADMIN)) {
                    try {
                        when (MoneyUtils.give(sender.name, strings[1], java.lang.Double.parseDouble(strings[3]), Integer.parseInt(strings[2]))) {
                            0 -> sender.sendMessage("§3正在处理中...")
                            -1 -> sender.sendMessage("§c类型输入错误!请使用 /money help 来查看指令帮助。")
                            -2 -> sender.sendMessage("§c数据拉取失败!")
                        }
                    } catch (e: NumberFormatException) {
                        sender.sendMessage("§c错误的指令用法。请使用 /money help 来显示指令列表。")
                    }

                } else {
                    sender.sendMessage("§c功能被禁用!")
                }
            }
        }
        return true
    }
}
