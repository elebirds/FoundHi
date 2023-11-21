package net.mcpes.foundhi.core.handle.guild;

import cn.nukkit.Player;
import cn.nukkit.Server;
import net.mcpes.foundhi.core.app.data.guild.Guild;
import net.mcpes.foundhi.core.handle.HandleClass;
import net.mcpes.foundhi.core.handle.HandleResults;

/**
 * FoundHi
 *
 * @author yui Copyright (c) 2018/7/28
 * version 1.0
 */
public class GuildTransferHandle extends HandleClass {
    private String leaderTo;
    private String leaderNow;

    public GuildTransferHandle(String leaderTo, String leaderNow) {
        this.leaderTo = leaderTo;
        this.leaderNow = leaderNow;
    }

    @Override
    public void handle() {
        Player player = Server.getInstance().getPlayerExact(leaderNow);
        if (player == null || (!player.isOnline())) return;
        switch (result) {
            case HandleResults.NORMAL_SUCCESS:
                player.sendMessage(Guild.GUILD_TITLE + "§e成功将会长转让给 " + leaderTo);
                break;
            case HandleResults.PARAMETER_ERROR:
                player.sendMessage(Guild.GUILD_TITLE + "§a指令错误咯");
                break;
            case HandleResults.PLAYER_OFFLINE:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:玩家已下线!");
                break;
            case HandleResults.NOT_MEMBER:
                player.sendMessage(Guild.GUILD_TITLE + "§a此人不是公会成员");
                break;
            case HandleResults.NOT_JOIN_GUILD:
                player.sendMessage(Guild.GUILD_TITLE + "§a你还未加入公会");
                break;
            case HandleResults.NOT_LEADER:
                player.sendMessage(Guild.GUILD_TITLE + "§a你不是会长，无权使用此指令");
                break;
            case HandleResults.NOT_EXIST_GUILD:
                player.sendMessage(Guild.GUILD_TITLE + "§a不存在此公会");
                break;
        }
    }
}
