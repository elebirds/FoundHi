package net.mcpes.foundhi.plugin.game.bedwars.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import net.mcpes.foundhi.plugin.game.bedwars.SBedWars;
import net.mcpes.foundhi.plugin.game.bedwars.SBedWarsAPI;
import net.mcpes.foundhi.plugin.game.bedwars.config.RoomConfig;
import net.mcpes.foundhi.plugin.game.bedwars.data.RoomData;
import net.mcpes.foundhi.plugin.game.bedwars.game.BedWars;
import net.mcpes.foundhi.plugin.game.bedwars.gui.GUIShopBase;
import net.mcpes.foundhi.plugin.game.bedwars.utils.FileFunction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static net.mcpes.foundhi.plugin.game.bedwars.SBedWars.*;

/**
 * @author hhm
 * @date 2017/7/13
 * @since SBedWars
 */

public class BedWarsCommand extends Command {
    public BedWarsCommand() {
        super("bw");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(SBedWars.DEFAULT_TITLE + "你不是一个玩家!");
            return false;
        }
        Player player = (Player) sender;
        if (strings.length == 0) {
            sender.sendMessage(SBedWars.DEFAULT_TITLE + "输入错误,请输入/bw help 查看帮助");
            return false;
        }
        int id;
        switch (strings[0]) {
            case "dataSign":
                if (!player.getName().equals(master.realOwner)) {
                    sender.sendMessage(SBedWars.DEFAULT_TITLE + "你不是此服务器最高管理者(RealOwner)!");
                    return false;
                }
                dataSignSetting.add(sender.getName());
                sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功进入设置数据查看木牌状态,请点击一个木牌");
                break;
            case "my":
                sender.sendMessage("§l§5======§l§e[§6S§eB§de§3d§6W§aa§4r§5s§e] §bMy §cINFO§5======\n" + "§l§c数据:\n  §l§d胜利场数:§6" + players.get(player.getName()).getWin() + "\n  §l§d失败场数:§6" + players.get(player.getName()).getLose() + "\n  §l§d平局场数:§6" + players.get(player.getName()).getDraw() + "\n  §l§d击杀:§6" + players.get(player.getName()).getKill() + "\n  §l§d死亡:§6" + players.get(player.getName()).getDeath() + "\n  §l§dKD:§6" + ((players.get(player.getName()).getKill()) / ((double) players.get(player.getName()).getDeath())) + "\n  §l§d拆床:§6" + players.get(player.getName()).getCore() + "\n");
                break;
            case "ranking":
                if (strings.length != 2) {
                    sender.sendMessage(SBedWars.DEFAULT_TITLE + "输入错误,请输入/bw help 查看帮助");
                    return false;
                }
                switch (strings[1]) {
                    case "kill":
                        sender.sendMessage(SBedWars.DEFAULT_TITLE + "设置成功,请等待排行榜20秒的自动刷新");
                        master.killRanking = player.getFloorX() + ":" + player.getFloorY() + ":" + player.getFloorZ() + ":" + player.getLevel().getName();
                        break;
                    case "kd":
                        sender.sendMessage(SBedWars.DEFAULT_TITLE + "设置成功,请等待排行榜20秒的自动刷新");
                        master.kdRanking = player.getFloorX() + ":" + player.getFloorY() + ":" + player.getFloorZ() + ":" + player.getLevel().getName();
                        break;
                    case "break":
                        master.breakRanking = player.getFloorX() + ":" + player.getFloorY() + ":" + player.getFloorZ() + ":" + player.getLevel().getName();
                        sender.sendMessage(SBedWars.DEFAULT_TITLE + "设置成功,请等待排行榜20秒的自动刷新");
                        break;
                    case "win":
                        master.winRanking = player.getFloorX() + ":" + player.getFloorY() + ":" + player.getFloorZ() + ":" + player.getLevel().getName();
                        sender.sendMessage(SBedWars.DEFAULT_TITLE + "设置成功,请等待排行榜20秒的自动刷新");
                        break;
                    default:
                        sender.sendMessage(SBedWars.DEFAULT_TITLE + "输入错误,请输入/bw help 查看帮助");
                        return false;
                }
                master.save();
                break;
            case "gui":
                id = SBedWarsAPI.getInstance().isPlayerGaming(player.getName());
                if (id != -1) {
                    GUIShopBase.openMain(player);
                    return false;
                }
                break;
            case "join":
                if (strings.length != 2) {
                    sender.sendMessage(SBedWars.DEFAULT_TITLE + "输入错误,请输入/bw help 查看帮助");
                    return false;
                }
                id = SBedWarsAPI.getInstance().isPlayerGaming(player.getName());
                if (id != -1) {
                    sender.sendMessage(SBedWars.DEFAULT_TITLE + "你已经在游戏中了,无法退出");
                    return false;
                }
                try {
                    id = Integer.valueOf(strings[1]);
                    if (!SBedWars.rooms.containsKey(id)) {
                        sender.sendMessage(SBedWars.DEFAULT_TITLE + "没有此房间,请确保输入正确");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(SBedWars.DEFAULT_TITLE + "输入错误,ID必须为一个整数");
                    return false;
                }
                if (!games.containsKey(id)) {
                    new BedWars(id).onJoin(player);
                }
                if (games.get(id).getGameMode() <= 2) {
                    games.get(id).onJoin(player);
                }
                return true;
            case "quit":
                id = SBedWarsAPI.getInstance().isPlayerGaming(player.getName());
                if (id != -1) {
                    games.get(id).onQuit(player, true);
                    return true;
                } else {
                    sender.sendMessage(SBedWars.DEFAULT_TITLE + "你不在游戏中,无法退出");
                }
                break;
            case "help":
                sender.sendMessage(TextFormat.YELLOW + "====SBed War====");
                sender.sendMessage(TextFormat.GREEN + "help - 查询帮助");
                sender.sendMessage(TextFormat.GREEN + "join <房间id> - 加入房间");
                sender.sendMessage(TextFormat.GREEN + "quit - 退出房间");
                sender.sendMessage(TextFormat.GREEN + "set - 设置房间(支持多房间)");
                break;
            case "set":
                if (!sender.isOp()) {
                    sender.sendMessage(SBedWars.DEFAULT_TITLE + "你不是OP");
                    return false;
                }
                if (SBedWars.add.containsKey(sender.getName())) {
                    RoomData data = SBedWars.add.get(sender.getName());
                    switch (strings.length) {
                        case 1:
                            switch (data.getSet()) {
                                case 1:
                                    data.setWaitPos(((int) player.getX()) + ":" + ((int) player.getY()) + ":" + ((int) player.getZ()) + ":" + player.getLevel().getName());
                                    data.setSet(2);
                                    sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功,接下来请输入/bw set 来设置结束地点");
                                    break;
                                case 2:
                                    data.setStopPos(((int) player.getX()) + ":" + ((int) player.getY()) + ":" + ((int) player.getZ()) + ":" + player.getLevel().getName());
                                    data.setSet(3);
                                    sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功,接下来请输入/bw set team <队伍数量> 来设置队伍数量");
                                    break;
                                case 5:
                                    int rid = data.getId();
                                    try {
                                        RoomConfig rc = new RoomConfig(rid);
                                        rc.addRoom(data);
                                    } catch (NoClassDefFoundError e) {
                                        e.printStackTrace();
                                    }
                                    SBedWars.add.remove(sender.getName());
                                    try {
                                        Server.getInstance().unloadLevel(Server.getInstance().getLevelByName(data.getGameWorld()));
                                        FileFunction.copy(Server.getInstance().getDataPath() + "/worlds/" + data.getGameWorld() + "/", new File(SBedWars.getInstance().getDataFolder() + "/worlds/" + data.getGameWorld() + "/").getCanonicalPath());
                                        FileFunction.remove(new File(Server.getInstance().getDataPath() + "/worlds/" + data.getGameWorld() + "/"));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功设置" + rid + "号房间!,请在插件配置文件的" + rid + ".yml中进行后续的设置!");
                                    break;
                            }
                            return true;
                        case 2:
                            switch (strings[1]) {
                                case "team":
                                    switch (data.getSet()) {
                                        case 4:
                                            HashMap<Integer, HashMap<String, Object>> team = data.getTeamData();
                                            if (team.containsKey(data.getCanTeam())) {
                                                HashMap<String, Object> td = team.get(data.getCanTeam());
                                                if (!td.containsKey("name")) {
                                                    if (td.containsKey("shopPos")) {
                                                        sender.sendMessage(SBedWars.DEFAULT_TITLE + "失败!你的下一步是输入/bw set team name <队伍名字>来设置第" + (data.getCanTeam() + 1) + "个队伍的名字");
                                                    }
                                                    if (td.containsKey("corePos")) {
                                                        td.put("shopPos", ((int) player.getX()) + ":" + ((int) player.getY()) + ":" + ((int) player.getZ()) + ":" + ((int) player.getYaw()) + ":" + ((int) player.getPitch()) + ":" + player.getLevel().getName());
                                                        team.put(data.getCanTeam(), td);
                                                        data.setTeamData(team, 2);
                                                        sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功,接下来请输入/bw set team name <队伍名字>来设置第" + (data.getCanTeam() + 1) + "个队伍的名字");
                                                    }
                                                }
                                            } else {
                                                if (Server.getInstance().getDefaultLevel().getName().equals(player.getLevel().getName())) {
                                                    sender.sendMessage(SBedWars.DEFAULT_TITLE + "游戏世界不能为主世界!");
                                                    return false;
                                                }
                                                HashMap<String, Object> td = new HashMap<>();
                                                data.setGameWorld(player.getLevel().getName());
                                                td.put("gamePos", ((int) player.getX()) + ":" + ((int) player.getY()) + ":" + ((int) player.getZ()) + ":" + player.getLevel().getName());
                                                team.put(data.getCanTeam(), td);
                                                data.setTeamData(team, 2);
                                                sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功,接下来请输入/bw set team core来设置第" + (data.getCanTeam() + 1) + "个队伍的水晶地点");
                                            }
                                            break;
                                        default:
                                            sender.sendMessage(SBedWars.DEFAULT_TITLE + "失败!你的下一步并不是这个");
                                            break;
                                    }
                                    break;
                                case "emerald":
                                    switch (data.getSet()) {
                                        case 5:
                                            ArrayList<String> emerald = data.getEmeraldPos();
                                            emerald.add(((int) player.getX()) + ":" + ((int) player.getY()) + ":" + ((int) player.getZ()) + ":" + player.getLevel().getName());
                                            data.setEmeraldPos(emerald);
                                            sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功!如果想继续设置绿宝石的掉落请继续输入/bw set emerald ,否则请输入/bw set diamond 来设置钻石的掉落!");
                                            break;
                                        default:
                                            sender.sendMessage(SBedWars.DEFAULT_TITLE + "失败!你的下一步并不是这个");
                                            break;
                                    }
                                    break;
                                case "diamond":
                                    switch (data.getSet()) {
                                        case 5:
                                            ArrayList<String> diamond = data.getDiamondPos();
                                            diamond.add(((int) player.getX()) + ":" + ((int) player.getY()) + ":" + ((int) player.getZ()) + ":" + player.getLevel().getName());
                                            data.setDiamondPos(diamond);
                                            sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功!如果想继续设置钻石的掉落请继续输入/bw set diamond ,否则请输入/bw set gold 来设置金的掉落!");
                                            break;
                                        default:
                                            sender.sendMessage(SBedWars.DEFAULT_TITLE + "失败!你的下一步并不是这个");
                                            break;
                                    }
                                    break;
                                case "gold":
                                    switch (data.getSet()) {
                                        case 5:
                                            ArrayList<String> gold = data.getGoldPos();
                                            gold.add(((int) player.getX()) + ":" + ((int) player.getY()) + ":" + ((int) player.getZ()) + ":" + player.getLevel().getName());
                                            data.setGoldPos(gold);
                                            sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功!如果想继续设置金的掉落请继续输入/bw set gold ,否则请输入/bw set silver 来设置银的掉落!");
                                            break;
                                        default:
                                            sender.sendMessage(SBedWars.DEFAULT_TITLE + "失败!你的下一步并不是这个");
                                            break;
                                    }
                                    break;
                                case "silver":
                                    switch (data.getSet()) {
                                        case 5:
                                            ArrayList<String> silver = data.getSilverPos();
                                            silver.add(((int) player.getX()) + ":" + ((int) player.getY()) + ":" + ((int) player.getZ()) + ":" + player.getLevel().getName());
                                            data.setSilverPos(silver);
                                            sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功!如果想继续设置银的掉落请继续输入/bw set silver ,否则请输入/bw set 来结束设置!");
                                            break;
                                        default:
                                            sender.sendMessage(SBedWars.DEFAULT_TITLE + "失败!你的下一步并不是这个");
                                            break;
                                    }
                                    break;
                            }
                            break;
                        case 3:
                            switch (strings[1]) {
                                case "team":
                                    switch (strings[2]) {
                                        case "core":
                                            switch (data.getSet()) {
                                                case 4:
                                                    HashMap<Integer, HashMap<String, Object>> team = data.getTeamData();
                                                    if (Server.getInstance().getDefaultLevel().getName().equals(player.getLevel().getName())) {
                                                        sender.sendMessage(SBedWars.DEFAULT_TITLE + "游戏世界不能为主世界!");
                                                        return false;
                                                    }
                                                    HashMap<String, Object> td = team.get(data.getCanTeam());
                                                    data.setGameWorld(player.getLevel().getName());
                                                    td.put("corePos", ((int) player.getX()) + ":" + ((int) player.getY()) + ":" + ((int) player.getZ()) + ":" + player.getLevel().getName());
                                                    team.put(data.getCanTeam(), td);
                                                    data.setTeamData(team, 2);
                                                    sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功,接下来请输入/bw set team 来设置第" + (data.getCanTeam() + 1) + "个队伍的商店地点（村民）");
                                                    break;
                                            }
                                            break;
                                        default:
                                            switch (data.getSet()) {
                                                case 3:
                                                    try {
                                                        data.setTeamCount(Integer.valueOf(strings[2]));
                                                    } catch (NumberFormatException e) {
                                                        sender.sendMessage(SBedWars.DEFAULT_TITLE + "你输入的数量不是一个正整数,要求为一个正整数并且大于等于2");
                                                    }
                                                    data.setSet(4);
                                                    data.setCanTeam(0);
                                                    sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功,接下来请输入/bw set team 来设置第1个队伍的出生点");
                                                    break;
                                            }
                                            break;
                                    }
                                    break;
                            }
                            break;
                        case 4:
                            switch (strings[1]) {
                                case "team":
                                    switch (data.getSet()) {
                                        case 4:
                                            switch (strings[2]) {
                                                case "name"://bw set team name <队伍名字>
                                                    HashMap<Integer, HashMap<String, Object>> team = data.getTeamData();
                                                    if (team.containsKey(data.getCanTeam())) {
                                                        HashMap<String, Object> td = team.get(data.getCanTeam());
                                                        if (td.containsKey("shopPos")) {
                                                            td.put("name", strings[3]);
                                                            team.put(data.getCanTeam(), td);
                                                            if (data.getCanTeam() + 1 < data.getTeamCount()) {
                                                                data.setCanTeam(data.getCanTeam() + 1);
                                                                sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功,接下来请输入/bw set team 来设置第" + (data.getCanTeam() + 1) + "个队伍的出生点");
                                                            } else {
                                                                sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功,队伍信息全部设置完毕!接下来请输入/bw set emerald 设置绿宝石的掉落地点!");
                                                                data.setSet(5);
                                                            }
                                                            data.setTeamData(team, 2);
                                                        } else {
                                                            sender.sendMessage(SBedWars.DEFAULT_TITLE + "失败!你的下一步并不是这个");
                                                        }
                                                    }
                                                    break;
                                            }
                                            break;
                                    }
                                    break;
                            }
                            break;
                    }
                } else {
                    RoomData data = new RoomData(SBedWarsAPI.getInstance().getFreeID());
                    data.setSet(1);
                    SBedWars.add.put(sender.getName(), data);
                    sender.sendMessage(SBedWars.DEFAULT_TITLE + "成功,接下来请输入/bw set 来设置等待地点");
                }
                break;
        }
        return false;
    }
}
