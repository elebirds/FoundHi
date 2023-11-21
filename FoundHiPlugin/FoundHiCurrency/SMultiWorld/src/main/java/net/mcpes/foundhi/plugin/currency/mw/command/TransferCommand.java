package net.mcpes.foundhi.plugin.currency.mw.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import static net.mcpes.foundhi.plugin.currency.mw.SMultiWorld.DEFAULT_TITLE;
import static net.mcpes.foundhi.plugin.currency.mw.SMultiWorld.worlds;

/**
 * @author hhm
 * @date 2017/7/7
 * @since SMultiWorld
 */

public class TransferCommand extends Command {
    public TransferCommand() {
        super("w");
        this.setAliases(new String[]{"w <世界名字>"});
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(DEFAULT_TITLE + "你必须是一个玩家!");
        }
        switch (strings.length) {
            case 1:
                if (!worlds.containsKey(strings[0])) {
                    sender.sendMessage(DEFAULT_TITLE + "此世界不存在");
                    return false;
                }
                ((Player) sender).teleport(Server.getInstance().getLevelByName(strings[0]).getSpawnLocation());
                return true;
            default:
                sender.sendMessage(DEFAULT_TITLE + "输入错误!正确操作为/w <世界名字>");
                break;
        }
        return false;
    }
}
