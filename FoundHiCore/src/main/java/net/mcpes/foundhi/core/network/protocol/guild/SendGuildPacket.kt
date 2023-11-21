package net.mcpes.foundhi.core.network.protocol.guild

import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.app.data.guild.GuildInterface
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.data.GuildCache
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/22
 * version 1.0
 */

class SendGuildPacket : DataPacket() {
    lateinit var guild: GuildInterface

    override fun pid(): Int {
        return ProtocolInfo.GUILD_SEND_PACKET
    }

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["data"] = guild.toString()
        return json.toString()
    }

    override fun decode(json: JSONObject): DataPacket {
        val `object` = json.getJSONObject("data")
        guild = GuildCache(`object`)
        return this
    }

    override fun handle() {
        if (ServerType.getType() == ServerType.API) return
        val data = guild as GuildCache
        FoundHiPlugin.instance.logger.notice("§b加载公会缓存(guildID:${data.id})")
        FoundHiPlugin.instance.addGuild(data)
    }
}
