package net.mcpes.foundhi.plugin.rpg.rpg.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import net.mcpes.foundhi.plugin.rpg.rpg.DataManager;
import net.mcpes.foundhi.plugin.rpg.rpg.RPGMain;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.ArmorsManager;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.ArmsManager;

/**
 * @author yui
 * @date 2017/10/13
 * @since RPG
 */

public class RPGCommands extends Command {
    private DataManager dataManager = RPGMain.getDataManager();

    public RPGCommands(String name) {
        super(name);
        setUsage("/weapon ");
        setDescription("RPG武器总指令");
        setPermission("rpg.command");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (strings.length == 0) {
            sender.sendMessage(RPGMain.PluginName + "§4格式错误！/weapon give <玩家名字> 武器/盔甲 <id> <数量>");
            return false;
        }
        switch (strings[0]) {
            case "give":
                if (strings.length >= 4) {
                    Player player = Server.getInstance().getPlayerExact(strings[1]);
                    if (player == null) {
                        sender.sendMessage(RPGMain.PluginName + "§4抱歉，对方不在线");
                        return true;
                    }
                    if (player.getInventory().isFull()) {
                        sender.sendMessage(RPGMain.PluginName + "§4对方背包已满，请通知对方清理背包后再行给予武器");
                        return true;
                    }
                    switch (strings[2]) {
                        case "武器":
                            String id = strings[3];
                            if (dataManager.existsArms(id)) {
                                ArmsManager arms = dataManager.getArmsManager(id);
                                Item item = arms.getItem();
                                if (strings.length == 5) {
                                    item.setCount(Integer.valueOf(strings[4]));
                                } else {
                                    item.setCount(1);
                                }
                                player.getInventory().addItem(item);
                                player.sendMessage(RPGMain.PluginName + "管理员给予了你一把神器，请查收");
                                sender.sendMessage(RPGMain.PluginName + "成功给予武器");
                            } else {
                                sender.sendMessage(RPGMain.PluginName + "§4请核对武器id，查找不到此id的武器");
                            }
                            break;
                        case "盔甲":
                            String armorId = strings[3];
                            if (dataManager.existsArmors(armorId)) {
                                ArmorsManager armors = dataManager.getArmorsManager(armorId);
                                Item item = armors.getItem();
                                if (strings.length == 5) {
                                    item.setCount(Integer.valueOf(strings[4]));
                                } else {
                                    item.setCount(1);
                                }
                                player.getInventory().addItem(item);
                                player.sendMessage(RPGMain.PluginName + "管理员给予了你一件神器盔甲，请查收");
                                sender.sendMessage(RPGMain.PluginName + "成功给予盔甲");
                            }
                            break;
                        default:
                            sender.sendMessage(RPGMain.PluginName + "§4请输入完整指令 /weapon give <玩家名字> 武器/盔甲 <id> <数量>");
                            break;
                    }
                } else {
                    sender.sendMessage(RPGMain.PluginName + "§4请输入完整指令 /weapon give <玩家名字> 武器/盔甲 <id> <数量>");
                }
                break;
        }
        return false;
    }
}
