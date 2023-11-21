package net.mcpes.foundhi.core.handle.guild;

import cn.nukkit.Player;
import cn.nukkit.Server;
import net.mcpes.foundhi.core.app.data.guild.Guild;
import net.mcpes.foundhi.core.handle.HandleClass;
import net.mcpes.foundhi.core.handle.HandleResults;

/**
 * FoundHi
 *
 * @author yui Copyright (c) 2018/7/9
 * version 1.0
 */
public class GuildKickHandle extends HandleClass {
    private String name;

    public GuildKickHandle(String name) {
        this.name = name;
    }

    @Override
    public void handle() {
        Player player = Server.getInstance().getPlayerExact(this.name);
        if (player == null || (!player.isOnline())) return;
        switch (this.result) {
            case HandleResults.NORMAL_SUCCESS:
                player.sendMessage(Guild.GUILD_TITLE + "成功踢出玩家");
                break;
            case HandleResults.NOT_JOIN_GUILD:
                player.sendMessage(Guild.GUILD_TITLE + "你还未加入公会");
                break;
            case HandleResults.PARAMETER_ERROR:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:参数错误!");
                break;
            case HandleResults.NO_PERMISSION:
                player.sendMessage(Guild.GUILD_TITLE + "你不是公会管理员，没有此权限");
                break;
            case HandleResults.PLAYER_OFFLINE:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:玩家已下线!");
                break;
            case HandleResults.NOT_MEMBER:
                player.sendMessage(Guild.GUILD_TITLE + "此人并不是你公会的成员！");
                break;
        }
    }
}
