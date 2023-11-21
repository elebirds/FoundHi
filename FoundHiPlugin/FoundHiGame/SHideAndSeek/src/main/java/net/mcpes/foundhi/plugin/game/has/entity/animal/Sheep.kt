package net.mcpes.foundhi.plugin.game.has.entity.animal

import cn.nukkit.entity.Entity
import cn.nukkit.entity.data.ByteEntityData
import cn.nukkit.item.Item
import cn.nukkit.level.format.FullChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.DyeColor
import net.mcpes.foundhi.plugin.game.has.entity.WalkingAnimal
import java.util.concurrent.ThreadLocalRandom

class Sheep(chunk: FullChunk, nbt: CompoundTag) : WalkingAnimal(chunk, nbt) {

    var sheared = false
    var color = 0
        get() = namedTag.getByte("Color")
        set(value) {
            field = color
            this.setDataProperty(ByteEntityData(Entity.DATA_COLOUR, field))
        }

    override val killExperience: Int
        get() = 0 // gain 1-3 experience

    override fun getNetworkId(): Int {
        return NETWORK_ID
    }

    override fun getName(): String {
        return "Sheep"
    }

    override fun getWidth(): Float {
        return if (this.isBaby) {
            0.45f
        } else 0.9f
    }

    override fun getHeight(): Float {
        return if (isBaby) {
            0.65f
        } else 1.3f
    }

    override fun getEyeHeight(): Float {
        return if (isBaby) {
            0.65f
        } else 1.1f
    }

    override fun isBaby(): Boolean {
        return this.getDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_BABY)
    }

    public override fun initEntity() {
        this.maxHealth = 8
        if (!this.namedTag.contains("Color")) {
            this.color = this.randomColor()
        } else {
            this.color = this.namedTag.getByte("Color")
        }

        if (!this.namedTag.contains("Sheared")) {
            this.namedTag.putByte("Sheared", 0)
        } else {
            this.sheared = this.namedTag.getBoolean("Sheared")
        }

        this.setDataFlag(0, 26, this.sheared)
    }

    override fun saveNBT() {
        super.saveNBT()
        this.namedTag.putByte("Color", this.color)
        this.namedTag.putBoolean("Sheared", this.sheared)
    }

    override fun getDrops(): Array<Item> {
        return arrayOf()
    }

    private fun randomColor(): Int {
        val random = ThreadLocalRandom.current()
        val rand = random.nextDouble(1.0, 100.0)

        return if (rand <= 15) {
            if (random.nextBoolean()) DyeColor.BLACK.dyeData else if (random.nextBoolean()) DyeColor.GRAY.dyeData else DyeColor.LIGHT_GRAY.dyeData
        } else DyeColor.WHITE.dyeData

    }

    companion object {
        const val NETWORK_ID = 13

        const val WHITE = 0
        const val ORANGE = 1
        const val MAGENTA = 2
        const val LIGHT_BLUE = 3
        const val YELLOW = 4
        const val LIME = 5
        const val PINK = 6
        const val GRAY = 7
        const val LIGHT_GRAY = 8
        const val CYAN = 9
        const val PURPLE = 10
        const val BLUE = 11
        const val BROWN = 12
        const val GREEN = 13
        const val RED = 14
        const val BLACK = 15
    }

}