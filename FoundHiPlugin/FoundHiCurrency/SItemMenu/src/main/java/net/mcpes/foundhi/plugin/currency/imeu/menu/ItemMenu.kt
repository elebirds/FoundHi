package net.mcpes.foundhi.plugin.currency.imeu.menu

import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.network.protocol.PlayerHotbarPacket
import net.mcpes.foundhi.plugin.currency.imeu.SItemMenu
import net.mcpes.foundhi.plugin.currency.imeu.SItemMenu.Companion.touch
import net.mcpes.foundhi.plugin.currency.imeu.listener.PlayerListener
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/13
 * version 1.0
 */
class ItemMenu(val id: String) {
    val buttons = HashMap<Int, ItemButton>()

    init {
        (SItemMenu.instance.menuConfig.get(id) as HashMap<*, *>).forEach { a, b ->
            val slot = Integer.valueOf(a.toString())
            buttons[slot] = ItemButton(this.id, Integer.valueOf(a.toString()), b as HashMap<String, Any>)
        }
    }

    @JvmOverloads
    fun open(player: Player, touch: Boolean, itemSave: Boolean = false) {
        if (touch) {
            SItemMenu.touch.remove(player.name)
        }
        if (itemSave) {
            SItemMenu.playerItems[player.name] = player.inventory.contents
        }
        SItemMenu.`in`[player.name] = id
        player.inventory.clearAll()
        player.inventory.heldItemIndex = 0
        player.inventory.setItem(0, Item.get(0))
        val slots = IntArray(9)
        var i = 1
        slots[0] = 26
        for (a in buttons.keys) {
            slots[i] = a
            player.inventory.setItem(a, buttons[a]!!.item)
            i++
        }
        setHotbar(player, 0, slots)
    }

    fun handle(player: Player, selected: Int): Boolean {
        if (selected == 0) return false
        if (!touch.containsKey(player.name) || touch.get(player.name) != selected) {
            touch[player.name] = selected
            this.open(player, false)
            return true
        }
        this.open(player, false)
        touch.remove(player.name)
        val button = buttons[selected]
        button?.handle(player)
        return true
    }

    companion object {
        fun setHotbar(player: Player, selected: Int, slots: IntArray) {
            val pk = PlayerHotbarPacket()
            pk.selectedHotbarSlot = selected
            pk.selectHotbarSlot = true
            player.dataPacket(pk)
            try {
                val field = player.inventory.javaClass.getDeclaredField("hotbar")
                field.isAccessible = true
                field.set(player.inventory, slots)
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }

        fun close(player: Player) {
            PlayerListener.removeMenuItem(player)
            player.inventory.clearAll()
            if (SItemMenu.playerItems.containsKey(player.name)) {
                player.inventory.contents = SItemMenu.playerItems[player.name]
            }
            SItemMenu.playerItems.remove(player.name)
            touch.remove(player.name)
            SItemMenu.`in`.remove(player.name)
        }
    }
}