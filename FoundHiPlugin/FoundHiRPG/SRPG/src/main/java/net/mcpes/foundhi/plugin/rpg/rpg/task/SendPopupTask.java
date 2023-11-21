package net.mcpes.foundhi.plugin.rpg.rpg.task;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.scheduler.PluginTask;
import net.mcpes.foundhi.core.app.APIFunction;
import net.mcpes.foundhi.plugin.rpg.rpg.DataManager;
import net.mcpes.foundhi.plugin.rpg.rpg.RPGMain;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.PlayerManager;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * @author yui
 * @date 2018/2/11
 * @since RPG
 */

public class SendPopupTask extends PluginTask<RPGMain> {
    public SendPopupTask(RPGMain owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        HashMap<UUID, Player> set = (HashMap<UUID, Player>) owner.getServer().getOnlinePlayers();
        for (UUID key : set.keySet()) {
            String name = owner.getServer().getOnlinePlayers().get(key).getName().toLowerCase();
            if (RPGMain.getDataManager().existsPlayer(name)) {
                PlayerManager playerManager = RPGMain.getDataManager().getPlayerManager(name);
                if (playerManager.haveRole()) {
                    Player player = owner.getServer().getPlayerExact(name);
                    Item handItem = player.getInventory().getItemInHand();
                    if (!handItem.getCustomName().equals("§a手持显示个人信息")) {
                        net.mcpes.foundhi.core.app.data.player.Player player1 = APIFunction.getInstance().getPlayer(player.getName());
                        float money1 = (float) Objects.requireNonNull(player1).getMoney1();
                        float money2 = (float) player1.getMoney2();
                        float money3 = (float) player1.getMoney3();
                        String job = playerManager.getRolesManager().getRoleName();
                        int level = playerManager.getLevel();
                        int exp = playerManager.getExp();
                        int maxExp = playerManager.getLevelManager().getExp();
                        String tip = DataManager.INSTANCE.getTip(money1, money2, money3, job, level, exp, maxExp);
                        player.sendTip(tip);
                    }
                }
            }
        }
    }
}
