package net.mcpes.foundhi.core.utils

import cn.nukkit.level.Location
import cn.nukkit.level.format.FullChunk
import cn.nukkit.nbt.tag.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/20
 * version 1.0
 */
object EntityUtils {
    @JvmStatic
    fun getNBT(pos: Location?): CompoundTag {
        return if (pos == null) {
            CompoundTag()
                    .putList(ListTag<Tag>("Pos").add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)))
                    .putList(ListTag<Tag>("Motion").add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)))
                    .putList(ListTag<Tag>("Rotation").add(FloatTag("", 0.0f)).add(FloatTag("", 0.0f)));
        } else {
            CompoundTag()
                    .putList(ListTag<Tag>("Pos").add(DoubleTag("", pos.x)).add(DoubleTag("", pos.y)).add(DoubleTag("", pos.z)))
                    .putList(ListTag<Tag>("Motion").add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)))
                    .putList(ListTag<Tag>("Rotation").add(FloatTag("", pos.yaw.toFloat())).add(FloatTag("", pos.pitch.toFloat())))
        }
    }

    @JvmStatic
    fun getChunk(source: Location): FullChunk {
        return source.getLevel().getChunk(source.x.toInt() shr 4, source.z.toInt() shr 4, true)
    }
}