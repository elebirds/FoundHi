package net.mcpes.foundhi.plugin.rpg.rpg.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;

import static net.mcpes.foundhi.plugin.rpg.rpg.RPGMain.*;

/**
 * @author hhm @ Yui-RPG Project
 **/
public class FragmentCommand extends Command {

    public FragmentCommand(String name) {
        super(name);
        setUsage("/sp ");
        setDescription("碎片总指令");
        setPermission("fragment.command");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (strings.length != 3) {
            sender.sendMessage(PluginName + "§4请正确输入指令 /sp 玩家名字 <碎片ID:精良/稀有/史诗/传说/神迹> 数量");
            return false;
        }
        Player player = Server.getInstance().getPlayerExact(strings[0]);
        if (player == null || (!player.isOnline())) {
            sender.sendMessage(PluginName + "§4玩家不在线");
            return true;
        }
        Item item;
        int count;
        try {
            count = Integer.parseInt(strings[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(PluginName + "§4请正确输入指令 /sp 玩家名字 <碎片ID:精良/稀有/史诗/传说/神迹> 数量");
            return false;
        }
        switch (strings[1]) {
            case "精良":
                item = fragment1.clone();
                break;
            case "稀有":
                item = fragment2.clone();
                break;
            case "史诗":
                item = fragment3.clone();
                break;
            case "传说":
                item = fragment4.clone();
                break;
            case "神迹":
                item = fragment5.clone();
                break;
            default:
                sender.sendMessage(PluginName + "§4请正确输入指令 /sp 玩家名字 <碎片ID:精良/稀有/史诗/传说/神迹> 数量");
                return false;
        }
        item.setCount(count);
        if (!player.getInventory().canAddItem(item)) {
            sender.sendMessage(PluginName + "§4玩家背包已满");
        }
        player.getInventory().addItem(item);
        sender.sendMessage(PluginName + "§4成功给予");
        return true;
    }
}
