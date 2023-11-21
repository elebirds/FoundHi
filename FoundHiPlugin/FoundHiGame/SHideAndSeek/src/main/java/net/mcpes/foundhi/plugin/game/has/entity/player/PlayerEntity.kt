package net.mcpes.foundhi.plugin.game.has.entity.player

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.Entity
import cn.nukkit.entity.EntityCreature
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.level.format.FullChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.AddEntityPacket
//import cn.nukkit.network.protocol.MoveEntityPacket
import cn.nukkit.network.protocol.RemoveEntityPacket
import net.mcpes.foundhi.plugin.game.has.entity.WalkingAnimal

class PlayerEntity(chunk: FullChunk, tag: CompoundTag, var player: Player, type: Int) : EntityCreature(chunk, tag) {
    private var eid: Long = -1
    private var nid: Int = -1
    private var spawned: Boolean = false

    override fun getNetworkId(): Int {
        return nid
    }

    override fun attack(source: EntityDamageEvent?): Boolean {
        return player.attack(source)
    }

    private fun getSpawnPacket() {
        val pk = AddEntityPacket()
        pk.x = player.x.toFloat()
        pk.y = player.y.toFloat()
        pk.z = player.z.toFloat()
        pk.yaw = player.yaw.toFloat()
        pk.pitch = player.pitch.toFloat()
        pk.speedX = 0f
        pk.speedY = 0f
        pk.speedZ = 0f
        pk.entityRuntimeId = eid
        pk.entityUniqueId = eid
        pk.type = nid
        pk.metadata = this.dataProperties;
        player.dataPacket(pk)
    }

    override fun spawnTo(player: Player?) {

    }

    override fun spawnToAll() {

    }

    fun spawnTo(player: Player, packet: AddEntityPacket) {
        player.dataPacket(packet)
    }

    fun spawnToAll(players: ArrayList<Player>) {
        val pk = getSpawnPacket() as AddEntityPacket
        for (player in players) {
            if (player != this.player) {
                this.spawnTo(player, pk)
            }
        }
        spawned = true
    }

    override fun despawnFromAll() {
        val pk = RemoveEntityPacket()
        pk.eid = this.eid
        for (player: Player in Server.getInstance().onlinePlayers.values) {
            player.dataPacket(pk)
        }
    }

    /*fun moveTo(player: Player?, packet: MoveEntityPacket) {
        if (player == null) return
        player.dataPacket(packet)
    }*/

    fun moveToAll(players: ArrayList<Player>) {
        //val pk = getMovePacket()
        //players.forEach { this.moveTo(player, pk) }
    }

    /*private fun getMovePacket(): MoveEntityPacket {
        val pk = MoveEntityPacket()
        pk.eid = this.eid
        pk.x = player.x
        pk.y = player.y
        pk.z = player.z
        pk.headYaw = player.yaw
        pk.pitch = player.pitch
        pk.teleport = false
        return pk
    }*/

    init {
        nid = WalkingAnimal.getNetWorkID(type)
        eid = Entity.entityCount++
    }
}