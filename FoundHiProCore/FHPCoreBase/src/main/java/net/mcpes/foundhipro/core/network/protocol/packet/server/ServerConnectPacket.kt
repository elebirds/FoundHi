package net.mcpes.foundhipro.core.network.protocol.packet.server

import com.google.gson.Gson
import io.netty.channel.Channel
import net.mcpes.foundhipro.core.ServerType
import net.mcpes.foundhipro.core.network.protocol.ProtocolInfo
import net.mcpes.foundhipro.core.network.protocol.packet.DataPacket
import net.mcpes.foundhipro.core.network.protocol.packet.ManuallyCode
import net.mcpes.foundhipro.utils.cast

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/30
 * version 1.0
 */
class ServerConnectPacket : DataPacket(), ManuallyCode {
    var id: String = ""
    var name: String = ""
    var password: String = ""
    var port: Int = 0
    var ip: String = ""
    var channel: Channel? = null
    var rsaGet: Boolean = false
    var publicKey: String = ""

    override fun pid() = ProtocolInfo.SERVER_CONNECT_PACKET

    override fun decode(data: MutableMap<*, *>): DataPacket {
        this.id = data["id"].cast()
        this.rsaGet = data["rg"].cast()
        if (rsaGet) {
            if (ServerType.type != ServerType.ServerType.API) {
                this.publicKey = data["pk"].cast()
            } else {
                this.name = data["name"].cast()
                this.port = data["port"].cast()
                this.ip = data["ip"].cast()
            }
        } else {
            if (ServerType.type == ServerType.ServerType.API) this.password = data["pw"].toString()
        }
        return this
    }

    override fun encode(): String {
        val data = hashMapOf<String, Any?>()
        data["pid"] = this.pid()
        data["id"] = id
        data["rg"] = rsaGet
        if (rsaGet) {
            if (ServerType.type == ServerType.ServerType.API) {
                data["pk"] = publicKey
            } else {
                data["name"] = name
                data["ip"] = ip
                data["port"] = port
            }
        } else {
            if (ServerType.type != ServerType.ServerType.API) {
                data["pw"] = this.password
            }
        }
        return Gson().toJson(data)
    }
}
