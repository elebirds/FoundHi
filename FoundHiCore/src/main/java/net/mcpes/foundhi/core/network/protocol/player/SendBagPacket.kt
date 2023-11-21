package net.mcpes.foundhi.core.network.protocol.player

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.app.APIFunction

import net.mcpes.foundhi.core.handle.bag.LoadBagHandle
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.sf.json.JSONArray
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/1
 * version 1.0
 */
class SendBagPacket : DataPacket() {
    lateinit var name: String
    lateinit var id: String
    lateinit var data: ByteArray

    override fun pid(): Int = ProtocolInfo.PLAYER_SEND_BAG_PACKET

    override fun decode(json: JSONObject): DataPacket {
        this.name = json["p"] as String
        this.id = json["id"] as String
        val s = json["data"] as JSONArray
        this.data = ByteArray(s.size)
        var i = 0
        s.forEach {
            this.data[i] = (it as Int).toByte()
            i++
        }
        return this
    }

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["id"] = id
        json["p"] = name
        json["data"] = JSONArray.fromObject(data).toString()
        return json.toString()
    }

    override fun handle() {
        if (ServerType.getType() == ServerType.API) {
            if (!APIFunction.instance.isPlayerOnline(name)) return
            APIFunction.instance.getPlayer(name)!!.bags[id] = data
        } else {
            val player = Server.getInstance().getPlayerExact(name)
            val tag: CompoundTag = NBTIO.read(data)
            if (player == null || (!player.isOnline)) {
                FoundHiPlugin.instance.addLoadBagHandle(LoadBagHandle(name, tag))
            } else {
                player.inventory.clearAll()
                tag.allTags.forEach {
                    val key: Int = it.name.toInt()
                    when (key) {
                        in 0..Player.SURVIVAL_SLOTS -> {
                            player.inventory.setItem(key, LoadBagHandle.tagToItem(tag))
                        }
                        else -> {
                            val real: Int = key - Player.SURVIVAL_SLOTS
                            player.inventory.setArmorItem(real, LoadBagHandle.tagToItem(tag))
                        }
                    }
                }
            }
        }
    }
}