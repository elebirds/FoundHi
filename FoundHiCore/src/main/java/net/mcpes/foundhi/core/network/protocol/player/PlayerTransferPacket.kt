package net.mcpes.foundhi.core.network.protocol.player

import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.sf.json.JSONObject
import java.net.InetSocketAddress

class PlayerTransferPacket : DataPacket() {
    lateinit var server_id: String
    lateinit var player_id: String
    lateinit var to_IP: String
    var to_Port: Int = 0
    lateinit var source: String

    override fun pid(): Int {
        return ProtocolInfo.PLAYER_TRANSFER_PACKET
    }

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["s_id"] = server_id
        json["p_id"] = player_id
        json["s"] = source
        if (ServerType.getType() == ServerType.API) {
            json["ti"] = to_IP
            json["tp"] = to_Port
        }
        return json.toString()
    }

    override fun decode(json: JSONObject): DataPacket {
        this.server_id = json.getString("s_id")
        this.player_id = json.getString("p_id")
        if (ServerType.getType() == ServerType.API) {
            this.source = json.getString("s")
        }
        if (ServerType.getType() != ServerType.API && server_id != FoundHiPlugin.instance.id) {
            this.source = json.getString("s")
            this.to_IP = json.getString("ti")
            this.to_Port = json.getInt("tp")
        }
        return this
    }

    override fun handle() {
        if (ServerType.getType() == ServerType.API) {
            if (APIFunction.instance.isPlayerOnline(player_id) && APIFunction.instance.isServerOnline(server_id)) {
                val player = APIFunction.instance.getPlayer(player_id)!!
                val server = APIFunction.instance.getServer(server_id)!!
                to_IP = server.server_ip
                to_Port = server.server_port
                server.dataPacket(this)
                APIFunction.instance.getServer(source)?.dataPacket(this)
                player.sendData(server_id)
                if (server.isUseCBag) {
                    val pk = SendBagPacket()
                    pk.id = server.cBagID!!
                    pk.name = this.player_id
                    val a = player.bags[pk.id]
                    if (a != null) {
                        pk.data = a
                        server.dataPacket(pk)
                    }
                }
            }
            return
        }
        if (ServerType.getType() != ServerType.LOGIN && server_id == FoundHiPlugin.instance.id) {
            FoundHiPlugin.instance.addWillJoin(player_id)
            FoundHiPlugin.instance.willKick.add(player_id)
            FoundHiPlugin.instance.logger.notice("§b加载FoundHi玩家跨服信息(id:$player_id)")
            return
        }
        if (ServerType.getType() != ServerType.LOGIN) {
            val player = cn.nukkit.Server.getInstance().getPlayerExact(player_id)
            player.transfer(InetSocketAddress(this.to_IP, this.to_Port))
        }
    }
}
