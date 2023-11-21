package net.mcpes.foundhi.plugin.rpg.rpg.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import net.mcpes.foundhi.plugin.rpg.rpg.DataManager;
import net.mcpes.foundhi.plugin.rpg.rpg.RPGMain;
import net.mcpes.foundhi.plugin.rpg.rpg.Utils;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.Artifact;

/**
 * @author yui
 * @date 2017/10/14
 * @since RPG
 */

public class TradingCommand extends Command {

    public TradingCommand(String name) {
        super(name);
        setUsage("/dh ");
        setDescription("兑换总指令");
        setPermission("trading.command");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(RPGMain.PluginName + "请在游戏里运行此命令");
            return true;
        }
        if (strings.length >= 1) {
            String id = strings[0];
            Player player = (Player) sender;
            DataManager manager = RPGMain.getDataManager();
            if (manager.existsArms(id) || manager.existsArmors(id)) {
                Artifact artifact = manager.getArtifact(id);
                int quality = artifact.getQuality() + 1;
                int needPieces = artifact.getNeedPieces();
                PlayerInventory inventory = player.getInventory();
                if (Utils.hasEnoughItem(player, RPGMain.getDataManager().getPiece(quality).getId(), RPGMain.getDataManager().getPiece(quality).getDamage(), needPieces, quality)) {
                    if (inventory.canAddItem(artifact.getItem())) {
                        Utils.removeItem(player, RPGMain.getDataManager().getPiece(quality).getId(), RPGMain.getDataManager().getPiece(quality).getDamage(), needPieces, quality);
                        inventory.addItem(artifact.getItem());
                        player.sendMessage(RPGMain.PluginName + "§e恭喜你成功兑换神器：" + artifact.getItem().getCustomName());
                    } else {
                        player.sendMessage(RPGMain.PluginName + "§4无法在你的背包里添加此神器，请腾出背包格子再尝试！");
                    }
                } else {
                    player.sendMessage(RPGMain.PluginName + "§4你没有足够的碎片来兑换此神器！");
                }
            } else {
                sender.sendMessage(RPGMain.PluginName + "§4不存在此ID的武器！");
            }
        } else {
            sender.sendMessage(RPGMain.PluginName + "§4请正确输入指令 /dh <武器ID>");
        }
        return false;
    }
}
