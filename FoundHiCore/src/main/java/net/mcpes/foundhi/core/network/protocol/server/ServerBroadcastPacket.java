package net.mcpes.foundhi.core.network.protocol.server;

import cn.nukkit.Server;
import net.mcpes.foundhi.core.ServerType;
import net.mcpes.foundhi.core.network.protocol.DataPacket;
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo;
import net.sf.json.JSONObject;

public class ServerBroadcastPacket extends DataPacket {
    public String message;

    @Override
    public byte pid() {
        return ProtocolInfo.SERVER_BROADCAST_PACKET;
    }

    @Override
    public String encode() {
        JSONObject json = new JSONObject();
        json.put("pid", this.pid());
        json.put("message", message);
        return json.toString();
    }

    @Override
    public DataPacket decode(JSONObject json) {
        this.message = json.getString("message");
        return this;
    }

    @Override
    public void handle() {
        if (ServerType.getType() != ServerType.API) Server.getInstance().broadcastMessage(message);
    }
}
