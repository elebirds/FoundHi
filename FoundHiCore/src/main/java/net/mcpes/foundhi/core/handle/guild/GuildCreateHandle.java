package net.mcpes.foundhi.core.handle.guild;

import cn.nukkit.Player;
import cn.nukkit.Server;
import net.mcpes.foundhi.core.app.data.guild.Guild;
import net.mcpes.foundhi.core.handle.HandleClass;
import net.mcpes.foundhi.core.handle.HandleResults;

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/22
 * version 1.0
 */
public class GuildCreateHandle extends HandleClass {
    private String player, name;

    public GuildCreateHandle(String player, String name) {
        this.player = player;
        this.name = name;
    }

    public String getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    @Override
    public void handle() {
        Player player = Server.getInstance().getPlayerExact(this.player);
        if (player == null || (!player.isOnline())) return;
        switch (result) {
            default:
                player.sendMessage(Guild.GUILD_TITLE + "§b创建成功!公会ID为:" + result);
                break;
            case HandleResults.PARAMETER_ERROR:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:参数错误!");
                break;
            case HandleResults.PLAYER_OFFLINE:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:玩家已下线!");
                break;
            case HandleResults.GUILD_NAME_EXIST:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:公会名称已经存在");
                break;
            case HandleResults.UNKNOW_ERROR:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:未知错误!");
                break;
            case HandleResults.ALREADY_JOIN_GUILD:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:你已经加入了公会!");
                break;
        }
    }
}
