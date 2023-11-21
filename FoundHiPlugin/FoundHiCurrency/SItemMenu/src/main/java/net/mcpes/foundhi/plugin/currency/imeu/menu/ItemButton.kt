package net.mcpes.foundhi.plugin.currency.imeu.menu

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.command.ConsoleCommandSender
import cn.nukkit.item.Item
import cn.nukkit.nbt.tag.CompoundTag
import net.mcpes.foundhi.plugin.currency.imeu.SItemMenu
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/13
 * version 1.0
 */
class ItemButton(val id: String, val slot: Int, data: HashMap<String, Any>) {
    val item: Item
    var operation: ArrayList<String>
    var handle = HashMap<Int, String>()

    init {
        this.item = Item.get(Integer.valueOf(data["Item"].toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]),
                Integer.valueOf(data["Item"].toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]),
                Integer.valueOf(data["Count"].toString()))
        this.item.customName = data["Name"].toString()
        this.item.customBlockData = CompoundTag().putBoolean("menu", true)
        this.operation = data["Action"] as ArrayList<String>
        this.operationLoad()
    }

    fun operationLoad() {
        this.operation.forEach { a: String ->
            val b = a.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            when (b[0]) {
                "{open}" -> this.handle[0] = b[1]
                "{close}" -> this.handle[3] = ""
                "{scmd}" -> this.handle[1] = b[1]
                "{pcmd}" -> this.handle[2] = b[1]
            }
        }
    }

    fun handle(player: Player) {
        handle(player, handle)
    }

    companion object {

        fun handle(player: Player, handle: HashMap<Int, String>) {
            handle.forEach { type: Int, p: String ->
                when (type) {
                    0 -> {
                        //open
                        val m = SItemMenu.menus[p]
                        m?.open(player, true)
                    }
                    1 ->
                        //scmd
                        Server.getInstance().dispatchCommand(ConsoleCommandSender(), p.replace("%name", player.name))
                    2 ->
                        //pcmd
                        Server.getInstance().dispatchCommand(player, p.replace("%name", player.name))
                    3 -> ItemMenu.close(player)
                }
            }
        }
    }
}