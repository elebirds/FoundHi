package net.mcpes.foundhi.plugin.hub.music.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.network.protocol.DataPacket;
import net.mcpes.foundhi.plugin.hub.music.utils.FloatingTextPacket;

import java.util.HashMap;

/**
 * @author hhm @ SBedWars Project
 **/
public class FloatingListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        new Thread(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (HashMap<String, Object> data : FloatingTextPacket.datas.values()) {
                DataPacket pk = (DataPacket) data.get("packet");
                event.getPlayer().dataPacket(pk);
            }
        }
        ).start();
    }
}
