package net.mcpes.foundhi.core.plugin.event;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;

public class PlayerRegisterEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    public PlayerRegisterEvent(Player player) {
        this.player = player;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }
}
