package net.mcpes.foundhi.core.network.protocol.player;

import net.mcpes.foundhi.core.ServerType;
import net.mcpes.foundhi.core.app.APIFunction;
import net.mcpes.foundhi.core.network.protocol.DataPacket;
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo;
import net.sf.json.JSONObject;

public class PlayerQuitPacket extends DataPacket {
    public String id;

    @Override
    public byte pid() {
        return ProtocolInfo.PLAYER_QUIT_PACKET;
    }

    @Override
    public String encode() {
        JSONObject json = new JSONObject();
        json.put("pid", this.pid());
        json.put("id", id);
        return json.toString();
    }

    @Override
    public DataPacket decode(JSONObject json) {
        this.id = json.getString("id");
        return this;
    }

    @Override
    public void handle() {
        if (ServerType.getType() != ServerType.API) {
            return;
        }
        APIFunction.getInstance().delPlayer(this.id);
    }
}
