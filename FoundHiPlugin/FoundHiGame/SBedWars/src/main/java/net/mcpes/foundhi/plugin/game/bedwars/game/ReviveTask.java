package net.mcpes.foundhi.plugin.game.bedwars.game;

import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;
import net.mcpes.foundhi.plugin.game.bedwars.SBedWars;

import static net.mcpes.foundhi.plugin.game.bedwars.SBedWars.games;
import static net.mcpes.foundhi.plugin.game.bedwars.SBedWars.revive;

/**
 * @author hhm @ SBedWars Project
 **/
public class ReviveTask extends PluginTask<SBedWars> {
    public ReviveTask() {
        super(SBedWars.getInstance());
    }

    @Override
    public void onRun(int i) {
        revive.forEach((Player player, Integer time) -> {
            int room = SBedWars.getInstance().isPlayerGaming(player.getName());
            if (room == -1) {
                revive.remove(player);
            } else {
                player.sendTip("§c§l你死了\n§7你将在 " + time + " 秒后重生");
                if (time <= 0) {
                    games.get(room).onRevive(player);
                    revive.remove(player);
                } else {
                    time--;
                    revive.put(player, time);
                }
            }
        });
    }
}
