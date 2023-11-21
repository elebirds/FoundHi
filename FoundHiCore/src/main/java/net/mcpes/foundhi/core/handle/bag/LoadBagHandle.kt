package net.mcpes.foundhi.core.handle.bag

import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.nbt.tag.CompoundTag
import net.mcpes.foundhi.core.handle.HandleClass

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/1
 * version 1.0
 */
class LoadBagHandle(val name: String, private val tag: CompoundTag) : HandleClass() {
    lateinit var player: Player
    override fun handle() {
        player.inventory.clearAll()
        tag.allTags.forEach {
            val key: Int = it.name.toInt()
            val tag: CompoundTag = it as CompoundTag
            when (key) {
                in 0..Player.SURVIVAL_SLOTS -> {
                    player.inventory.setItem(key, tagToItem(tag))
                }
                else -> {
                    val real: Int = key - Player.SURVIVAL_SLOTS
                    player.inventory.setArmorItem(real, tagToItem(tag))
                }
            }
        }
    }

    companion object {
        fun tagToItem(tag: CompoundTag): Item {
            return Item.get(tag.getShort("a"), tag.getShort("b"), tag.getByte("c")).setCompoundTag(tag.getCompound("d"))
        }
    }
}