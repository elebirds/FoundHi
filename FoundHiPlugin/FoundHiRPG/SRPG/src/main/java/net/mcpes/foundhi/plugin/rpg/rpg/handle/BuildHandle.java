package net.mcpes.foundhi.plugin.rpg.rpg.handle;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import net.mcpes.foundhi.core.handle.Handle;
import net.mcpes.foundhi.core.handle.HandleResults;
import net.mcpes.foundhi.plugin.rpg.rpg.RPGMain;
import net.mcpes.foundhi.plugin.rpg.rpg.Utils;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.BuildManager;

/**
 * FoundHi
 *
 * @author yui Copyright (c) 2018/7/17
 * version 1.0
 */
public class BuildHandle extends Handle {
    private String target;
    private BuildManager buildManager;

    public BuildHandle(String target, BuildManager buildManager) {
        this.target = target;
        this.buildManager = buildManager;
    }

    @Override
    public void handle() {
        Player player = Server.getInstance().getPlayerExact(target);
        if (player == null || !player.isOnline()) {
            return;
        }
        switch (this.result) {
            case HandleResults.NORMAL_SUCCESS:
                Item item = player.getInventory().getItemInHand();
                int buildLevel = BuildManager.getBuildLevel(item) + 1;
                if (Utils.random(buildManager.getSucceedPR(buildLevel))) {
                    player.getInventory().remove(item);
                    player.getInventory().setItemInHand(BuildManager.addBuildLevel(item));
                    player.sendMessage(RPGMain.PluginName + "§e恭喜你强化成功！");
                } else {
                    if (buildManager.canBack(buildLevel)) {
                        player.getInventory().remove(item);
                        player.getInventory().setItemInHand(BuildManager.backBuildLevel(item));
                        player.sendMessage(RPGMain.PluginName + "很遗憾...强化失败惹，而且强化等级还下降了一级....伤心");
                    } else {
                        player.sendMessage(RPGMain.PluginName + "很遗憾...强化失败惹");
                    }
                }
                break;
            case HandleResults.NOT_ENOUGH_MONEY:
                player.sendMessage(RPGMain.PluginName + "你好像没有足够的钱来强化武器欸...");
                break;
            case HandleResults.ALREADY_BUILD_MAX_LEVEL:
                player.sendMessage(RPGMain.PluginName + "你的武器已经强化到最高等级啦！");
                break;
            case HandleResults.THE_OTHER_OFFLINE:
                player.sendMessage("§a§l操作失败:玩家:" + this.target + "不在线!");
                break;
            case HandleResults.PARAMETER_ERROR:
                player.sendMessage("§a§l操作失败:未知系统错误!");
                break;
        }
    }
}
