package net.mcpes.foundhi.core.plugin.listener.login;

import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.scheduler.AsyncTask;
import net.mcpes.foundhi.core.network.protocol.player.PlayerJoinPacket;
import net.mcpes.foundhi.core.network.protocol.server.BestHallPacket;
import net.mcpes.foundhi.core.plugin.FoundHiPlugin;
import net.mcpes.foundhi.core.plugin.event.PlayerLoginEvent;

public class LoginServerListener implements Listener {
    @EventHandler
    public void onJoin(PlayerPreLoginEvent event) {
        PlayerJoinPacket pk = new PlayerJoinPacket();
        pk.id = event.getPlayer().getName();
        FoundHiPlugin.getInstance().dataPacket(pk);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Server.getInstance().getScheduler().scheduleAsyncTask(FoundHiPlugin.getInstance(), new AsyncTask() {
            @Override
            public void onRun() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BestHallPacket pk = new BestHallPacket();
                pk.source = FoundHiPlugin.getInstance().getId();
                pk.pname = event.getPlayer().getName();
                FoundHiPlugin.getInstance().dataPacket(pk);
            }
        });
    }
}
