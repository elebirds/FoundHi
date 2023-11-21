package net.mcpes.foundhi.plugin.hub.music.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import net.mcpes.foundhi.plugin.hub.music.SMusic;
import net.mcpes.foundhi.plugin.hub.music.config.MusicTitleConfig;

import java.io.File;

import static net.mcpes.foundhi.plugin.hub.music.SMusic.*;

/**
 * @author hhm
 */

public class MusicCommand extends Command {
    public MusicCommand() {
        super("music");
        this.setAliases(new String[]{
                "music",
                "music 加载 <音乐名字>",
                "music 下一首",
                "music 上一首",
                "music 播放 <开启/关闭>"
        });
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (strings.length == 0) {
            sender.sendMessage(PluginName + "§6输入错误,请输入/music help 查看帮助");
        } else {
            switch (strings[0]) {
                case "stop":
                case "暂停":
                    if (sender instanceof ConsoleCommandSender) {
                        sender.sendMessage(PluginName + "§6你不是一个玩家!");
                    }
                    songPlayers.remove(sender);
                    break;
                case "添加浮空":
                    if (sender instanceof ConsoleCommandSender) {
                        sender.sendMessage(PluginName + "§6你不是一个玩家!");
                    }
                    if (!sender.isOp()) {
                        sender.sendMessage(PluginName + "§6你没有这个权限!");
                        return false;
                    }
                    MusicTitleConfig.addPos(((Player) sender).getLocation());
                    sender.sendMessage(PluginName + "§6成功");
                    break;
                case "加载":
                case "loadAll":
                    if (!sender.isOp()) {
                        sender.sendMessage(PluginName + "§6你没有这个权限!");
                        return false;
                    }
                    if (strings.length == 2) {
                        if (new File(SMusic.getInstance().getDataFolder() + "/songs/" + strings[1] + ".nbs").exists()) {
                            getAPI().loadSong(strings[1]);
                            sender.sendMessage(PluginName + "§6成功操作");
                        } else {
                            sender.sendMessage(PluginName + "§6tan90°");
                        }
                    } else {
                        sender.sendMessage(PluginName + "§6输入错误,请输入/music help 查看帮助");
                    }
                    break;
                case "下一首":
                case "next":
                    if (!sender.isOp()) {
                        sender.sendMessage(PluginName + "§6你没有这个权限!");
                        return false;
                    }
                    if (strings.length != 1)
                        sender.sendMessage(PluginName + "§6输入错误,请输入/music help 查看帮助");
                    else {
                        getAPI().down();
                        sender.sendMessage(PluginName + "§6成功设置");
                    }
                    break;
                case "上一首":
                case "previous":
                    if (!sender.isOp()) {
                        sender.sendMessage(PluginName + "§6你没有这个权限!");
                        return false;
                    }
                    if (strings.length != 1)
                        sender.sendMessage(PluginName + "§6输入错误,请输入/music help 查看帮助");
                    else {
                        if (playMode == RANDOM_PLAY) {
                            sender.sendMessage(PluginName + "§6随机播放你怎么上一首= =");
                        } else {
                            getAPI().up();
                            sender.sendMessage(PluginName + "§6成功设置");
                        }
                    }
                    break;
                case "播放":
                case "play":
                    if (!sender.isOp()) {
                        sender.sendMessage(PluginName + "§6你没有这个权限!");
                        return false;
                    }
                    if (strings.length == 2) {
                        switch (strings[1]) {
                            case "开启":
                            case "on":
                                playing = true;
                                sender.sendMessage(PluginName + "§6成功设置");
                                break;
                            case "关闭":
                            case "off":
                                playing = false;
                                sender.sendMessage(PluginName + "§6成功设置");
                                break;
                            default:
                                sender.sendMessage(PluginName + "§6输入错误,请输入/music help 查看帮助");
                                return false;
                        }
                    }
                    break;
                case "帮助":
                case "help":
                    break;
                default:
                    sender.sendMessage(PluginName + "§6输入错误,请输入/music help 查看帮助");
                    break;

            }
        }
        return false;
    }
}