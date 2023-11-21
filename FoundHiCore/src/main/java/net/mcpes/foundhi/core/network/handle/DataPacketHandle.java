package net.mcpes.foundhi.core.network.handle;

import io.netty.channel.Channel;
import net.mcpes.foundhi.core.app.APIFunction;
import net.mcpes.foundhi.core.network.protocol.DataPacket;
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo;
import net.mcpes.foundhi.core.network.protocol.server.ServerConnectPacket;

/**
 * @author hhm @ SGameCore Project
 **/
public class DataPacketHandle {
    public static void handleDataPacket(Channel channel, DataPacket pk) {
        try {
            switch (pk.pid()) {
                case ProtocolInfo.SERVER_CONNECT_PACKET:
                    ServerConnectPacket serverConnectPacket = (ServerConnectPacket) pk;
                    serverConnectPacket.channel = channel;
                    pk = serverConnectPacket;
                    break;
            }
            pk.handle();
        } catch (Exception e) {
            APIFunction.getInstance().getLogger().critical("在处理数据包时发生错误!", e);
        }
    }
}
