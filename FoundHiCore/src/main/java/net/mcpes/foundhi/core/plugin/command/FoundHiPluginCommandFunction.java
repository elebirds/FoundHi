package net.mcpes.foundhi.core.plugin.command;

import cn.nukkit.command.CommandSender;
import net.mcpes.foundhi.core.app.data.player.Player;
import net.mcpes.foundhi.core.plugin.FoundHiPlugin;
import net.mcpes.foundhi.core.plugin.data.PlayerCache;


public interface FoundHiPluginCommandFunction {
    default boolean checkPull(CommandSender sender) {
        return sender.isPlayer() && (!FoundHiPlugin.getInstance().isPlayerOnline(sender.getName()));
    }

    default boolean checkPermission(Player player, Player.Permission permission) {
        return player.getPermission().getLevel() >= permission.getLevel();
    }

    default boolean checkPermission(PlayerCache player, Player.Permission permission) {
        return player.getPermission().getLevel() >= permission.getLevel();
    }
}
