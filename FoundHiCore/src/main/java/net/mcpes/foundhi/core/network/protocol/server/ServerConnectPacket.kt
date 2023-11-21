package net.mcpes.foundhi.core.network.protocol.server

import io.netty.channel.Channel
import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.ServerType.API
import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.data.server.NoProveServer
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.network.server.listener.ServerListener.overTime
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.banned.PlayerBannedData
import net.mcpes.foundhi.core.utils.encryption.RSA
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/30
 * version 1.0
 */
class ServerConnectPacket : DataPacket() {
    lateinit var id: String
    @JvmField
    var name: String = ""
    @JvmField
    var password: String = ""
    @JvmField
    var port: Int = 0
    @JvmField
    var ip: String = ""
    @JvmField
    var type: Int = 0
    @JvmField
    var cid: String = ""
    @JvmField
    var channel: Channel? = null
    @JvmField
    var rsaGet: Boolean = false
    @JvmField
    var publicKey: String = ""

    override fun pid(): Int {
        return ProtocolInfo.SERVER_CONNECT_PACKET
    }

    override fun decode(json: JSONObject): DataPacket {
        this.id = json.getString("id")
        this.rsaGet = json.getBoolean("rg")
        if (rsaGet) {
            if (ServerType.getType() != API) {
                this.publicKey = json["pk"].toString()
            } else {
                this.name = json.getString("name")
                this.type = json.getInt("type")
                this.port = json.getInt("port")
                this.ip = json.getString("ip")
                this.cid = json.getString("c")
            }
        } else {
            if (ServerType.getType() == API) this.password = json["pw"].toString()
        }
        return this
    }

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["id"] = id
        json["rg"] = rsaGet
        if (rsaGet) {
            if (ServerType.getType() == API) {
                json["pk"] = publicKey
            } else {
                json["name"] = name
                json["type"] = type
                json["ip"] = ip
                json["port"] = port
                json["c"] = cid
            }
        } else {
            if (ServerType.getType() != API) {
                json["pw"] = this.password
            }
        }
        return json.toString()
    }

    override fun handle() {
        if (ServerType.getType() == API) {
            if (APIFunction.instance.isServerOnline(this.id)) {
                APIFunction.instance.serverShutdown(APIFunction.instance.getServer(this.id)!!, false)
            }
            if (!rsaGet) {
                if (!APIFunction.instance.isProveServer(id)) return
                val ps = APIFunction.instance.getProveServer(id)!!
                val un = RSA.privateDecrypt(this.password, ps.privateKey)
                if (un == APIFunction.instance.serverSecretKey) {
                    ps.verification = true
                    if (ps.type == ServerType.HUB) APIFunction.instance.addHub(id)
                    ps.toProvenServer()
                    val server = APIFunction.instance.getServer(this.id)!!
                    server.dataPacket(this)
                    val stp1 = SendTopPacket()
                    stp1.type = 1
                    stp1.data = APIFunction.instance.money1TopS
                    val stp2 = SendTopPacket()
                    stp2.type = 2
                    stp2.data = APIFunction.instance.money2TopS
                    val stp3 = SendTopPacket()
                    stp3.type = 3
                    stp3.data = APIFunction.instance.money3TopS
                    APIFunction.instance.sendBannedData(server)
                    server.dataPacket(stp1)
                    server.dataPacket(stp2)
                    server.dataPacket(stp3)
                } else {
                    APIFunction.instance.serverShutdown(ps, true)
                    return
                }
            } else {
                val server = NoProveServer(this.id, this.name, this.ip, this.port, channel!!, this.cid, this.type)
                val rsaKey = RSA.createKeys(2048)
                this.publicKey = rsaKey[RSA.RSA_PUBLIC_KEY].toString()
                server.privateKey = rsaKey[RSA.RSA_PRIVATE_KEY].toString()
                APIFunction.instance.addProveServer(server)
                overTime[channel!!] = System.currentTimeMillis()
                server.dataPacket(this)
            }
        } else {
            if (!rsaGet) {
                FoundHiPlugin.instance.setProved(true)
                PlayerBannedData.loadAll()
                FoundHiPlugin.instance.overTime = System.currentTimeMillis()
            } else {
                this.rsaGet = false
                this.password = RSA.publicEncrypt(FoundHiPlugin.SERVER_SECRET_KEY, RSA.getPublicKey(this.publicKey))
                FoundHiPlugin.instance.dataPacket(this, true)
            }
        }
    }
}
