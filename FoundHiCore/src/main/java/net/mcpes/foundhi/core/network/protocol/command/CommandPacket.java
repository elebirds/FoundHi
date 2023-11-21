package net.mcpes.foundhi.core.network.protocol.command;

import net.mcpes.foundhi.core.ServerType;
import net.mcpes.foundhi.core.app.APIFunction;
import net.mcpes.foundhi.core.app.command.PacketCommandSender;
import net.mcpes.foundhi.core.network.protocol.DataPacket;
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo;
import net.mcpes.foundhi.core.network.protocol.handle.RunHandlePacket;
import net.sf.json.JSONObject;

import java.util.Objects;

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/12
 * version 1.0
 */

public class CommandPacket extends DataPacket {
    public boolean event;
    public String eventID;
    public String cmd;
    public String sid;

    @Override
    public byte pid() {
        return ProtocolInfo.COMMAND_EXECUTE_PACKET;
    }

    @Override
    public String encode() {
        JSONObject json = new JSONObject();
        json.put("pid", this.pid());
        json.put("eid", eventID);
        json.put("eb", event);
        json.put("cmd", cmd);
        json.put("s", sid);
        return json.toString();
    }

    @Override
    public DataPacket decode(JSONObject json) {
        this.event = json.getBoolean("eb");
        this.eventID = json.getString("eid");
        this.cmd = json.getString("cmd");
        this.sid = json.getString("s");
        return this;
    }

    @Override
    public void handle() {
        if (ServerType.getType() != ServerType.API) return;
        if (event) {
            PacketCommandSender pcs = new PacketCommandSender();
            boolean flag = APIFunction.getInstance().getCommandMap().dispatch(pcs, cmd);
            if (flag) {
                RunHandlePacket pk = new RunHandlePacket();
                pk.handleID = this.eventID;
                pk.result = pcs.getResult();
                if (APIFunction.getInstance().isServerOnline(sid)) {
                    Objects.requireNonNull(APIFunction.getInstance().getServer(sid)).dataPacket(pk);
                }
            }
        }
    }
}
