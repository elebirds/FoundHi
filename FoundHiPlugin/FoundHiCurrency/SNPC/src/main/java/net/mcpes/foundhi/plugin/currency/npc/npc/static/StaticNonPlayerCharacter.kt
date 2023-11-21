package net.mcpes.foundhi.plugin.currency.npc.npc.static

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.Entity
import cn.nukkit.entity.data.EntityMetadata
import cn.nukkit.entity.data.Skin
import cn.nukkit.item.Item
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.level.Level
import cn.nukkit.level.Location
import cn.nukkit.level.Position
import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.AddPlayerPacket
import cn.nukkit.network.protocol.MobArmorEquipmentPacket
import cn.nukkit.network.protocol.MovePlayerPacket
import cn.nukkit.network.protocol.RemoveEntityPacket
import net.mcpes.foundhi.plugin.currency.npc.SNonPlayerCharacter
import net.mcpes.foundhi.plugin.currency.npc.config.NPCConfig
import net.mcpes.foundhi.plugin.currency.npc.npc.NonPlayerCharacter
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.asin
import kotlin.math.round
import kotlin.math.sqrt

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/9
 * version 1.0
 */
abstract class StaticNonPlayerCharacter(val id: String) : NonPlayerCharacter {
    var namedTag: String = ""
        set(value) {
            field = value
            if (create) this.spawnToAll()
        }

    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0
    var yaw: Double = 0.0
    var pitch: Double = 0.0
    lateinit var level: String

    var scale: Float = 0.toFloat()
        set(value) {
            field = value
            if (create) this.spawnToAll()
        }

    var handItem: Item = Item(0)
        set(value) {
            field = value
            if (create) this.spawnToAll()
        }
    lateinit var handInfo: HashMap<String, Any>
    var equipmentItems: HashMap<String, Item> = HashMap()
        set(value) {
            field = value
            if (create) this.spawnToAll()
        }
    lateinit var equipmentInfos: HashMap<String, HashMap<String, Any>>
    lateinit var skinFile: String
    var skin = Skin(ByteArray(Skin.SINGLE_SKIN_SIZE), "Standard_Custom")
        set(skin) {
            field = skin
            if (create) this.spawnToAll()
        }
    private var entity: NPCEntity? = null
    var create = true

    abstract fun load(): Boolean

    protected fun load(type: SNonPlayerCharacter.NPCType): Boolean {
        try {
            val data: HashMap<String, Any>
            when (type) {
                SNonPlayerCharacter.NPCType.Reply -> data = NPCConfig.reply.get(id) as HashMap<String, Any>
                SNonPlayerCharacter.NPCType.Command -> data = NPCConfig.command.get(id) as HashMap<String, Any>
                SNonPlayerCharacter.NPCType.Teleport -> data = NPCConfig.teleport.get(id) as HashMap<String, Any>
                SNonPlayerCharacter.NPCType.Transfer -> data = NPCConfig.transfer.get(id) as HashMap<String, Any>
                else -> data = HashMap()
            }
            this.namedTag = data["namedTag"].toString()
            this.x = data["x"] as Double
            this.y = data["y"] as Double
            this.z = data["z"] as Double
            this.yaw = data["yaw"] as Double
            this.pitch = data["pitch"] as Double
            this.level = data["level"].toString()
            this.scale = 1.0f//defalut 1.0
            this.handInfo = data["hand"] as HashMap<String, Any>
            this.equipmentInfos = data["equipment"] as HashMap<String, HashMap<String, Any>>
            this.equipmentItems = HashMap()
            this.handItem = Item(handInfo["id"] as Int, handInfo["damage"] as Int)
            if (handInfo["enchant"] as Boolean) handItem.addEnchantment(Enchantment.get(1).setLevel(1))
            for (key in equipmentInfos.keys) {
                when (key) {
                    "helmet", "chestplate", "leggings", "boots" -> {
                        val item = Item.get(equipmentInfos[key]!!["id"] as Int)
                        if (equipmentInfos[key]!!["enchant"] as Boolean) {
                            item.addEnchantment(Enchantment.get(1).setLevel(1))
                        }
                        equipmentItems[key] = item
                    }
                    else -> {
                    }
                }
            }
            val air = Item.get(0, 0, 1)
            equipmentItems.putIfAbsent("helmet", air)
            equipmentItems.putIfAbsent("chestplate", air)
            equipmentItems.putIfAbsent("leggings", air)
            equipmentItems.putIfAbsent("boots", air)
            this.skinFile = data.getOrDefault("skin", "Steve.png").toString()
            return true
        } catch (e: Exception) {
            return false
        }

    }

    fun createEntity() {
        this.entity = NPCEntity(this, x, y, z, yaw, pitch, Server.getInstance().getLevelByName(level))
    }

    fun baseMap(): HashMap<String, Any> {
        val data = HashMap<String, Any>()
        data["namedTag"] = namedTag
        data["x"] = x
        data["y"] = y
        data["z"] = z
        data["yaw"] = yaw
        data["pitch"] = pitch
        data["level"] = level
        data["scale"] = scale
        data["hand"] = handInfo
        data["equipment"] = equipmentInfos
        return data
    }

    abstract fun save()

    abstract fun onTouch(player: Player)

    fun setPos(location: Location) {
        this.x = location.x
        this.y = location.y
        this.z = location.z
        this.yaw = location.yaw
        this.pitch = location.pitch
        this.level = location.level.name
        if (create) this.spawnToAll()
    }

    fun getEntity(): NPCEntity {
        if (entity == null) {
            this.createEntity()
        }
        return entity!!
    }

    fun spawnToAll() {
        if (this.entity == null) {
            this.createEntity()
        }
        this.getEntity().spawnToAll()
    }

    abstract fun remove(): Boolean

    companion object {
        fun playerMove(player: Player) {
            SNonPlayerCharacter.npcs.forEach { _, npc ->
                if (npc is StaticNonPlayerCharacter) {
                    if (npc.getEntity().distance(player.location) <= 10) {
                        npc.getEntity().look(player)
                    }
                }
            }
        }
    }

    class NPCEntity(private val npc: StaticNonPlayerCharacter, x: Double, y: Double, z: Double, yaw: Double, pitch: Double, level: Level) : Location(x, y, z, yaw, pitch, level) {
        val eid: Long = Entity.entityCount++
        private val uuid: UUID = UUID(Random().nextLong(), Random().nextLong())

        fun teleport(pos: Vector3) {
            this.x = pos.x
            this.y = pos.y
            this.z = pos.z
            if (pos is Position) {
                if (pos.level != null) {
                    this.level = pos.getLevel()
                    this.npc.level = this.level.name
                    if (pos is Location) {
                        this.yaw = pos.yaw
                        this.pitch = pos.pitch
                    }
                    this.spawnToAll()
                }
            } else {
                this.moveEntity()
            }
        }

        fun despawnTo(player: Player) {
            val pk = RemoveEntityPacket()
            pk.eid = this.eid
            player.dataPacket(pk)
        }

        fun despawnToAll() {
            val players = ArrayList<Player>()
            if (level != null) {
                players.addAll(level.players.values)
            } else {
                players.addAll(Server.getInstance().onlinePlayers.values)
            }
            for (p in players) {
                this.despawnTo(p)
            }
        }

        fun spawnTo(player: Player): Boolean {
            if (level == null) {
                level = player.getLevel()
            }
            if (this.level.name != "" && level.folderName.toLowerCase() != this.level.name.toLowerCase()) {
                this.despawnTo(player)
                return false
            }
            Server.getInstance().updatePlayerListData(this.uuid, this.eid, this.npc.namedTag, this.npc.skin, arrayOf(player))
            val pk = AddPlayerPacket()
            pk.username = this.npc.namedTag
            pk.entityUniqueId = this.eid
            pk.entityRuntimeId = this.eid
            pk.uuid = this.uuid
            pk.x = this.x.toFloat()
            pk.y = this.y.toFloat()
            pk.z = this.z.toFloat()
            pk.yaw = this.yaw.toFloat()
            pk.pitch = this.pitch.toFloat()
            pk.speedX = 0f
            pk.speedY = 0f
            pk.speedZ = 0f
            pk.item = this.npc.handItem
            var flags: Long = 0x00
            flags = flags xor (1 shl Entity.DATA_FLAG_CAN_SHOW_NAMETAG).toLong()
            flags = flags xor (1 shl Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG).toLong()
            pk.metadata = EntityMetadata()
                    .putLong(Entity.DATA_FLAGS, flags)
                    .putShort(Entity.DATA_AIR, 400)
                    .putShort(Entity.DATA_MAX_AIR, 400)
                    .putString(Entity.DATA_NAMETAG, this.npc.namedTag)
                    .putLong(Entity.DATA_LEAD_HOLDER_EID, -1)
                    .putFloat(Entity.DATA_SCALE, this.npc.scale)
            player.dataPacket(pk)
            Server.getInstance().removePlayerListData(this.uuid, arrayOf(player))
            sendEquipments(arrayOf(this.npc.equipmentItems.getOrDefault("helmet", Item(0)), this.npc.equipmentItems.getOrDefault("chestplate", Item(0)), this.npc.equipmentItems.getOrDefault("leggings", Item(0)), this.npc.equipmentItems.getOrDefault("boots", Item(0))), player)
            return true
        }

        fun spawnToAll() {
            val players = ArrayList<Player>()
            if (level != null) {
                players.addAll(level.players.values)
            } else {
                players.addAll(Server.getInstance().onlinePlayers.values)
            }
            for (p in players) {
                this.spawnTo(p)
            }
        }

        fun moveEntity() {
            val pk = MovePlayerPacket()
            pk.eid = this.eid
            pk.x = this.x.toFloat()
            pk.y = (this.y + 1.62).toFloat()
            pk.z = this.z.toFloat()
            pk.yaw = this.yaw.toFloat()
            pk.headYaw = pk.yaw
            pk.pitch = this.pitch.toFloat()
            pk.mode = 0
            for (p in Server.getInstance().onlinePlayers.values) {
                (p as Player).dataPacket(pk)
            }
        }

        fun sendEquipments(items: Array<Item>, player: Player) {
            val armorPk = MobArmorEquipmentPacket()
            armorPk.eid = this.eid
            armorPk.slots = items
            armorPk.encode()
            armorPk.isEncoded = true
            player.dataPacket(armorPk)
        }

        fun sendEquipmentToAll(item: Array<Item>) {
            for (p in Server.getInstance().onlinePlayers.values) {
                this.sendEquipments(item, p)
            }
        }

        fun look(player: Player) {
            val x = this.x - player.x
            val y = this.y - player.y
            val z = this.z - player.z
            var yaw = asin(x / sqrt(x * x + z * z)) / 3.14 * 180
            val pitch = round(asin(y / sqrt(x * x + z * z + y * y)) / 3.14 * 180)
            if (z > 0) {
                yaw = -yaw + 180
            }
            val pk = MovePlayerPacket()
            pk.eid = this.eid
            pk.x = this.x.toFloat()
            pk.y = ((this.y + 1.62).toFloat())
            pk.z = this.z.toFloat()
            pk.yaw = yaw.toFloat()
            pk.headYaw = pk.yaw
            pk.pitch = pitch.toFloat()
            pk.mode = 0
            player.dataPacket(pk)
        }

        fun close() {
            this.despawnToAll()
        }
    }
}