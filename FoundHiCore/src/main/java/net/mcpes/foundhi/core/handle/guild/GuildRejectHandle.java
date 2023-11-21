package net.mcpes.foundhi.core.handle.guild;

import cn.nukkit.Player;
import cn.nukkit.Server;
import net.mcpes.foundhi.core.app.data.guild.Guild;
import net.mcpes.foundhi.core.handle.HandleClass;
import net.mcpes.foundhi.core.handle.HandleResults;

/**
 * FoundHi
 *
 * @author yui Copyright (c) 2018/7/12
 * version 1.0
 */
public class GuildRejectHandle extends HandleClass {
    private String name;

    public GuildRejectHandle(String name) {
        this.name = name;
    }

    @Override
    public void handle() {
        Player player = Server.getInstance().getPlayerExact(this.name);
        if (player == null || (!player.isOnline())) return;
        switch (this.result) {
            case HandleResults.NORMAL_SUCCESS:
                player.sendMessage(Guild.GUILD_TITLE + "成功拒绝邀请");
                break;
            case HandleResults.ALREADY_JOIN_GUILD:
                player.sendMessage(Guild.GUILD_TITLE + "你已经加入其他公会惹//");
                break;
            case HandleResults.PLAYER_OFFLINE:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:玩家已下线!");
                break;
            case HandleResults.ALREADY_IS_MEMBER:
                player.sendMessage(Guild.GUILD_TITLE + "你已是此公会成员惹 ");
            case HandleResults.UNKNOW_ERROR:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:未知错误!");
                break;
        }
    }
}
