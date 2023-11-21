package net.mcpes.foundhi.plugin.game.has.entity

import cn.nukkit.Player
import cn.nukkit.entity.Entity
import cn.nukkit.entity.EntityAgeable
import cn.nukkit.entity.data.ShortEntityData
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.level.format.FullChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.potion.Effect
import co.aikar.timings.Timings
import net.mcpes.foundhi.plugin.game.has.entity.animal.Chicken
import net.mcpes.foundhi.plugin.game.has.entity.animal.Cow
import net.mcpes.foundhi.plugin.game.has.entity.animal.Pig
import net.mcpes.foundhi.plugin.game.has.entity.animal.Sheep

abstract class WalkingAnimal(chunk: FullChunk, nbt: CompoundTag) : WalkingEntity(chunk, nbt), EntityAgeable {
    private val updateTime = 50

    override fun initEntity() {
        super.initEntity()

        if (this.getDataFlag(Entity.DATA_FLAG_BABY, 0)) {
            this.setDataFlag(Entity.DATA_FLAG_BABY, Entity.DATA_TYPE_BYTE)
        }
    }

    override fun isBaby(): Boolean {
        return this.getDataFlag(Entity.DATA_FLAG_BABY, 0)
    }

    override fun entityBaseTick(tickDiff: Int): Boolean {
        var hasUpdate: Boolean
        Timings.entityBaseTickTimer.startTiming()

        hasUpdate = super.entityBaseTick(tickDiff)

        if (!this.hasEffect(Effect.WATER_BREATHING) && this.isInsideOfWater) {
            hasUpdate = true
            var airTicks = this.getDataPropertyShort(Entity.DATA_AIR) - tickDiff
            if (airTicks <= -20) {
                airTicks = 0
                this.attack(EntityDamageEvent(this, DamageCause.DROWNING, 2f))
            }
            this.setDataProperty(ShortEntityData(Entity.DATA_AIR, airTicks))
        } else {
            this.setDataProperty(ShortEntityData(Entity.DATA_AIR, 300))
        }

        Timings.entityBaseTickTimer.stopTiming()
        return hasUpdate
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (this.closed) {
            return false
        }

        if (!this.isAlive) {
            if (++this.deadTicks >= 23) {
                this.close()
                return false
            }
            return true
        }

        val tickDiff = currentTick - this.lastUpdate
        this.lastUpdate = currentTick
        this.entityBaseTick(tickDiff)

        val target = this.updateMove(tickDiff)
        if (target is Player) {
            if (this.distanceSquared(target) <= 2) {
                this.pitch = 22.0
                this.x = this.lastX
                this.y = this.lastY
                this.z = this.lastZ
            }
        } else if (Math.pow(this.x - target.x, 2.0) + Math.pow(this.z - target.z, 2.0) <= 1) {
            this.moveTime = 0
        }
        return true
    }

    companion object {
        fun getNetWorkID(type: Int): Int {
            return when (type) {
                1 -> Chicken.NETWORK_ID
                2 -> Cow.NETWORK_ID
                3 -> Pig.NETWORK_ID
                else -> Sheep.NETWORK_ID
            }
        }
    }
}
