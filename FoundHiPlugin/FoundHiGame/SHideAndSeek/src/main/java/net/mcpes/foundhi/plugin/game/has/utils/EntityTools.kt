package net.mcpes.foundhi.plugin.game.has.utils

import cn.nukkit.entity.Entity
import cn.nukkit.entity.weather.EntityLightning
import cn.nukkit.level.Location
import cn.nukkit.level.Position
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.DoubleTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import net.mcpes.foundhi.plugin.game.has.entity.animal.Chicken
import net.mcpes.foundhi.plugin.game.has.entity.animal.Cow
import net.mcpes.foundhi.plugin.game.has.entity.animal.Pig
import net.mcpes.foundhi.plugin.game.has.entity.animal.Sheep

object EntityTools {
    fun create(type: Int, source: Position): Entity {
        val chunk = source.getLevel().getChunk(source.x.toInt() shr 4, source.z.toInt() shr 4, true)
        val nbt = CompoundTag().putList(ListTag<DoubleTag>("Pos").add(DoubleTag("", source.x)).add(DoubleTag("", source.y)).add(DoubleTag("", source.z)))
                .putList(ListTag<DoubleTag>("Motion").add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)))
                .putList(ListTag<FloatTag>("Rotation").add(FloatTag("", (source as? Location)?.yaw?.toFloat() ?: 0f))
                        .add(FloatTag("", (source as? Location)?.pitch?.toFloat() ?: 0f)))
        return when (type) {
            1 -> Chicken(chunk, nbt)
            2 -> Cow(chunk, nbt)
            3 -> Pig(chunk, nbt)
            else -> Sheep(chunk, nbt)
        }
    }

    private fun getSomeEntityNBT(): CompoundTag {
        return CompoundTag()
                .putList(ListTag<DoubleTag>("Pos").add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)))
                .putList(ListTag<DoubleTag>("Motion").add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)))
                .putList(ListTag<FloatTag>("Rotation").add(FloatTag("", 0.0f)).add(FloatTag("", 0.0f)))
    }

    fun getEntityNBT(source: Position): CompoundTag {
        return CompoundTag().putList(ListTag<DoubleTag>("Pos").add(DoubleTag("", source.x)).add(DoubleTag("", source.y)).add(DoubleTag("", source.z)))
                .putList(ListTag<DoubleTag>("Motion").add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)))
                .putList(ListTag<FloatTag>("Rotation").add(FloatTag("", (source as? Location)?.yaw?.toFloat() ?: 0f))
                        .add(FloatTag("", (source as? Location)?.pitch?.toFloat() ?: 0f)))
    }

    fun callThunder(location: Location) {
        val x1 = location.getX().toInt()
        val z1 = location.getZ().toInt()
        val t = EntityLightning(location.getLevel().getChunk(x1 shr 4, z1 shr 4, false), getSomeEntityNBT())
        t.setPosition(location)
        t.spawnToAll()
    }
}