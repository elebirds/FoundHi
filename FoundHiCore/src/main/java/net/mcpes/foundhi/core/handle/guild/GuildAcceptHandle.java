package net.mcpes.foundhi.core.handle.guild;

import cn.nukkit.Player;
import cn.nukkit.Server;
import net.mcpes.foundhi.core.app.data.guild.Guild;
import net.mcpes.foundhi.core.handle.HandleClass;
import net.mcpes.foundhi.core.handle.HandleResults;

/**
 * FoundHi
 *
 * @author yui Copyright (c) 2018/7/7
 * version 1.0
 */
public class GuildAcceptHandle extends HandleClass {
    private String name;
    private String inviter;
    private String guildName;

    public GuildAcceptHandle(String name, String inviter, String guildName) {
        this.name = name;
        this.inviter = inviter;
        this.guildName = guildName;
    }

    @Override
    public void handle() {
        Player player = Server.getInstance().getPlayerExact(this.name);
        if (player == null || (!player.isOnline())) return;
        switch (result) {
            case HandleResults.SCCESS_SEND_APPLY:
                player.sendMessage(Guild.GUILD_TITLE + "成功接受" + inviter + "的邀请并发送加入公会：" + guildName + "申请");
                break;
            case HandleResults.NORMAL_SUCCESS:
                player.sendMessage(Guild.GUILD_TITLE + "成功接受" + inviter + "的邀请并加入公会：" + guildName);
                break;
            case HandleResults.NO_INVITATION:
                player.sendMessage(Guild.GUILD_TITLE + "没有这条邀请");
                break;
            case HandleResults.NOT_EXIST_GUILD:
                player.sendMessage(Guild.GUILD_TITLE + "§a不存在此公会!");
                break;
            case HandleResults.ALREADY_IS_MEMBER:
                player.sendMessage(Guild.GUILD_TITLE + "§a你已是此公会成员!");
                break;
            case HandleResults.ALREADY_JOIN_GUILD:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:你已有公会!");
                break;
            case HandleResults.GUILD_ALREADY_FULL:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:对方公会已满!");
                break;
            case HandleResults.UNKNOW_ERROR:
                player.sendMessage(Guild.GUILD_TITLE + "§a操作失败:未知错误!");
                break;
        }
    }
}
