package net.mcpes.foundhi.plugin.rpg.rpg.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import net.mcpes.foundhi.plugin.rpg.rpg.RPGMain;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.PlayerManager;

/**
 * @author yui
 * @date 2018/2/11
 * @since RPG
 */

public class RoleCommand extends Command {
    public RoleCommand(String name) {
        super(name);
        setUsage("/role ");
        setDescription("职业总指令");
        setPermission("role.command");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(RPGMain.PluginName + "请在游戏里运行此命令");
            return true;
        }
        if (strings.length >= 2) {
            String pn = sender.getName().toLowerCase();
            PlayerManager pm = RPGMain.getDataManager().getPlayerManager(pn);
            switch (strings[0]) {
                case "add":
                    if (pm.haveRole()) {
                        sender.sendMessage(RPGMain.PluginName + "§4你目前已有职业");
                        return true;
                    }
                    if (!RPGMain.getDataManager().existsRole(strings[1])) {
                        sender.sendMessage(RPGMain.PluginName + "§4不存在此ID的职业");
                        return true;
                    }
                    pm.setRoleID(strings[1]);
                    pm.saveAll();
                    sender.sendMessage(RPGMain.PluginName + "§e恭喜你成功加入职业" + pm.getRolesManager().getRoleName());
                    break;
                case "change":
                    if (!pm.haveRole()) {
                        sender.sendMessage(RPGMain.PluginName + "§4你目前还未有职业");
                        return true;
                    }
                    if (!RPGMain.getDataManager().existsRole(strings[1])) {
                        sender.sendMessage(RPGMain.PluginName + "§4不存在此ID的职业");
                        return true;
                    }
                    pm.setRoleID(strings[1]);
                    pm.saveAll();
                    //TODO money
                    //Money.getInstance().reduceMoney(sender.getName(), RPGMain.getDataManager().getRoleChangeMoney());
                    sender.sendMessage(RPGMain.PluginName + "§e恭喜你转职为" + pm.getRolesManager().getRoleName());
            }
        }
        return false;
    }
}
