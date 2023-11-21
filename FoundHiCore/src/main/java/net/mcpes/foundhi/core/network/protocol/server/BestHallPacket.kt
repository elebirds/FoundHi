package net.mcpes.foundhi.core.network.protocol.server

import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.network.protocol.player.PlayerTransferPacket
import net.mcpes.foundhi.core.network.protocol.player.SendBagPacket
import net.mcpes.foundhi.core.plugin.FoundHiPlugin.Companion.SERVER_TITLE
import net.sf.json.JSONObject
import java.net.InetSocketAddress

class BestHallPacket : DataPacket() {
    var hallID: String? = null
    lateinit var pname: String
    lateinit var source: String
    lateinit var hallIP: String
    var hallPort: Int = 0

    override fun pid(): Int {
        return ProtocolInfo.BEST_HALL_PACKET
    }

    override fun encode(): String {
        val json = JSONObject()
        json["pid"] = this.pid()
        json["id"] = hallID
        json["player"] = pname
        json["source"] = source
        if (ServerType.getType() == ServerType.API && hallID != null) {
            val server = APIFunction.instance.getServer(hallID!!)!!
            json["hip"] = server.server_ip
            json["hpt"] = server.server_port
        }
        return json.toString()
    }

    override fun decode(json: JSONObject): DataPacket {
        if (json.containsKey("id"))
            this.hallID = json.getString("id")
        else
            this.hallID = null
        this.pname = json.getString("player")
        this.source = json.getString("source")
        if (ServerType.getType() == ServerType.LOGIN && hallID != null) {
            this.hallIP = json.getString("hip")
            this.hallPort = json.getInt("hpt")
        }
        return this
    }

    override fun handle() {
        if (ServerType.getType() == ServerType.API) {
            val sServer = APIFunction.instance.getServer(source)!!
            if (!APIFunction.instance.isPlayerOnline(pname)) {
                val cp = ServerCommandPacket()
                cp.command = "kick $pname §c服务器人数已满!"
                sServer.dataPacket(cp)
                return
            }
            hallID = APIFunction.instance.bestHall
            if (hallID == null && APIFunction.instance.hasHubs()) hallID = APIFunction.instance.getHub(0)
            if (hallID == null) {
                val cp = ServerCommandPacket()
                cp.command = "kick $pname §c无可用大厅!"
                sServer.dataPacket(cp)
                return
            }
            //TODO 莫名其妙null
            val hServer = APIFunction.instance.getServer(hallID!!)!!
            val pk = PlayerTransferPacket()
            pk.server_id = hallID!!
            pk.player_id = pname
            pk.source = source
            pk.to_IP = hServer.server_ip
            pk.to_Port = hServer.server_port
            val data = APIFunction.instance.getPlayer(pname)!!
            if (hallID != null) {
                data.sendData(hallID!!)
                hServer.dataPacket(pk)
                sServer.dataPacket(this)
                if (hServer.isUseCBag) {
                    val spk = SendBagPacket()
                    spk.id = hServer.cBagID!!
                    spk.name = this.pname
                    val a = data.bags[spk.id]
                    if (a != null) {
                        spk.data = a
                        hServer.dataPacket(spk)
                    }
                }
            }
            return
        }
        if (ServerType.getType() != ServerType.HUB) {
            val player = cn.nukkit.Server.getInstance().getPlayerExact(pname)
            if (hallID == null) {
                player.sendMessage("$SERVER_TITLE 现在没有可用的大厅!")
                player.kick("$SERVER_TITLE 现在没有可用的大厅!")
                return
            }
            player.transfer(InetSocketAddress(this.hallIP, this.hallPort))
        }
    }
}
