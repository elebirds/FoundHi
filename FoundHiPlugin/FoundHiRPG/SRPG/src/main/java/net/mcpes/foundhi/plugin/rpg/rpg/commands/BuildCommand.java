package net.mcpes.foundhi.plugin.rpg.rpg.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import net.mcpes.foundhi.core.handle.HandleResults;
import net.mcpes.foundhi.core.plugin.FoundHiPlugin;
import net.mcpes.foundhi.core.plugin.data.PlayerCache;
import net.mcpes.foundhi.core.utils.MoneyUtils;
import net.mcpes.foundhi.plugin.rpg.rpg.DataManager;
import net.mcpes.foundhi.plugin.rpg.rpg.RPGMain;
import net.mcpes.foundhi.plugin.rpg.rpg.Utils;
import net.mcpes.foundhi.plugin.rpg.rpg.handle.BuildHandle;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.BuildManager;

import java.util.Objects;

/**
 * @author yui
 * @date 2018/2/14
 * @since RPG
 */

public class BuildCommand extends Command {
    public BuildCommand(String name) {
        super(name);
        setUsage("/build ");
        setDescription("强化总指令");
        setPermission("build.command");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(RPGMain.PluginName + "请在游戏里运行此命令");
            return true;
        }
        Player player = (Player) sender;
        Item item = player.getInventory().getItemInHand();
        if (Utils.isArtifact(item)) {
            int buildLevel = BuildManager.getBuildLevel(item) + 1;
            String buildMode = Utils.getItemArtifact(item).getBuildMode();
            BuildManager buildManager = DataManager.INSTANCE.getBuildManager(buildMode);
            BuildHandle buildHandle = new BuildHandle(sender.getName(), buildManager);
            if (buildManager.getMaxBuildLevel() < buildLevel) {
                buildHandle.setResult(HandleResults.ALREADY_BUILD_MAX_LEVEL);
                buildHandle.handle();
                return true;
            }
            int money = buildManager.getMoney(buildLevel);
            PlayerCache player1 = FoundHiPlugin.getInstance().getPlayer(sender.getName());
            if (Objects.requireNonNull(player1).getMoney1() <= money) {
                buildHandle.setResult(HandleResults.NOT_ENOUGH_MONEY);
                buildHandle.handle();
                return true;
            }
            switch (MoneyUtils.delete("System-FoundHiAPI-RPG强化", sender.getName(), money, 1, true, buildHandle)) {
                case HandleResults.NORMAL_SUCCESS:
                    sender.sendMessage(RPGMain.PluginName + "正在处理中...");
                    break;
                case HandleResults.MONEY_TYPE_ERROR:
                    sender.sendMessage(RPGMain.PluginName + "未知系统错误");
                    break;
            }
        } else {
            sender.sendMessage(RPGMain.PluginName + "§4请持有RPG武器，再进行强化 [ /抱脸/ ]");
        }
        return false;
    }
}
