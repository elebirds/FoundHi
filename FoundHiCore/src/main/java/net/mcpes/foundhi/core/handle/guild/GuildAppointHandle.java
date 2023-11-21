package net.mcpes.foundhi.core.handle.guild;

import cn.nukkit.Player;
import cn.nukkit.Server;
import net.mcpes.foundhi.core.app.data.guild.Guild;
import net.mcpes.foundhi.core.handle.Handle;
import net.mcpes.foundhi.core.handle.HandleResults;

/**
 * FoundHi
 *
 * @author yui Copyright (c) 2018/8/4
 * version 1.0
 */
public class GuildAppointHandle extends Handle {
    private String leader;

    public GuildAppointHandle(String leader) {
        this.leader = leader;
    }


    @Override
    public void handle() {
        Player player = Server.getInstance().getPlayerExact(this.leader);
        if (player == null || (!player.isOnline())) return;
        switch (result) {
            default:
                player.sendMessage(Guild.GUILD_TITLE + "未知原因");
                break;
            case HandleResults.PARAMETER_ERROR:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:参数错误!");
                break;
            case HandleResults.PLAYER_OFFLINE:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:玩家已下线!");
                break;
            case HandleResults.NOT_EXIST_POSITION:
                player.sendMessage(Guild.GUILD_TITLE + "§a呀，好像不存在这个职位");
                break;
            case HandleResults.NOT_MEMBER:
                player.sendMessage(Guild.GUILD_TITLE + "§a这个玩家并不是你的公会的成员哦！");
                break;
            case HandleResults.TOO_MUCH_ADMIN:
                player.sendMessage(Guild.GUILD_TITLE + "§a此职位的管理员已经满员惹");
                break;
            case HandleResults.ALREADY_IS_THIS_POSITION:
                player.sendMessage(Guild.GUILD_TITLE + "§a这个玩家已经是此职务上的管理员了");
                break;
            case HandleResults.NOT_EXIST_GUILD:
                player.sendMessage(Guild.GUILD_TITLE + "§a不存在此公会");
                break;
        }
    }
}
