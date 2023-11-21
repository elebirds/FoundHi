package net.mcpes.foundhi.core.network.protocol.guild;

import net.mcpes.foundhi.core.network.protocol.DataPacket;
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo;
import net.mcpes.foundhi.core.plugin.FoundHiPlugin;
import net.sf.json.JSONObject;

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/22
 * version 1.0
 */
public class RemoveGuildPacket extends DataPacket {
    public int id;

    @Override
    public byte pid() {
        return ProtocolInfo.GUILD_REMOVE_PACKET;
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
        this.id = json.getInt("id");
        return this;
    }

    @Override
    public void handle() {
        FoundHiPlugin.getInstance().removeGuild(this.id);
    }
}
