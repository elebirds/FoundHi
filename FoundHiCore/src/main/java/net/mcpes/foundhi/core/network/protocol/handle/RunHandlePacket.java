package net.mcpes.foundhi.core.network.protocol.handle;

import net.mcpes.foundhi.core.handle.Handle;
import net.mcpes.foundhi.core.network.protocol.DataPacket;
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo;
import net.mcpes.foundhi.core.plugin.FoundHiPlugin;
import net.sf.json.JSONObject;
import org.jetbrains.annotations.NotNull;

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/14
 * version 1.0
 */
public class RunHandlePacket extends DataPacket {
    public String handleID;
    public int result;

    @Override
    public byte pid() {
        return ProtocolInfo.HANDLE_RUN_PACKET;
    }

    @NotNull
    @Override
    public String encode() {
        JSONObject json = new JSONObject();
        json.put("pid", this.pid());
        json.put("hid", handleID);
        json.put("h", result);
        return json.toString();
    }

    @NotNull
    @Override
    public DataPacket decode(@NotNull JSONObject json) {
        this.handleID = json.getString("hid");
        this.result = json.getInt("h");

        return this;
    }

    @Override
    public void handle() {
        long hid = Long.parseLong(this.handleID.replace(FoundHiPlugin.getInstance().getId(), ""));
        Handle handle = FoundHiPlugin.getInstance().getHandle(hid);
        if (handle != null) {
            handle.handle(this.result);
            FoundHiPlugin.getInstance().removeHandle(hid);
        }
    }
}
