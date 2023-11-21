package net.mcpes.foundhi.core.app.command.defaults

import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.command.Command
import net.mcpes.foundhi.core.app.command.CommandSender
import net.mcpes.foundhi.core.app.command.PacketCommandSender
import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.handle.HandleResults
import net.mcpes.foundhi.core.utils.getMoneyName
import net.mcpes.foundhi.core.utils.getPlayerPermission

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/26
 * version 1.0
 */

class AdminCommand(name: String) : Command(name, "FoundHi管理命令") {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.isEmpty()) {
            sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
            return false
        }
        when (strings[0]) {
            "permission" -> if (sender is PacketCommandSender) {
                if (strings.size != 3) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                    sender.result = HandleResults.THE_OTHER_OFFLINE
                    return true
                }
                val player: Player = APIFunction.instance.getPlayer(strings[1])!!
                try {
                    player.permission = strings[2].toInt().getPlayerPermission()
                } catch (e: NumberFormatException) {
                    sender.result = HandleResults.PARAMETER_ERROR
                }
            } else {
                if (strings.size != 3) {
                    sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                    return true
                }
                if (!APIFunction.instance.isPlayerOnline(strings[1])) {
                    sender.sendMessage("§c玩家不在线。")
                    return true
                }
                val player: Player = APIFunction.instance.getPlayer(strings[1])!!
                try {
                    player.permission = strings[2].toInt().getPlayerPermission()
                    sender.sendMessage("§a成功设置玩家 " + strings[1] + "的全局权限为 " + player.permission.permissionName)
                } catch (e: NumberFormatException) {
                    sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                }

            }
            "money" -> if (sender is PacketCommandSender) {
                if (strings.size == 1) {
                    sender.result = HandleResults.PARAMETER_ERROR
                    return true
                }
                when (strings[1]) {
                    "add" -> {
                        //admin money add <name> <type> <count>
                        if (strings.size != 5) {
                            sender.result = HandleResults.PARAMETER_ERROR
                            return true
                        }
                        if (!APIFunction.instance.isPlayerOnline(strings[2])) {
                            sender.result = HandleResults.THE_OTHER_OFFLINE
                            return true
                        }
                        try {
                            val player = APIFunction.instance.getPlayer(strings[2])!!
                            when (Integer.valueOf(strings[3])) {
                                1 -> player.addMoney1(strings[4].toDouble())
                                2 -> player.addMoney2(strings[4].toDouble())
                                3 -> player.addMoney3(strings[4].toDouble())
                                else -> sender.result = HandleResults.PARAMETER_ERROR
                            }
                        } catch (e: NumberFormatException) {
                            sender.result = HandleResults.PARAMETER_ERROR
                        }
                    }
                    "del" -> {
                        if (strings.size != 5) {
                            sender.result = HandleResults.PARAMETER_ERROR
                            return true
                        }
                        if (!APIFunction.instance.isPlayerOnline(strings[2])) {
                            sender.result = HandleResults.THE_OTHER_OFFLINE
                            return true
                        }
                        try {
                            val player = APIFunction.instance.getPlayer(strings[2])!!
                            when (Integer.valueOf(strings[3])) {
                                1 -> sender.result = if (player.delMoney1(strings[4].toDouble())) {
                                    HandleResults.NORMAL_SUCCESS
                                } else {
                                    HandleResults.NOT_ENOUGH_MONEY
                                }
                                2 -> sender.result = if (player.delMoney2(strings[4].toDouble())) {
                                    HandleResults.NORMAL_SUCCESS
                                } else {
                                    HandleResults.NOT_ENOUGH_MONEY
                                }
                                3 -> sender.result = if (player.delMoney3(strings[4].toDouble())) {
                                    HandleResults.NORMAL_SUCCESS
                                } else {
                                    HandleResults.NOT_ENOUGH_MONEY
                                }
                                else -> sender.result = HandleResults.PARAMETER_ERROR
                            }
                        } catch (e: NumberFormatException) {
                            sender.result = HandleResults.PARAMETER_ERROR
                        }
                    }
                    "set" -> {
                        if (strings.size != 5) {
                            sender.result = HandleResults.PARAMETER_ERROR
                            return true
                        }
                        if (!APIFunction.instance.isPlayerOnline(strings[2])) {
                            sender.result = HandleResults.THE_OTHER_OFFLINE
                            return true
                        }
                        try {
                            val player = APIFunction.instance.getPlayer(strings[2])!!
                            when (Integer.valueOf(strings[3])) {
                                1 -> player.money1 = strings[4].toDouble()
                                2 -> player.money2 = strings[4].toDouble()
                                3 -> player.money3 = strings[4].toDouble()
                                else -> sender.result = HandleResults.PARAMETER_ERROR
                            }
                        } catch (e: NumberFormatException) {
                            sender.result = HandleResults.PARAMETER_ERROR
                        }

                    }
                    "pay" -> {
                        if (strings.size != 6) {
                            sender.result = HandleResults.PARAMETER_ERROR
                            return true
                        }
                        if (!APIFunction.instance.isPlayerOnline(strings[2]) || !APIFunction.instance.isPlayerOnline(strings[3])) {
                            sender.result = HandleResults.THE_OTHER_OFFLINE
                            return true
                        }
                        try {
                            val player = APIFunction.instance.getPlayer(strings[2])!!
                            val player2 = APIFunction.instance.getPlayer(strings[3])!!
                            when (strings[4].toInt()) {
                                1 -> sender.result = if (player.delMoney1(strings[4].toDouble())) {
                                    player2.addMoney1(strings[5].toDouble())
                                    HandleResults.NORMAL_SUCCESS
                                } else {
                                    HandleResults.NOT_ENOUGH_MONEY
                                }
                                2 -> sender.result = if (player.delMoney2(strings[4].toDouble())) {
                                    player2.addMoney2(strings[5].toDouble())
                                    HandleResults.NORMAL_SUCCESS
                                } else {
                                    HandleResults.NOT_ENOUGH_MONEY
                                }
                                3 -> sender.result = if (player.delMoney3(strings[4].toDouble())) {
                                    player2.addMoney3(strings[5].toDouble())
                                    HandleResults.NORMAL_SUCCESS
                                } else {
                                    HandleResults.NOT_ENOUGH_MONEY
                                }
                                else -> sender.result = HandleResults.PARAMETER_ERROR
                            }
                        } catch (e: NumberFormatException) {
                            sender.result = HandleResults.PARAMETER_ERROR
                        }

                    }
                }
            } else {
                if (strings.size == 1) {
                    sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                    return true
                }
                when (strings[1]) {
                    "add" -> {
                        //admin money add <name> <type> <count>
                        if (strings.size != 5) {
                            sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                            return true
                        }
                        if (!APIFunction.instance.isPlayerOnline(strings[2])) {
                            sender.sendMessage("§c玩家不在线。")
                            return true
                        }
                        try {
                            val type: Int = Integer.valueOf(strings[3])
                            val player = APIFunction.instance.getPlayer(strings[2])!!
                            when (type) {
                                1 -> player.addMoney1(strings[4].toDouble())
                                2 -> player.addMoney1(strings[4].toDouble())
                                3 -> player.addMoney1(strings[4].toDouble())
                                else -> {
                                    sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                                    return true
                                }
                            }
                            sender.sendMessage("§a成功增加了玩家 " + strings[1] + "的" + type.getMoneyName() + strings[4] + "枚")
                        } catch (e: NumberFormatException) {
                            sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                        }

                    }
                    "del" -> {
                        if (strings.size != 5) {
                            sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                            return true
                        }
                        if (!APIFunction.instance.isPlayerOnline(strings[2])) {
                            sender.sendMessage("§c玩家不在线。")
                            return true
                        }
                        try {
                            val type: Int = Integer.valueOf(strings[3])
                            val player = APIFunction.instance.getPlayer(strings[2])!!
                            when (type) {
                                1 -> player.delMoney1(strings[4].toDouble())
                                2 -> player.delMoney1(strings[4].toDouble())
                                3 -> player.delMoney1(strings[4].toDouble())
                                else -> sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                            }
                            sender.sendMessage("§a成功减少了玩家 " + strings[1] + "的" + type.getMoneyName() + strings[4] + "枚")
                        } catch (e: NumberFormatException) {
                            sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                        }

                    }
                    "set" -> {
                        if (strings.size != 5) {
                            sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                            return true
                        }
                        if (!APIFunction.instance.isPlayerOnline(strings[2])) {
                            sender.sendMessage("§c玩家不在线。")
                            return true
                        }
                        try {
                            val type: Int = Integer.valueOf(strings[3])
                            val player = APIFunction.instance.getPlayer(strings[2])!!
                            when (type) {
                                1 -> player.money1 = strings[4].toDouble()
                                2 -> player.money1 = strings[4].toDouble()
                                3 -> player.money1 = strings[4].toDouble()
                                else -> sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                            }
                            sender.sendMessage("§a成功设置玩家 " + strings[1] + "的" + type.getMoneyName() + "为" + strings[4] + "枚")
                        } catch (e: NumberFormatException) {
                            sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
                        }
                    }
                }
            }
            else -> if (sender is PacketCommandSender) {
                sender.result = HandleResults.PARAMETER_ERROR
            } else {
                sender.sendMessage("§c错误的指令用法。请使用 /help 来显示指令帮助。")
            }
        }
        return true
    }
}