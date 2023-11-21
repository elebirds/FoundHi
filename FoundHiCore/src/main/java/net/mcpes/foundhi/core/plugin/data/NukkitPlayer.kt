package net.mcpes.foundhi.core.plugin.data

import cn.nukkit.Player
import cn.nukkit.form.window.FormWindow
import cn.nukkit.item.Item
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.SourceInterface
import cn.nukkit.network.protocol.ModalFormRequestPacket
import net.mcpes.foundhi.core.network.protocol.player.SendBagPacket
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.gui.NukkitGraphicalUserInterface
import net.mcpes.foundhi.core.plugin.gui.function.Variable
import java.io.IOException


/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/1
 * version 1.0
 */
class NukkitPlayer(interfaz: SourceInterface, clientID: Long?, ip: String, port: Int) : Player(interfaz, clientID, ip, port) {
    val guiParams: HashMap<String, Array<out Any>> = hashMapOf()

    fun getFormWindowCount() = this.formWindowCount

    fun getBagTag(): CompoundTag {
        if (this.inventory == null) return CompoundTag()
        val all = CompoundTag()
        for (slot in 0 until Player.SURVIVAL_SLOTS) {
            val item = this.inventory.getItem(slot)
            all.put(slot.toString(), this.getItemTag(item).setName(slot.toString()))
        }
        for (slot in 0..3) {
            val item = this.inventory.getItem(this.inventory.size + slot)
            all.put((this.inventory.size + slot).toString(), this.getItemTag(item).setName((this.inventory.size + slot).toString()))
        }
        return all
    }

    private fun getItemTag(item: Item): CompoundTag {
        return CompoundTag().putShort("a", item.id).putShort("b", item.damage).putByte("c", item.count).putCompound("tag", item.namedTag
                ?: CompoundTag())
    }

    fun sendBagTag() {
        if (FoundHiPlugin.instance.isUseCBag) {
            try {
                val bag = this.getBagTag()
                if (bag.isEmpty) return
                val pk2 = SendBagPacket()
                pk2.name = this.name
                pk2.data = NBTIO.write(bag)
                pk2.id = FoundHiPlugin.instance.cBagID
                FoundHiPlugin.instance.dataPacket(pk2)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun showGUI(gui: Variable, vararg param: Any) {
        if (gui is NukkitGraphicalUserInterface) this.guiParams[gui.id] = param
        gui.open(this, param)
    }

    fun showGUI(data: String, gui: NukkitGraphicalUserInterface): Int {
        val packet = ModalFormRequestPacket()
        packet.formId = this.formWindowCount++
        packet.data = data
        this.dataPacket(packet)
        gui.openIds[this.name] = packet.formId
        NukkitGraphicalUserInterface.playerInGUI[this.name] = gui.id
        return packet.formId
    }

    fun showGUI(gui: NukkitGraphicalUserInterface): Int = showGUI(gui.data, gui)

    @Deprecated("请使用方法:showGUI", ReplaceWith("-1"))
    override fun showFormWindow(window: FormWindow?): Int {
        return -1
    }
}