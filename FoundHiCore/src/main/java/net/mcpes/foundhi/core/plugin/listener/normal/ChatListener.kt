package net.mcpes.foundhi.core.plugin.listener.normal

import cn.nukkit.Server
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerChatEvent
import net.mcpes.foundhi.core.plugin.FoundHiPlugin

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/22
 * version 1.0
 */
class ChatListener : Listener {
    @EventHandler
    fun onPlayerChat(event: PlayerChatEvent) {
        event.setCancelled()
        if (event.message.contains("§")) {
            event.player.sendMessage("§c聊天不能包涵颜色符号")
            return
        }
        val p = event.player
        val pn = p.name
        val pd = FoundHiPlugin.instance.getPlayer(pn)!!
        val marry: String = if (pd.lover != "") {
            "§a[§c情侣§4❤§f" + pd.lover + "§a]"
        } else {
            "§a[§c情侣§4❤§2单身贵族§a]"
        }
        val guild = FoundHiPlugin.instance.getGuild(pd.guild)
        val sName: String = if (guild != null) {
            guild.color + guild.name
        } else {
            "No~team"
        }
        val title: String = if (pd.title.size != 0 && pd.titling != -1) {
            pd.title[pd.titling]
        } else {
            ""
        }
        Server.getInstance().broadcastMessage(FoundHiPlugin.instance.name + " §6>> §l[§f" + sName + "§l]" + marry + "§e<§f" + title + "の" + pd.nameColor + pn + "§e>:" + pd.chatColor + event.message)
    }
}
