package net.mcpes.foundhi.core.network.protocol.player;

import cn.nukkit.Player;
import cn.nukkit.Server;
import net.mcpes.foundhi.core.ServerType;
import net.mcpes.foundhi.core.app.APIFunction;
import net.mcpes.foundhi.core.network.protocol.DataPacket;
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo;
import net.sf.json.JSONObject;

import java.util.Objects;

public class PlayerSendMessagePacket extends DataPacket {
    public String id;
    public String message;

    @Override
    public byte pid() {
        return ProtocolInfo.PLAYER_SEND_MESSAGE_PACKET;
    }

    @Override
    public String encode() {
        JSONObject json = new JSONObject();
        json.put("pid", this.pid());
        json.put("id", id);
        json.put("msg", message);
        return json.toString();
    }

    @Override
    public DataPacket decode(JSONObject json) {
        this.id = json.getString("id");
        this.message = json.getString("msg");
        return this;
    }

    @Override
    public void handle() {
        if (ServerType.getType() == ServerType.API) {
            if (APIFunction.getInstance().isPlayerOnline(id)) {
                net.mcpes.foundhi.core.app.data.player.Player player = APIFunction.getInstance().getPlayer(id);
                if (player == null || !APIFunction.getInstance().isServerOnline(player.getInServer())) return;
                Objects.requireNonNull(APIFunction.getInstance().getServer(player.getInServer())).dataPacket(this);
            }
            return;
        }
        Player player = Server.getInstance().getPlayerExact(id);
        if (player != null && player.isOnline()) {
            player.sendMessage(message);
        }
    }
}
