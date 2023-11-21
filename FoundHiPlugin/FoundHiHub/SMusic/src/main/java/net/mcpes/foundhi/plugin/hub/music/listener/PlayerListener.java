package net.mcpes.foundhi.plugin.hub.music.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

import static net.mcpes.foundhi.plugin.hub.music.SMusic.songPlayers;

/**
 * @author hhm
 * @date 2017/7/18
 * @since SMusic
 */

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        songPlayers.add(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        songPlayers.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onKick(PlayerKickEvent event) {
        songPlayers.remove(event.getPlayer());
    }
}
