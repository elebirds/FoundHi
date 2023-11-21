package net.mcpes.foundhi.plugin.survival.land.command

import cn.nukkit.Server
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.command.ConsoleCommandSender
import cn.nukkit.level.Location
import cn.nukkit.scheduler.AsyncTask
import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.FoundHiPlugin.Companion.SERVER_TITLE
import net.mcpes.foundhi.core.plugin.command.FoundHiPluginCommandFunction
import net.mcpes.foundhi.plugin.survival.land.SLand
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.landWorldCorresponding
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.landWorlds
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.lands
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.phLands
import net.mcpes.foundhi.plugin.survival.land.land.LandWorld
import net.mcpes.foundhi.plugin.survival.land.utils.LandUtils

@Suppress("JAVA_CLASS_ON_COMPANION")
/**
 * SLand
 *
 * @author hhm Copyright (c) 2018/5/4
 * version 1.0
 */
class LandCommand(name: String) : Command(name), FoundHiPluginCommandFunction {

    override fun execute(sender: CommandSender, s: String, strings: Array<out String>): Boolean {
        if (strings.isEmpty()) {
            sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
            return true
        }
        when (strings[0]) {
            "my", "m", "我" -> {
                if (sender !is cn.nukkit.Player) {
                    sender.sendMessage("§c操作失败:只有玩家才能使用此命令。")
                    return false
                }
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (!this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.PLAYER)) {
                    sender.sendMessage("§c功能被禁用!")
                    return true
                }
                if (!phLands.containsKey(sender.name)) {
                    sender.sendMessage("§c操作错误:你没有领地。")
                    return true
                }
                sender.sendMessage(">===$SERVER_TITLE ${SLand.TITLE} ${sender.name} 玩家领地列表===<")
                var i = 0
                phLands[sender.name]!!.forEach {
                    sender.sendMessage(" §a$it")
                    i++
                }
                sender.sendMessage("§6结束,共 $i 个")
            }
            "list", "l", "列表" -> {
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (sender !is ConsoleCommandSender && !this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.LOW_ADMIN)) {
                    sender.sendMessage("§c功能被禁用!")
                    return true
                }
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                if (!phLands.containsKey(strings[1])) {
                    sender.sendMessage("§c操作错误:此玩家没有领地。")
                    return true
                }
                sender.sendMessage(">===$SERVER_TITLE ${SLand.TITLE} ${strings[1]} 玩家领地列表===<")
                var i = 0
                phLands[strings[1]]!!.forEach {
                    sender.sendMessage(" §a$it")
                    i++
                }
                sender.sendMessage("§6结束,共 $i 个")
            }
            "delete", "d", "删除" -> {
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (sender !is ConsoleCommandSender && !this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.LOW_ADMIN)) {
                    sender.sendMessage("§c功能被禁用!")
                    return true
                }
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                if (!lands.containsKey(strings[1])) {
                    sender.sendMessage("§c操作错误:没有此领地。")
                    return true
                }
                val a = strings[1].split("-")
                Server.getInstance().scheduler.scheduleAsyncTask(SLand.instance, object : AsyncTask() {
                    override fun onRun() {
                        val id = "${a[0]}-${a[1]}"
                        LandUtils.cleanLand(id, a[2])
                        val lw = landWorlds[landWorldCorresponding[a[2]]]!!
                        LandUtils.delete(lw.lands[id]!!, lw)
                        sender.sendMessage("§6操作成功!")
                    }
                })
            }
            "clean", "cl", "清空建筑" -> {
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (sender !is ConsoleCommandSender && !this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.LOW_ADMIN)) {
                    sender.sendMessage("§c功能被禁用!")
                    return true
                }
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                if (!lands.containsKey(strings[1])) {
                    sender.sendMessage("§c操作错误:没有此领地。")
                    return true
                }
                val a = strings[1].split("-")
                Server.getInstance().scheduler.scheduleAsyncTask(SLand.instance, object : AsyncTask() {
                    override fun onRun() {
                        LandUtils.cleanLand("${a[0]}-${a[1]}", a[2])
                        sender.sendMessage("§6操作成功!")
                    }
                })
            }
            "found", "f", "寻找" -> {
                if (sender !is cn.nukkit.Player) {
                    sender.sendMessage("§c操作失败:只有玩家才能使用此命令。")
                    return false
                }
                if (!landWorldCorresponding.containsKey(sender.level.name)) {
                    sender.sendMessage("§c操作失败:此世界不是一个领地世界。")
                    return false
                }
                Server.getInstance().scheduler.scheduleAsyncTask(SLand.instance, object : AsyncTask() {
                    override fun onRun() {
                        sender.teleport(LandUtils.getEmptyPos(sender, landWorlds[landWorldCorresponding[sender.level.name]!!]!!.type))
                        sender.sendMessage("§6操作成功!")
                    }
                })
            }
            "give", "g", "给予" -> {
                if (sender !is cn.nukkit.Player) {
                    sender.sendMessage("§c操作失败:只有玩家才能使用此命令。")
                    return false
                }
                if (!this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.PLAYER)) {
                    sender.sendMessage("§c功能被禁用!")
                    return true
                }
                if (strings.size != 3) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                if (!lands.containsKey(strings[1]) || lands[strings[1]]!!.owner != sender.name) {
                    sender.sendMessage("§c操作失败:此领地不是你的。")
                    return true
                }
                val land = lands[strings[1]]!!
                land.owner = strings[2]
                land.guests.remove(land.owner)
                land.guests.remove(sender.name)
                LandUtils.delPhLand(sender.name, land)
                LandUtils.addPhLand(land.owner, land)
                sender.sendMessage("§6操作成功!")
            }
            "tp", "t", "传送" -> {
                if (sender !is cn.nukkit.Player) {
                    sender.sendMessage("§c操作失败:只有玩家才能使用此命令。")
                    return false
                }
                if (!this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.PLAYER)) {
                    sender.sendMessage("§c功能被禁用!")
                    return true
                }
                if (strings.size != 2) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                if (!lands.containsKey(strings[1])) {
                    sender.sendMessage("§c操作失败:不存在此领地。")
                    return true
                }
                val land = lands[strings[1]]!!
                sender.teleport(Location(land.x1 + 2.0, 10.toDouble(), land.z1 + 2.0))
                sender.sendMessage("§6操作成功!")
            }
            "guest", "u", "访客" -> {
                if (sender !is cn.nukkit.Player) {
                    sender.sendMessage("§c操作失败:只有玩家才能使用此命令。")
                    return false
                }
                if (!this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.PLAYER)) {
                    sender.sendMessage("§c功能被禁用!")
                    return true
                }
                if (strings.size != 4) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                if (!lands.containsKey(strings[2]) || lands[strings[2]]!!.owner != sender.name) {
                    sender.sendMessage("§c操作失败:此领地不是你的。")
                    return true
                }
                val land = lands[strings[2]]!!
                when (strings[1]) {
                    "add" -> {
                        if (land.guests.contains(strings[3])) {
                            sender.sendMessage("§c操作失败:对方已经是你领地的访客。")
                            return true
                        }
                        land.guests.add(strings[3])
                        sender.sendMessage("§6操作成功!")
                    }
                    "del" -> {
                        if (!land.guests.contains(strings[3])) {
                            sender.sendMessage("§c操作失败:对方还不是你领地的访客。")
                            return true
                        }
                        land.guests.remove(strings[3])
                        sender.sendMessage("§6操作成功!")
                    }
                    else -> {
                        sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                        return true
                    }
                }
            }
            "create", "c", "创建" -> {
                //land create level name type money mt
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (sender !is ConsoleCommandSender && !this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.HIGH_ADMIN)) {
                    sender.sendMessage("§c功能被禁用!")
                    return true
                }
                if (strings.size != 6) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                    return true
                }
                if (landWorldCorresponding.containsKey(strings[1]) || Server.getInstance().isLevelLoaded(strings[1])) {
                    sender.sendMessage("§c操作错误:此世界已存在。")
                    return true
                }
                try {
                    val name = strings[2]
                    val type = strings[3].toInt()
                    val money = strings[4].toDouble()
                    val mt = strings[5].toInt()
                    val landWorld = LandWorld(strings[1], name, type, money, mt)
                    val id = landWorld.id
                    LandUtils.createLandWorld(landWorld.level, type)
                    landWorlds[id] = landWorld
                    landWorldCorresponding[landWorld.level] = id
                    sender.sendMessage("§a操作成功:成功创建${type}型领地世界,ID:$id,名称:$name。")
                } catch (e: NullPointerException) {
                    sender.sendMessage("§c错误的指令用法。请使用 /$s help 来显示指令列表。")
                }
            }
            "help", "帮助", "h" -> {
                if (this.checkPull(sender)) {
                    sender.sendMessage("§c数据拉取失败!")
                    return true
                }
                if (sender !is ConsoleCommandSender && !this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.PLAYER)) {
                    sender.sendMessage("§c功能被禁用!")
                    return true
                }
                sender.sendMessage(">===$SERVER_TITLE ${SLand.TITLE} 插件帮助===<")
                if (sender !is ConsoleCommandSender || this.checkPermission(FoundHiPlugin.instance.getPlayer(sender.name), Player.Permission.HIGH_ADMIN)) {
                    sender.sendMessage(" §a/$s {create/c/创建} <世界> <领地世界名字> <类型:1/2> <花费货币数量> <货币类型:1/2/3>  :  创建领地世界  : HIGH_ADMIN")
                    sender.sendMessage(" §8/$s {list/l/列表} <目标玩家>  :  查看玩家领地列表  : LOW_ADMIN")
                    sender.sendMessage(" §7/$s {delete/d/删除} <领地id:(x-z-世界名称)> <是否清空建筑:true/false>  :  删除领地  : LOW_ADMIN")
                    sender.sendMessage(" §7/$s {clean/cl/清空建筑} <领地id:(x-z-世界名称)>  :  清空领地建筑  : LOW_ADMIN")
                }
                sender.sendMessage(" §b/$s {found/f/寻找}  :  寻找空地皮")
                sender.sendMessage(" §c/$s {give/g/给予} <领地id:(x-z-世界名称)> <目标玩家>  :  给予领地")
                sender.sendMessage(" §d/$s {my/m/我}  :  查看我的地皮详情")
                sender.sendMessage(" §e/$s {help/h/帮助}  :  插件帮助")
                sender.sendMessage(" §1/$s {tp/t/传送} <领地id:(x-z-世界名称)>  :  传送到目标领地")
                sender.sendMessage(" §2/$s {guest/u/访客} <操作:add/del> <领地id:(x-z-世界名称)> <目标玩家>  :  添加/删除访客")
                sender.sendMessage(" §1/$s {info/i/信息} <领地id:(x-z-世界名称)>  :  查看自己的领地信息")
            }
        }
        return true
    }
}