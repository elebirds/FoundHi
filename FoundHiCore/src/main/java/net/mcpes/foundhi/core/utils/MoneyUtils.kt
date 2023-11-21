package net.mcpes.foundhi.core.utils

import cn.nukkit.Server
import net.mcpes.foundhi.core.handle.FastHandle
import net.mcpes.foundhi.core.handle.Handle
import net.mcpes.foundhi.core.handle.HandleResults
import net.mcpes.foundhi.core.handle.HandleSystem
import net.mcpes.foundhi.core.handle.money.MoneyAddHandle
import net.mcpes.foundhi.core.handle.money.MoneyDeleteHandle
import net.mcpes.foundhi.core.network.protocol.command.CommandPacket
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.sf.json.JSONArray
import java.util.function.Consumer


fun Int.getMoneyName() = when (this) {
    1 -> "金币"
    2 -> "钻石"
    3 -> "点券"
    else -> ""
}

fun String.getMoneyNameByType() = when (this) {
    "m1" -> "金币"
    "m2" -> "钻石"
    "m3" -> "点券"
    else -> ""
}

object MoneyUtils {
    @JvmOverloads
    @JvmStatic
    fun pay(cause: String, target: String, count: Double, type: Int, handle: Handle? = null): Int {
        if (type <= 0 || type >= 4) return HandleResults.MONEY_TYPE_ERROR
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + FoundHiPlugin.instance.id
        cp.event = true
        cp.sid = FoundHiPlugin.instance.id
        cp.cmd = "admin money pay $cause $target $type $count"
        FoundHiPlugin.instance.addHandle(evid, handle ?: FastHandle(Consumer {
            val causeP = Server.getInstance().getPlayerExact(cause)
            val targetP = Server.getInstance().getPlayerExact(target)
            if (causeP != null && causeP.isOnline) {
                when (it) {
                    HandleResults.NORMAL_SUCCESS -> {
                        causeP.sendMessage("§a§l支付" + count.moneyFormat() + "枚" + type.getMoneyName() + "给" + target + "成功!")
                        if (targetP != null && targetP.isOnline) {
                            targetP.sendMessage("§l§8┃§bS§au§dm§em§4i§6t§8┃§8 - NOTICE: " + cause + "§a§l支付给你" + count.moneyFormat() + "枚" + type.getMoneyName())
                        }
                    }
                    HandleResults.THE_OTHER_OFFLINE -> causeP.sendMessage("§a§l操作失败:玩家:" + target + "不在线!")
                    HandleResults.PARAMETER_ERROR -> causeP.sendMessage("§a§l操作失败:未知参数错误!")
                    HandleResults.NOT_ENOUGH_MONEY -> causeP.sendMessage("§a§l操作失败:所需货币不足!")
                }
            }
        }))
        FoundHiPlugin.instance.dataPacket(cp)
        return HandleResults.NORMAL_SUCCESS
    }

    @JvmOverloads
    @JvmStatic
    fun add(target: String, count: Double, type: Int, handle: Handle = MoneyAddHandle("系统", target, count, type)): Int {
        return give("系统", target, count, type, true, handle)
    }

    @JvmStatic
    fun add(target: String, count: ArrayList<Double>, type: ArrayList<Int>, handle: Handle): Int {
        return add(target, JSONArray.fromObject(count).toString(), JSONArray.fromObject(type).toString(), handle)
    }

    @JvmStatic
    fun add(target: String, count: String, type: String, handle: Handle): Int {
        return give("系统", target, count, type, true, handle)
    }

    @JvmOverloads
    @JvmStatic
    fun give(cause: String, target: String, count: String, type: String, send: Boolean = true, handle: Handle): Int {
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + FoundHiPlugin.instance.id
        cp.event = true
        cp.sid = FoundHiPlugin.instance.id
        cp.cmd = "admin money add $target $type $count"
        if (send) FoundHiPlugin.instance.addHandle(evid, handle)
        FoundHiPlugin.instance.dataPacket(cp)
        return HandleResults.NORMAL_SUCCESS
    }

    @JvmOverloads
    @JvmStatic
    fun give(cause: String, target: String, count: Double, type: Int, send: Boolean = true, handle: Handle = MoneyAddHandle(cause, target, count, type)): Int {
        if (type <= 0 || type >= 4) return HandleResults.MONEY_TYPE_ERROR
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + FoundHiPlugin.instance.id
        cp.event = true
        cp.sid = FoundHiPlugin.instance.id
        cp.cmd = "admin money add $target $type $count"
        if (send) FoundHiPlugin.instance.addHandle(evid, handle)
        FoundHiPlugin.instance.dataPacket(cp)
        return HandleResults.NORMAL_SUCCESS
    }

    @JvmOverloads
    @JvmStatic
    fun delete(cause: String, target: String, count: String, type: String, handle: Handle? = null) {
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + FoundHiPlugin.instance.id
        cp.event = true
        cp.sid = FoundHiPlugin.instance.id
        cp.cmd = "admin money del $target $type $count"
        if (handle != null) FoundHiPlugin.instance.addHandle(evid, handle)
        FoundHiPlugin.instance.dataPacket(cp)
    }

    @JvmOverloads
    @JvmStatic
    fun delete(cause: String, target: String, count: Double, type: Int, send: Boolean = true, handle: Handle = MoneyDeleteHandle(cause, target, count, type)): Int {
        if (type <= 0 || type >= 4) return HandleResults.MONEY_TYPE_ERROR
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + FoundHiPlugin.instance.id
        cp.event = true
        cp.sid = FoundHiPlugin.instance.id
        cp.cmd = "admin money del $target $type $count"
        if (send) FoundHiPlugin.instance.addHandle(evid, handle)
        FoundHiPlugin.instance.dataPacket(cp)
        return HandleResults.NORMAL_SUCCESS
    }
}
