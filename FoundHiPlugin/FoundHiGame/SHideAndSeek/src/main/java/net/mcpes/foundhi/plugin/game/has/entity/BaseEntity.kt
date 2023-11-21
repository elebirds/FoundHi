package net.mcpes.foundhi.plugin.game.has.entity

import cn.nukkit.Player
import cn.nukkit.entity.Entity
import cn.nukkit.entity.EntityCreature
import cn.nukkit.entity.data.ByteEntityData
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityMotionEvent
import cn.nukkit.item.Item
import cn.nukkit.level.Level
import cn.nukkit.level.format.FullChunk
import cn.nukkit.math.NukkitMath
import cn.nukkit.math.Vector3
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.AddEntityPacket
import co.aikar.timings.Timings

abstract class BaseEntity(chunk: FullChunk, nbt: CompoundTag) : EntityCreature(chunk, nbt) {
    var speed = 1.0f
    var moveMultifier = 1.0
    var inWater = false
    var inLava = false
    var onClimbable = false
    protected var target: Vector3? = null
    protected var stayTime = 0
    protected var moveTime = 0
    protected var followTarget: Entity? = null
    protected var fireProof = false
    val maxJumpHeight = 1.2f // default: 1 block jump height - this should be 2 for horses e.g.
    var isFriendly = false
    var isMovement = true
    var isWallCheck = true
    private val source: EntityDamageEvent? = null

    abstract val killExperience: Int

    val isKnockback: Boolean
        get() = this.attackTime > 0

    val maxFallHeight: Int
        get() {
            if (this.target !is Entity) {
                return 3
            } else {
                var i = (this.getHealth() - this.maxHealth * 0.33f).toInt()
                i -= (3 - this.getServer().difficulty) * 4

                if (i < 0) {
                    i = 0
                }

                return i + 3
            }
        }

    abstract fun updateMove(tickDiff: Int): Vector3

    fun getSpeed(): Double {
        return this.speed.toDouble()
    }

    fun getTarget(): Entity? {
        return if (this.followTarget != null) this.followTarget else if (this.target is Entity) this.target as Entity? else null
    }

    fun setTarget(target: Entity) {
        this.followTarget = target

        this.moveTime = 0
        this.stayTime = 0
        this.target = null
    }

    override fun initEntity() {
        super.initEntity()

        if (this.namedTag.contains("Movement")) {
            this.isMovement = this.namedTag.getBoolean("Movement")
        }

        if (this.namedTag.contains("WallCheck")) {
            this.isWallCheck = this.namedTag.getBoolean("WallCheck")
        }

        this.setDataProperty(ByteEntityData(Entity.DATA_FLAG_NO_AI, 1.toByte().toInt()))
    }

    override fun saveNBT() {
        super.saveNBT()
        this.namedTag.putBoolean("Movement", this.isMovement)
        this.namedTag.putBoolean("WallCheck", this.isWallCheck)
    }

    override fun spawnTo(player: Player) {
        if (!this.hasSpawned.containsKey(player.loaderId) && player.usedChunks.containsKey(Level.chunkHash(this.chunk.x, this.chunk.z))) {
            val pk = AddEntityPacket()
            pk.entityRuntimeId = this.getId()
            pk.entityUniqueId = this.getId()
            pk.type = this.networkId
            pk.x = this.x.toFloat()
            pk.y = this.y.toFloat()
            pk.z = this.z.toFloat()
            pk.speedZ = 0f
            pk.speedY = pk.speedZ
            pk.speedX = pk.speedY
            pk.yaw = this.yaw.toFloat()
            pk.pitch = this.pitch.toFloat()
            pk.metadata = this.dataProperties
            player.dataPacket(pk)
            this.hasSpawned[player.loaderId] = player
        }
    }

    override fun updateMovement() {
        if (this.lastX != this.x || this.lastY != this.y || this.lastZ != this.z || this.lastYaw != this.yaw || this.lastPitch != this.pitch) {
            this.lastX = this.x
            this.lastY = this.y
            this.lastZ = this.z
            this.lastYaw = this.yaw
            this.lastPitch = this.pitch
            this.addMovement(this.x, this.y, this.z, this.yaw, this.pitch, this.yaw)
        }
    }

    fun targetOption(creature: EntityCreature, distance: Double): Boolean {
        if (this is Monster) {
            if (creature is Player) {
                return !creature.closed && creature.spawned && creature.isAlive && creature.isSurvival && distance <= 80
            }
            return creature.isAlive && !creature.closed && distance <= 80
        } else if (this is WalkingAnimal) {
            return if (creature is Player) {
                creature.spawned && creature.isAlive && !creature.closed && creature.inventory.itemInHand.id == Item.WHEAT && distance <= 49
            } else creature.isAlive && creature.closed && distance <= 50
        }
        return false
    }


    override fun isInsideOfSolid(): Boolean {
        val block = this.level.getBlock(this.temporalVector.setComponents(NukkitMath.floorDouble(this.x).toDouble(), NukkitMath.floorDouble(this.y + this.height - 0.18f).toDouble(), NukkitMath.floorDouble(this.z).toDouble()))
        val bb = block.boundingBox
        return bb != null && block.isSolid && !block.isTransparent && bb.intersectsWith(this.getBoundingBox())
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (false) {
            if (this.isKnockback && source is EntityDamageByEntityEvent && source.damager is Player) {
                return false
            }

            if (source is EntityDamageByEntityEvent) {
                val attacker = source.damager
                if (this.attackTime > 0 && attacker is Player) {
                    source.setCancelled(true)
                    return false
                }

                val motion = Vector3(this.x - attacker.x, this.y - attacker.y, this.z - attacker.z).normalize()
                this.motionX = motion.x * 0.19
                this.motionZ = motion.z * 0.19
            }
            super.attack(source)

            this.stayTime = 0
            this.moveTime = 0
            this.target = null
            this.attackTime = 7
        } else
            source.isCancelled = true
        return false
    }

    override fun setMotion(motion: Vector3): Boolean {
        if (!this.justCreated) {
            val ev = EntityMotionEvent(this, motion)
            this.server.pluginManager.callEvent(ev)
            if (ev.isCancelled) {
                return false
            }
        }

        this.motionX = motion.x
        this.motionY = motion.y
        this.motionZ = motion.z
        return true
    }

    override fun move(dx: Double, dy: Double, dz: Double): Boolean {
        var dx = dx
        var dy = dy
        var dz = dz
        Timings.entityMoveTimer.startTiming()

        val movX = dx * moveMultifier
        val movY = dy
        val movZ = dz * moveMultifier

        val list = this.level.getCollisionCubes(this, if (this.level.tickRate > 1) this.boundingBox.getOffsetBoundingBox(dx, dy, dz) else this.boundingBox.addCoord(dx, dy, dz))
        if (this.isWallCheck) {
            for (bb in list) {
                dx = bb.calculateXOffset(this.boundingBox, dx)
            }
            this.boundingBox.offset(dx, 0.0, 0.0)

            for (bb in list) {
                dz = bb.calculateZOffset(this.boundingBox, dz)
            }
            this.boundingBox.offset(0.0, 0.0, dz)
        }
        for (bb in list) {
            dy = bb.calculateYOffset(this.boundingBox, dy)
        }
        this.boundingBox.offset(0.0, dy, 0.0)

        this.setComponents(this.x + dx, this.y + dy, this.z + dz)
        this.checkChunks()

        this.checkGroundState(movX, movY, movZ, dx, dy, dz)
        this.updateFallState(this.onGround)

        Timings.entityMoveTimer.stopTiming()
        return true
    }

}
