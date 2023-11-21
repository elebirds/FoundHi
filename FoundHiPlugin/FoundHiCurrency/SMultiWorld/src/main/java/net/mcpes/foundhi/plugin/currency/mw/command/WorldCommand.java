package net.mcpes.foundhi.plugin.currency.mw.command;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;
import net.mcpes.foundhi.plugin.currency.mw.utils.World;

import static net.mcpes.foundhi.plugin.currency.mw.SMultiWorld.*;

/**
 * @author hhm
 * @date 2017/7/8
 * @since SMultiWorld
 */

public class WorldCommand extends Command {
    public WorldCommand() {
        super("world");
        this.setAliases(new String[]{
                "world list",
                "world about",
                "world info",
                "world loadAll <世界名字>",
                "world unload <世界名字>",
                "world del <世界名字>",
                "world add <世界名字>",
                "world add <世界名字> <normal/flat/nether/void>",
                "world add <世界名字> <normal/flat/nether/void> <种子>"
        });
        this.setPermission("sworld.command.admin");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (strings.length == 0) {
            sender.sendMessage(DEFAULT_TITLE + "输入错误!请你输入/world help 查看帮助");
            return false;
        }
        switch (strings[0]) {
            case "help":
                sender.sendMessage("§6§l===== " + DEFAULT_TITLE + " 指令帮助 =====");
                sender.sendMessage("§2  - /world list 查看所有世界");
                sender.sendMessage("§2  - /world info <世界名字> 查看世界相关信息");
                sender.sendMessage("§2  - /world about 关于本插件");
                sender.sendMessage("§2  - /world del <世界名字> 删除世界");
                sender.sendMessage("§2  - /world loadAll <世界名字> 加载世界");
                sender.sendMessage("§2  - /world unload <世界名字> 卸载世界");
                sender.sendMessage("§2  - /world add <世界名字> 新建世界");
                sender.sendMessage("§2  - /world add <世界名字> <normal/flat/nether/void> 新建世界");
                sender.sendMessage("§2  - /world add <世界名字> <normal/flat/nether/void> <种子> 新建世界");
                break;
            case "info":
                if (strings.length != 2) {
                    sender.sendMessage(DEFAULT_TITLE + "输入错误!请你输入/world help 查看帮助");
                    return false;
                }
                if (!worlds.containsKey(strings[1])) {
                    sender.sendMessage(DEFAULT_TITLE + "此世界没有加载!");
                    return false;
                }
                sender.sendMessage("§6§l===== " + DEFAULT_TITLE + strings[1] + " 世界信息 =====");
                World world = worlds.get(strings[1]);
                Level level = Server.getInstance().getLevelByName(strings[1]);
                sender.sendMessage("§6§l玩家数量: " + level.getPlayers().size());
                sender.sendMessage("§6§l区块加载数量: " + level.getChunks().size());
                sender.sendMessage("§6§lPVP开启: " + world.isPvp());
                sender.sendMessage("§6§lPVE开启: " + world.isPve());
                sender.sendMessage("§6§l农作物生长开启: " + world.isGrow());
                sender.sendMessage("§6§l死亡掉落: " + world.isDrop());
                sender.sendMessage("§6§lTNT爆炸破坏方块: " + world.isTnt_boom());
                sender.sendMessage("§6§l苦力怕爆炸破坏方块: " + world.isCreeper_boom());
                sender.sendMessage("§6§l岩浆流动: " + world.isLava_flow());
                sender.sendMessage("§6§l水流动: " + world.isWater_flow());
                sender.sendMessage("§6§l最大玩家数量: " + world.getMaxPlayer());
                String gameMode = "";
                for (Integer mode : world.getGameMode()) {
                    switch (mode) {
                        case 0:
                            gameMode += "生存,";
                            break;
                        case 1:
                            gameMode += "创造,";
                            break;
                        case 2:
                            gameMode += "冒险,";
                            break;
                        case 3:
                            gameMode += "旁观,";
                            break;
                    }
                }
                sender.sendMessage("§6§l可进入游戏模式: " + gameMode.substring(0, gameMode.length() - 1));
                sender.sendMessage("§6§l进入花费: " + world.getMoney());
                break;
            case "about":
                sender.sendMessage(DEFAULT_TITLE + "By hhm,开源");
                break;
            case "add":
                switch (strings.length) {
                    case 2:
                        switch (getApi().newWorld(strings[1])) {
                            case 1:
                                sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "成功,现已自动加载");
                                break;
                            case 2:
                                sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "成功,加载失败");
                                break;
                            case 3:
                                sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "失败,未知原因");
                                break;
                            case 4:
                                sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "失败,此世界原本没有加载,现已自动加载");
                                break;
                            case 5:
                                sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "失败,此世界已经存在");
                                break;
                        }
                        break;
                    case 3:
                        switch (strings[2]) {
                            case "normal":
                                switch (getApi().newWorld(strings[1], 1, "normal")) {
                                    case 1:
                                        sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "成功,现已自动加载");
                                        break;
                                    case 2:
                                        sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "成功,加载失败");
                                        break;
                                    case 3:
                                        sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "失败,未知原因");
                                        break;
                                    case 4:
                                        sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "失败,此世界原本没有加载,现已自动加载");
                                        break;
                                    case 5:
                                        sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "失败,此世界已经存在");
                                        break;
                                }
                                break;
                            case "flat":
                                switch (getApi().newWorld(strings[1], 1, "flat")) {
                                    case 1:
                                        sender.sendMessage(DEFAULT_TITLE + "新建超平坦世界" + strings[1] + "成功,现已自动加载");
                                        break;
                                    case 2:
                                        sender.sendMessage(DEFAULT_TITLE + "新建超平坦世界" + strings[1] + "成功,加载失败");
                                        break;
                                    case 3:
                                        sender.sendMessage(DEFAULT_TITLE + "新建超平坦世界" + strings[1] + "失败,未知原因");
                                        break;
                                    case 4:
                                        sender.sendMessage(DEFAULT_TITLE + "新建超平坦世界" + strings[1] + "失败,此世界原本没有加载,现已自动加载");
                                        break;
                                    case 5:
                                        sender.sendMessage(DEFAULT_TITLE + "新建超平坦世界" + strings[1] + "失败,此世界已经存在");
                                        break;
                                }
                                break;
                            case "nether":
                                switch (getApi().newWorld(strings[1], 1, "nether")) {
                                    case 1:
                                        sender.sendMessage(DEFAULT_TITLE + "新建地狱世界" + strings[1] + "成功,现已自动加载");
                                        break;
                                    case 2:
                                        sender.sendMessage(DEFAULT_TITLE + "新建地狱世界" + strings[1] + "成功,加载失败");
                                        break;
                                    case 3:
                                        sender.sendMessage(DEFAULT_TITLE + "新建地狱世界" + strings[1] + "失败,未知原因");
                                        break;
                                    case 4:
                                        sender.sendMessage(DEFAULT_TITLE + "新建地狱世界" + strings[1] + "失败,此世界原本没有加载,现已自动加载");
                                        break;
                                    case 5:
                                        sender.sendMessage(DEFAULT_TITLE + "新建地狱世界" + strings[1] + "失败,此世界已经存在");
                                        break;
                                }
                                break;
                            case "void":
                                switch (getApi().newWorld(strings[1], 1, "void")) {
                                    case 1:
                                        sender.sendMessage(DEFAULT_TITLE + "新建虚空世界" + strings[1] + "成功,现已自动加载");
                                        break;
                                    case 2:
                                        sender.sendMessage(DEFAULT_TITLE + "新建虚空世界" + strings[1] + "成功,加载失败");
                                        break;
                                    case 3:
                                        sender.sendMessage(DEFAULT_TITLE + "新建虚空世界" + strings[1] + "失败,未知原因");
                                        break;
                                    case 4:
                                        sender.sendMessage(DEFAULT_TITLE + "新建虚空世界" + strings[1] + "失败,此世界原本没有加载,现已自动加载");
                                        break;
                                    case 5:
                                        sender.sendMessage(DEFAULT_TITLE + "新建虚空世界" + strings[1] + "失败,此世界已经存在");
                                        break;
                                }
                                break;
                            default:
                                sender.sendMessage(DEFAULT_TITLE + "输入错误!请你输入/world help 查看帮助");
                                return false;
                        }
                        break;
                    case 4:
                        long seed;
                        try {
                            seed = Long.valueOf(strings[3]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(DEFAULT_TITLE + "你输入的种子不是一个数字!");
                            return false;
                        }
                        switch (strings[2]) {
                            case "normal":
                                switch (getApi().newWorld(strings[1], seed, "normal")) {
                                    case 1:
                                        sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "成功(种子:" + seed + "),现已自动加载");
                                        break;
                                    case 2:
                                        sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "成功(种子:" + seed + "),加载失败");
                                        break;
                                    case 3:
                                        sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "失败(种子:" + seed + "),未知原因");
                                        break;
                                    case 4:
                                        sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "失败(种子:" + seed + "),此世界原本没有加载,现已自动加载");
                                        break;
                                    case 5:
                                        sender.sendMessage(DEFAULT_TITLE + "新建普通世界" + strings[1] + "失败(种子:" + seed + "),此世界已经存在");
                                        break;
                                }
                                break;
                            case "flat":
                                switch (getApi().newWorld(strings[1], seed, "flat")) {
                                    case 1:
                                        sender.sendMessage(DEFAULT_TITLE + "新建超平坦世界" + strings[1] + "成功(种子:" + seed + "),现已自动加载");
                                        break;
                                    case 2:
                                        sender.sendMessage(DEFAULT_TITLE + "新建超平坦世界" + strings[1] + "成功(种子:" + seed + "),加载失败");
                                        break;
                                    case 3:
                                        sender.sendMessage(DEFAULT_TITLE + "新建超平坦世界" + strings[1] + "失败(种子:" + seed + "),未知原因");
                                        break;
                                    case 4:
                                        sender.sendMessage(DEFAULT_TITLE + "新建超平坦世界" + strings[1] + "失败(种子:" + seed + "),此世界原本没有加载,现已自动加载");
                                        break;
                                    case 5:
                                        sender.sendMessage(DEFAULT_TITLE + "新建超平坦世界" + strings[1] + "失败(种子:" + seed + "),此世界已经存在");
                                        break;
                                }
                                break;
                            case "nether":
                                switch (getApi().newWorld(strings[1], seed, "nether")) {
                                    case 1:
                                        sender.sendMessage(DEFAULT_TITLE + "新建地狱世界" + strings[1] + "成功(种子:" + seed + "),现已自动加载");
                                        break;
                                    case 2:
                                        sender.sendMessage(DEFAULT_TITLE + "新建地狱世界" + strings[1] + "成功(种子:" + seed + "),加载失败");
                                        break;
                                    case 3:
                                        sender.sendMessage(DEFAULT_TITLE + "新建地狱世界" + strings[1] + "失败(种子:" + seed + "),未知原因");
                                        break;
                                    case 4:
                                        sender.sendMessage(DEFAULT_TITLE + "新建地狱世界" + strings[1] + "失败(种子:" + seed + "),此世界原本没有加载,现已自动加载");
                                        break;
                                    case 5:
                                        sender.sendMessage(DEFAULT_TITLE + "新建地狱世界" + strings[1] + "失败(种子:" + seed + "),此世界已经存在");
                                        break;
                                }
                                break;
                            case "void":
                                switch (getApi().newWorld(strings[1], 1, "void")) {
                                    case 1:
                                        sender.sendMessage(DEFAULT_TITLE + "新建虚空世界" + strings[1] + "成功,现已自动加载");
                                        break;
                                    case 2:
                                        sender.sendMessage(DEFAULT_TITLE + "新建虚空世界" + strings[1] + "成功,加载失败");
                                        break;
                                    case 3:
                                        sender.sendMessage(DEFAULT_TITLE + "新建虚空世界" + strings[1] + "失败,未知原因");
                                        break;
                                    case 4:
                                        sender.sendMessage(DEFAULT_TITLE + "新建虚空世界" + strings[1] + "失败,此世界原本没有加载,现已自动加载");
                                        break;
                                    case 5:
                                        sender.sendMessage(DEFAULT_TITLE + "新建虚空世界" + strings[1] + "失败,此世界已经存在");
                                        break;
                                }
                                break;
                            default:
                                sender.sendMessage(DEFAULT_TITLE + "输入错误!请你输入/world help 查看帮助");
                                return false;
                        }
                        break;
                    default:
                        sender.sendMessage(DEFAULT_TITLE + "输入错误!请你输入/world help 查看帮助");
                        break;
                }
                break;
            case "del":
                if (strings.length != 2) {
                    sender.sendMessage(DEFAULT_TITLE + "输入错误!请你输入/world help 查看帮助");
                    return false;
                }
                switch (getApi().removeWorld(strings[1])) {
                    case 1:
                        sender.sendMessage(DEFAULT_TITLE + "删除世界" + strings[1] + "成功");
                        break;
                    case 2:
                        sender.sendMessage(DEFAULT_TITLE + "卸载世界" + strings[1] + "失败,无法继续进行");
                        break;
                    case 3:
                        sender.sendMessage(DEFAULT_TITLE + "世界" + strings[1] + "不存在");
                        break;
                }
                break;
            case "list":
                sender.sendMessage("§6§l===== " + DEFAULT_TITLE + "世界列表 =====");
                getApi().getWorlds().forEach((a) -> sender.sendMessage("§2   - " + a));
                break;
            case "loadAll":
                if (strings.length != 2) {
                    sender.sendMessage(DEFAULT_TITLE + "输入错误!请你输入/world help 查看帮助");
                    return false;
                }
                if (getApi().loadWorld(strings[1])) {
                    sender.sendMessage(DEFAULT_TITLE + "加载世界" + strings[1] + "成功");
                } else {
                    sender.sendMessage(DEFAULT_TITLE + "加载世界" + strings[1] + "失败");
                }
                break;
            case "unload":
                if (strings.length != 2) {
                    sender.sendMessage(DEFAULT_TITLE + "输入错误!请你输入/world help 查看帮助");
                    return false;
                }
                switch (getApi().unloadWorld(strings[1])) {
                    case 1:
                        sender.sendMessage(DEFAULT_TITLE + "卸载世界" + strings[1] + "成功");
                        break;
                    case 2:
                        sender.sendMessage(DEFAULT_TITLE + "发现世界" + strings[1] + ",但卸载失败");
                        break;
                    case 3:
                        sender.sendMessage(DEFAULT_TITLE + "没有发现世界" + strings[1] + "");
                        break;
                }
                break;
        }
        return false;
    }
}
