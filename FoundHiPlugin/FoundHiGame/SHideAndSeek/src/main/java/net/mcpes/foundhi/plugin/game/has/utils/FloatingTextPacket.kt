package net.mcpes.foundhi.plugin.game.has.utils

import cn.nukkit.Server
import cn.nukkit.entity.Entity
import cn.nukkit.entity.data.EntityMetadata
import cn.nukkit.item.Item
import cn.nukkit.level.Position
import cn.nukkit.network.protocol.AddPlayerPacket
import cn.nukkit.network.protocol.RemoveEntityPacket
import cn.nukkit.network.protocol.SetEntityDataPacket
import java.util.*

object FloatingTextPacket {
    var datas = HashMap<Long, HashMap<String, Any>>()

    fun addFloatingText(title: String, text: String, pos: Position): Long {
        val eid = Entity.entityCount++
        val pk = AddPlayerPacket()
        pk.uuid = UUID.randomUUID()
        pk.username = ""
        pk.entityUniqueId = eid
        pk.entityRuntimeId = eid
        pk.x = pos.x.toFloat()
        pk.y = pos.y.toFloat()
        pk.z = pos.z.toFloat()
        pk.speedX = 0f
        pk.speedY = 0f
        pk.speedZ = 0f
        pk.yaw = 0f
        pk.pitch = 0f
        val flags = 1L shl Entity.DATA_FLAG_CAN_SHOW_NAMETAG or
                (1L shl Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG) or
                (1L shl Entity.DATA_FLAG_IMMOBILE)
        pk.metadata = EntityMetadata()
                .putLong(Entity.DATA_FLAGS, flags)
                .putString(Entity.DATA_NAMETAG, title + "\n" + text)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, -1)
                .putFloat(Entity.DATA_SCALE, 0.01f) //zero causes problems on debug builds?
        pk.item = Item.get(Item.AIR)
        Server.broadcastPacket(Server.getInstance().onlinePlayers.values, pk)
        val data = HashMap<String, Any>()
        data["title"] = title
        data["text"] = text
        data["pos"] = pos
        data["packet"] = pk
        datas[eid] = data
        return eid
    }

    fun setFloatingText(eid: Long, title: String, text: String) {
        val npk = SetEntityDataPacket()
        val flags = 114688L
        npk.metadata = EntityMetadata().putLong(0, flags).putString(4, title + "\n" + text)
        npk.eid = eid
        Server.broadcastPacket(Server.getInstance().onlinePlayers.values, npk)
        val data = datas[eid] ?: return
        data["title"] = title
        data["text"] = text
        val pk = data["packet"] as AddPlayerPacket
        pk.metadata = EntityMetadata().putLong(0, flags).putString(4, title + "\n" + text)
        data["packet"] = pk
        datas[eid] = data
    }

    fun removeFloatingText(eid: Long): Boolean {
        val pk = RemoveEntityPacket()
        pk.eid = eid
        Server.broadcastPacket(Server.getInstance().onlinePlayers.values, pk)
        datas.remove(eid)
        return true
    }
}