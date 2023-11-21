package net.mcpes.foundhi.core.plugin.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;

public class PlayerLoginEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    public PlayerLoginEvent(Player player) {
        this.player = player;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }
}
