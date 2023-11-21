package net.mcpes.foundhi.core.network.protocol.server;

import net.mcpes.foundhi.core.network.protocol.DataPacket;
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo;
import net.mcpes.foundhi.core.plugin.FoundHiPlugin;
import net.sf.json.JSONObject;

import java.util.LinkedHashMap;


public class SendTopPacket extends DataPacket {
    public String data;
    public int type;

    @Override
    public byte pid() {
        return ProtocolInfo.SEND_TOP_PACKET;
    }

    @Override
    public DataPacket decode(JSONObject json) {
        this.data = json.getString("d");
        this.type = json.getInt("t");
        return this;
    }

    @Override
    public String encode() {
        JSONObject json = new JSONObject();
        json.put("pid", pid());
        json.put("t", type);
        json.put("d", data);
        return json.toString();
    }

    @Override
    public void handle() {
        LinkedHashMap<String, Double> data = new LinkedHashMap<>();
        JSONObject.fromObject(this.data).forEach((key, value) -> data.put(key.toString(), Double.parseDouble(value.toString())));
        switch (type) {
            case 1:
                FoundHiPlugin.getInstance().setMoney1Top(data);
                break;
            case 2:
                FoundHiPlugin.getInstance().setMoney2Top(data);
                break;
            case 3:
                FoundHiPlugin.getInstance().setMoney3Top(data);
                break;
        }
    }
}
