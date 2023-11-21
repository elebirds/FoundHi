package net.mcpes.foundhi.plugin.game.has.entity.animal

import cn.nukkit.entity.Entity
import cn.nukkit.item.Item
import cn.nukkit.level.format.FullChunk
import cn.nukkit.nbt.tag.CompoundTag
import net.mcpes.foundhi.plugin.game.has.entity.WalkingAnimal

class Cow(chunk: FullChunk, nbt: CompoundTag) : WalkingAnimal(chunk, nbt) {
    override val killExperience: Int
        get() = 0

    override fun getNetworkId(): Int {
        return NETWORK_ID
    }

    override fun getName(): String {
        return "Cow"
    }

    override fun getWidth(): Float {
        return 0.9f
    }

    override fun getHeight(): Float {
        return if (this.isBaby) {
            0.65f
        } else 1.3f
    }

    override fun getEyeHeight(): Float {
        return if (this.isBaby) {
            0.65f
        } else 1.2f
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
        const val NETWORK_ID = 11
    }
}