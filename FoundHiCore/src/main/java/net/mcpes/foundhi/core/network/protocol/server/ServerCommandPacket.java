package net.mcpes.foundhi.core.network.protocol.server;

import net.mcpes.foundhi.core.ServerType;
import net.mcpes.foundhi.core.network.protocol.DataPacket;
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo;
import net.mcpes.foundhi.core.plugin.task.ServerCommandTask;
import net.sf.json.JSONObject;
import org.jetbrains.annotations.NotNull;

public class ServerCommandPacket extends DataPacket {
    public String command = "";

    @Override
    public byte pid() {
        return ProtocolInfo.SERVER_COMMAND_PACKET;
    }

    @NotNull
    @Override
    public String encode() {
        JSONObject json = new JSONObject();
        json.put("pid", this.pid());
        json.put("command", command);
        return json.toString();
    }

    @NotNull
    @Override
    public DataPacket decode(@NotNull JSONObject json) {
        this.command = json.getString("command");
        return this;
    }

    @Override
    public void handle() {
        if (ServerType.getType() == ServerType.API) {
            return;
        }
        ServerCommandTask.getCommands().add(this.command);
    }
}
