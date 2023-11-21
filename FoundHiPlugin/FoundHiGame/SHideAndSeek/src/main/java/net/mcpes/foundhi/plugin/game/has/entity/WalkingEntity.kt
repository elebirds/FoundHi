package net.mcpes.foundhi.plugin.game.has.entity

import cn.nukkit.block.BlockFence
import cn.nukkit.block.BlockFenceGate
import cn.nukkit.block.BlockLiquid
import cn.nukkit.entity.EntityCreature
import cn.nukkit.level.format.FullChunk
import cn.nukkit.math.NukkitMath
import cn.nukkit.math.Vector2
import cn.nukkit.math.Vector3
import cn.nukkit.nbt.tag.CompoundTag
import net.mcpes.foundhi.plugin.game.has.utils.EntityUtils


abstract class WalkingEntity(chunk: FullChunk, nbt: CompoundTag) : BaseEntity(chunk, nbt) {
    protected fun checkTarget() {
        if (this.isKnockback) {
            return
        }

        if (this.followTarget != null && !this.followTarget!!.closed && this.followTarget!!.isAlive) {
            return
        }

        val target = this.target
        if (target !is EntityCreature || !this.targetOption((target as EntityCreature?)!!, this.distanceSquared(target))) {
            var near = Integer.MAX_VALUE.toDouble()

            for (entity in this.getLevel().entities) {
                if (entity === this || entity !is EntityCreature || entity is Animal) {
                    continue
                }

                if (entity is BaseEntity && entity.isFriendly == this.isFriendly) {
                    continue
                }

                val distance = this.distanceSquared(entity)
                if (distance > near || !this.targetOption(entity, distance)) {
                    continue
                }
                near = distance

                this.stayTime = 0
                this.moveTime = 0
                this.target = entity
            }
        }

        // (Spitz4478) this.target must be EntityCreature, open, alive AND meet criteria of targetOption() for this Entity before returning
        if (this.target is EntityCreature && !(this.target as EntityCreature).closed && (this.target as EntityCreature).isAlive && this.targetOption((this.target as EntityCreature?)!!, this.distanceSquared(this.target!!))) {
            return
        }

        val x: Int
        val z: Int
        if (this.stayTime > 0) {
            if (EntityUtils.rand(1, 100) > 5) {
                return
            }
            x = EntityUtils.rand(10, 30)
            z = EntityUtils.rand(10, 30)
            this.target = this.add((if (EntityUtils.rand()) x else -x).toDouble(), (EntityUtils.rand(-20, 20) / 10).toDouble(), (if (EntityUtils.rand()) z else -z).toDouble())
        } else if (EntityUtils.rand(1, 410) == 1) {
            x = EntityUtils.rand(10, 30)
            z = EntityUtils.rand(10, 30)
            this.stayTime = EntityUtils.rand(90, 400)
            this.target = this.add((if (EntityUtils.rand()) x else -x).toDouble(), (EntityUtils.rand(-20, 20) / 10).toDouble(), (if (EntityUtils.rand()) z else -z).toDouble())
        } else if (this.moveTime <= 0 || this.target == null) {
            x = EntityUtils.rand(20, 100)
            z = EntityUtils.rand(20, 100)
            this.stayTime = 0
            this.moveTime = EntityUtils.rand(300, 1200)
            this.target = this.add((if (EntityUtils.rand()) x else -x).toDouble(), 0.0, (if (EntityUtils.rand()) z else -z).toDouble())
        }

    }

    protected fun checkJump(dx: Double, dz: Double): Boolean {
        if (this.motionY == (this.gravity * 2).toDouble()) {
            return this.level.getBlock(Vector3(NukkitMath.floorDouble(this.x).toDouble(), this.y.toInt().toDouble(), NukkitMath.floorDouble(this.z).toDouble())) is BlockLiquid
        } else {
            if (this.level.getBlock(Vector3(NukkitMath.floorDouble(this.x).toDouble(), (this.y + 0.8).toInt().toDouble(), NukkitMath.floorDouble(this.z).toDouble())) is BlockLiquid) {
                this.motionY = (this.gravity * 2).toDouble()
                return true
            }
        }

        if (!this.onGround || this.stayTime > 0) {
            return false
        }

        val that = this.getLevel().getBlock(Vector3(NukkitMath.floorDouble(this.x + dx).toDouble(), this.y.toInt().toDouble(), NukkitMath.floorDouble(this.z + dz).toDouble()))
        if (this.direction == null) {
            return false
        }

        val block = that.getSide(this.horizontalFacing)
        if (!block.canPassThrough() && block.up().canPassThrough() && that.up(2).canPassThrough()) {
            if (block is BlockFence || block is BlockFenceGate) {
                this.motionY = this.gravity.toDouble()
            } else if (this.motionY <= this.gravity * 4) {
                this.motionY = (this.gravity * 4).toDouble()
            } else if (this.motionY <= this.gravity * 8) {
                this.motionY = (this.gravity * 8).toDouble()
            } else {
                this.motionY += this.gravity * 0.25
            }
            return true
        }
        return false
    }

    override fun updateMove(tickDiff: Int): Vector3 {
        if (!this.isMovement) {
            return Vector3()
        }

        if (this.isKnockback) {
            this.move(this.motionX * tickDiff, this.motionY, this.motionZ * tickDiff)
            this.motionY -= (this.gravity * tickDiff).toDouble()
            this.updateMovement()
            return Vector3()
        }

        if (this.followTarget != null && !this.followTarget!!.closed && this.followTarget!!.isAlive) {
            val x = this.followTarget!!.x - this.x
            val y = this.followTarget!!.y - this.y
            val z = this.followTarget!!.z - this.z

            val diff = Math.abs(x) + Math.abs(z)
            if (this.stayTime > 0 || this.distance(this.followTarget) <= (this.width + 0.0) / 2 + 0.05) {
                this.motionX = 0.0
                this.motionZ = 0.0
            } else {
                this.motionX = this.getSpeed() * 0.15 * (x / diff)
                this.motionZ = this.getSpeed() * 0.15 * (z / diff)
            }
            this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff))
            this.pitch = if (y == 0.0) 0.0 else Math.toDegrees(-Math.atan2(y, Math.sqrt(x * x + z * z)))
            return this.followTarget!!
        }

        val before = this.target
        this.checkTarget()
        if (this.target is EntityCreature || before !== this.target) {
            val x = this.target!!.x - this.x
            val y = this.target!!.y - this.y
            val z = this.target!!.z - this.z

            val diff = Math.abs(x) + Math.abs(z)
            if (this.stayTime > 0 || this.distance(this.target) <= (this.width + 0.0) / 2 + 0.05) {
                this.motionX = 0.0
                this.motionZ = 0.0
            } else {
                this.motionX = this.getSpeed() * 0.15 * (x / diff)
                this.motionZ = this.getSpeed() * 0.15 * (z / diff)
            }
            this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff))
            this.pitch = if (y == 0.0) 0.0 else Math.toDegrees(-Math.atan2(y, Math.sqrt(x * x + z * z)))
        }

        val dx = this.motionX * tickDiff
        val dz = this.motionZ * tickDiff
        val isJump = this.checkJump(dx, dz)
        if (this.stayTime > 0) {
            this.stayTime -= tickDiff
            this.move(0.0, this.motionY * tickDiff, 0.0)
        } else {
            val be = Vector2(this.x + dx, this.z + dz)
            this.move(dx, this.motionY * tickDiff, dz)
            val af = Vector2(this.x, this.z)

            if ((be.x != af.x || be.y != af.y) && !isJump) {
                this.moveTime -= 90 * tickDiff
            }
        }

        if (!isJump) {
            if (this.onGround) {
                this.motionY = 0.0
            } else if (this.motionY > -this.gravity * 4) {
                if (this.level.getBlock(Vector3(NukkitMath.floorDouble(this.x).toDouble(), (this.y + 0.8).toInt().toDouble(), NukkitMath.floorDouble(this.z).toDouble())) !is BlockLiquid) {
                    this.motionY -= (this.gravity * 1).toDouble()
                }
            } else {
                this.motionY -= (this.gravity * tickDiff).toDouble()
            }
        }
        this.updateMovement()
        return this.target!!
    }
}