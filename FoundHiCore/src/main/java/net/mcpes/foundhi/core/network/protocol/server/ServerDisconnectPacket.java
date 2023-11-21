package net.mcpes.foundhi.core.network.protocol.server;

import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import net.mcpes.foundhi.core.ServerType;
import net.mcpes.foundhi.core.app.APIFunction;
import net.mcpes.foundhi.core.network.protocol.DataPacket;
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo;
import net.mcpes.foundhi.core.plugin.FoundHiPlugin;
import net.sf.json.JSONObject;

import java.util.Objects;

public class ServerDisconnectPacket extends DataPacket {
    public String id;

    @Override
    public byte pid() {
        return ProtocolInfo.SERVER_DISCONNECT_PACKET;
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
        if (ServerType.getType() == ServerType.API) {
            if (APIFunction.getInstance().isServerOnline(this.id)) {
                Objects.requireNonNull(APIFunction.getInstance().getServer(this.id)).shutdown(true);
            }
        } else {
            FoundHiPlugin.getInstance().getNetwork().getPluginListener().getChannel().close();
            FoundHiPlugin.getInstance().getLogger().info("API服务器已经关闭,十秒后尝试重新连接");
            Server.getInstance().getScheduler().scheduleAsyncTask(FoundHiPlugin.getInstance(), new AsyncTask() {
                @Override
                public void onRun() {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    FoundHiPlugin.getInstance().getNetwork().getPluginListener().doConnect();
                }
            });
        }
    }
}
