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
public class GuildDissolveHandle extends HandleClass {
    private String name;
    private int id;

    public GuildDissolveHandle(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public void handle() {
        Player player = Server.getInstance().getPlayerExact(this.name);
        if (player == null || (!player.isOnline())) return;
        switch (result) {
            case HandleResults.NORMAL_SUCCESS:
                player.sendMessage(Guild.GUILD_TITLE + "成功解散公会!");
                break;
            case HandleResults.PLAYER_OFFLINE:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:玩家已下线!");
                break;
            case HandleResults.NOT_JOIN_GUILD:
                player.sendMessage(Guild.GUILD_TITLE + "§c操作失败:你没有加入公会!");
                break;
            case HandleResults.NOT_LEADER:
                player.sendMessage(Guild.GUILD_TITLE + "§c操作失败:你不是会长!");
                break;
            case HandleResults.UNKNOW_ERROR:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:未知错误!");
                break;
        }
    }
}
