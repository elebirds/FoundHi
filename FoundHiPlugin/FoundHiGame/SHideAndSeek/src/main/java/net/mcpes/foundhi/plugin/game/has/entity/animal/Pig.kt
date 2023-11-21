package net.mcpes.foundhi.plugin.game.has.entity.animal

import cn.nukkit.entity.Entity
import cn.nukkit.item.Item
import cn.nukkit.level.format.FullChunk
import cn.nukkit.nbt.tag.CompoundTag
import net.mcpes.foundhi.plugin.game.has.entity.WalkingAnimal

class Pig(chunk: FullChunk, nbt: CompoundTag) : WalkingAnimal(chunk, nbt) {
    override val killExperience: Int
        get() = 0 // gain 1-3 experience

    override fun getNetworkId(): Int {
        return NETWORK_ID
    }

    override fun getName(): String {
        return "Pig"
    }

    override fun getWidth(): Float {
        return if (this.isBaby) {
            0.45f
        } else 0.9f
    }

    override fun getHeight(): Float {
        return if (this.isBaby) {
            0.45f
        } else 0.9f
    }

    override fun getEyeHeight(): Float {
        return if (this.isBaby) {
            0.45f
        } else 0.9f
    }

    override fun isBaby(): Boolean {
        return this.getDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_BABY)
    }

    public override fun initEntity() {
        super.initEntity()
        this.maxHealth = 10
    }

    override fun getDrops(): Array<Item> {
        return arrayOf()
    }

    companion object {
        const val NETWORK_ID = 12
    }
}