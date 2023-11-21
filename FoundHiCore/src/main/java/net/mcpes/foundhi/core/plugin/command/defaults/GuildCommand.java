package net.mcpes.foundhi.core.plugin.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import net.mcpes.foundhi.core.app.data.guild.Guild;
import net.mcpes.foundhi.core.handle.HandleResults;
import net.mcpes.foundhi.core.plugin.command.FoundHiPluginCommandFunction;
import net.mcpes.foundhi.core.utils.GuildUtils;
import net.mcpes.foundhi.core.utils.Utils;

public class GuildCommand extends Command implements FoundHiPluginCommandFunction {
    public GuildCommand(String name) {
        super(name, "公会系统", "", new String[]{"公会", "g"});
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (strings.length == 0) {
            sender.sendMessage(Guild.GUILD_TITLE + "§c错误的指令用法。请使用 /" + s + " help 来显示指令列表。");
            return true;
        }
        switch (strings[0]) {
            case "help":
                break;
            case "transfer":
            case "t":
            case "转让":
                if (!sender.isPlayer()) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c控制台禁用此命令!");
                    return true;
                }
                if (strings.length != 2) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c错误的指令用法。请使用 /" + s + " help 来显示指令列表。");
                    return true;
                }
                switch (GuildUtils.transfer(strings[1], sender.getName())) {
                    case HandleResults.NORMAL_SUCCESS:
                        sender.sendMessage(Guild.GUILD_TITLE + Guild.GUILD_TITLE + Guild.GUILD_TITLE + "§3正在处理中...");
                        break;
                    case HandleResults.PLAYER_OFFLINE:
                        sender.sendMessage(Guild.GUILD_TITLE + Guild.GUILD_TITLE + "§c§l操作失败:玩家已下线!");
                        break;
                    case HandleResults.NOT_JOIN_GUILD:
                        sender.sendMessage(Guild.GUILD_TITLE + Guild.GUILD_TITLE + "§4你还未加入公会");
                        break;
                    case HandleResults.PARAMETER_ERROR:
                        sender.sendMessage(Guild.GUILD_TITLE + "§4转让者不能是自己！");
                        break;
                }
                break;
            case "base":
            case "b":
            case "基地":
                break;
            case "appoint":
            case "ap":
            case "任命":
                // 公会 任命 <玩家> <职务>
                if (!sender.isPlayer()) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c控制台禁用此命令!");
                    return true;
                }
                if (strings.length != 3) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c错误的指令用法。请使用 /" + s + " help 来显示指令列表。");
                    return true;
                }
                if (!Utils.isInteger(strings[2])) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c职务id必须为数字");
                    return true;
                }
                switch (GuildUtils.appoint(strings[1], sender.getName(), Integer.valueOf(strings[2]))) {
                    case HandleResults.NORMAL_SUCCESS:
                        sender.sendMessage(Guild.GUILD_TITLE + Guild.GUILD_TITLE + Guild.GUILD_TITLE + "§3正在处理中...");
                        break;
                    case HandleResults.PLAYER_OFFLINE:
                        sender.sendMessage(Guild.GUILD_TITLE + Guild.GUILD_TITLE + "§c§l操作失败:玩家已下线!");
                        break;
                    case HandleResults.NOT_JOIN_GUILD:
                        sender.sendMessage(Guild.GUILD_TITLE + Guild.GUILD_TITLE + "§4你还未加入公会");
                        break;
                }
                break;
            case "ban":
            case "封禁":
                break;
            case "unban":
            case "解禁":
                break;
            case "upgrade":
            case "upg":
            case "升级":
                break;
            case "donate":
            case "do":
            case "捐献":
                break;
            case "information":
            case "info":
            case "信息":
                break;
            case "kick":
            case "k":
            case "踢出":
                if (!sender.isPlayer()) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c控制台禁用此命令!");
                    return true;
                }
                if (strings.length != 2) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c错误的指令用法。请使用 /" + s + " help 来显示指令列表。");
                    return true;
                }
                switch (GuildUtils.kick(sender.getName(), strings[1])) {
                    case HandleResults.NORMAL_SUCCESS:
                        sender.sendMessage(Guild.GUILD_TITLE + "§3正在处理中...");
                        break;
                    case HandleResults.PLAYER_OFFLINE:
                        sender.sendMessage(Guild.GUILD_TITLE + "§c§l操作失败:玩家已下线!");
                        break;
                    case HandleResults.NOT_JOIN_GUILD:
                        sender.sendMessage(Guild.GUILD_TITLE + "§4你还未加入公会");
                        break;
                }
                break;
            case "accept":
            case "a":
            case "接受":
                if (!sender.isPlayer()) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c控制台禁用此命令!");
                    return true;
                }
                if (strings.length != 2) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c错误的指令用法。请使用 /" + s + " help 来显示指令列表。");
                    return true;
                }
                switch (GuildUtils.accept(sender.getName(), strings[1])) {
                    case HandleResults.NORMAL_SUCCESS:
                        sender.sendMessage(Guild.GUILD_TITLE + "§3正在处理中...");
                        break;
                    case HandleResults.PLAYER_OFFLINE:
                        sender.sendMessage(Guild.GUILD_TITLE + "§c§l操作失败:玩家已下线!");
                        break;
                    case HandleResults.NO_INVITATION:
                        sender.sendMessage(Guild.GUILD_TITLE + "§4没有此条邀请");
                        break;
                }
                break;
            case "reject":
            case "r":
            case "拒绝":
                if (!sender.isPlayer()) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c控制台禁用此命令!");
                    return true;
                }
                if (strings.length != 2) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c错误的指令用法。请使用 /" + s + " help 来显示指令列表。");
                    return true;
                }
                switch (GuildUtils.reject(sender.getName(), strings[1])) {
                    case HandleResults.NORMAL_SUCCESS:
                        sender.sendMessage(Guild.GUILD_TITLE + "§3正在处理中..");
                        break;
                    case HandleResults.PLAYER_OFFLINE:
                        sender.sendMessage(Guild.GUILD_TITLE + "§c§l操作失败:玩家已下线!");
                        break;
                    case HandleResults.NO_INVITATION:
                        sender.sendMessage(Guild.GUILD_TITLE + "§4没有此条邀请");
                        break;
                }
                break;
            case "invite":
            case "i":
            case "邀请":
                if (!sender.isPlayer()) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c控制台禁用此命令!");
                    return true;
                }
                if (strings.length != 2) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c错误的指令用法。请使用 /" + s + " help 来显示指令列表。");
                    return true;
                }
                switch (GuildUtils.invite(sender.getName(), strings[1])) {
                    case HandleResults.NORMAL_SUCCESS:
                        sender.sendMessage(Guild.GUILD_TITLE + "§3正在处理中...");
                        break;
                    case HandleResults.PLAYER_OFFLINE:
                        sender.sendMessage(Guild.GUILD_TITLE + "§c§l操作失败:玩家已下线!");
                        break;
                    case HandleResults.NOT_EXIST_GUILD:
                        sender.sendMessage(Guild.GUILD_TITLE + "§c§l你还未加入公会");
                        break;
                    case HandleResults.NO_PERMISSION:
                        sender.sendMessage(Guild.GUILD_TITLE + "§c§l你没有权限");
                    case HandleResults.ALREADY_JOIN_GUILD:
                        sender.sendMessage(Guild.GUILD_TITLE + "§c§l操作失败:你已经加入了公会!");
                        break;
                }
                break;
            case "create":
            case "c":
            case "创建":
                if (!sender.isPlayer()) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c控制台禁用此命令!");
                    return true;
                }
                if (strings.length != 2) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c错误的指令用法。请使用 /" + s + " help 来显示指令列表。");
                    return true;
                }
                switch (GuildUtils.create(sender.getName(), strings[1])) {
                    case HandleResults.NORMAL_SUCCESS:
                        sender.sendMessage(Guild.GUILD_TITLE + "§3正在处理中...");
                        break;
                    case HandleResults.PLAYER_OFFLINE:
                        sender.sendMessage(Guild.GUILD_TITLE + "§c§l操作失败:玩家已下线!");
                        break;
                    case HandleResults.NOT_ENOUGH_MONEY:
                        sender.sendMessage(Guild.GUILD_TITLE + "§c§l操作失败:你的金币不足!");
                        break;
                    case HandleResults.ALREADY_JOIN_GUILD:
                        sender.sendMessage(Guild.GUILD_TITLE + "§c§l操作失败:你已经加入了公会!");
                        break;
                }
                break;
            case "解散":
            case "dissolve":
            case "d":
                if (!sender.isPlayer()) {
                    sender.sendMessage(Guild.GUILD_TITLE + "§c控制台禁用此命令!");
                    return true;
                }
                switch (GuildUtils.dissolve(sender.getName())) {
                    case HandleResults.NORMAL_SUCCESS:
                        sender.sendMessage(Guild.GUILD_TITLE + "§3正在处理中...");
                        break;
                    case HandleResults.PLAYER_OFFLINE:
                        sender.sendMessage(Guild.GUILD_TITLE + "§c§l操作失败:玩家已下线!");
                        break;
                    case HandleResults.NOT_JOIN_GUILD:
                        sender.sendMessage(Guild.GUILD_TITLE + "§c§l操作失败:你没有加入公会!");
                        break;
                    case HandleResults.NOT_LEADER:
                        sender.sendMessage(Guild.GUILD_TITLE + "§c§l操作失败:你不是会长!");
                        break;
                }
                break;
        }
        return false;
    }
}
