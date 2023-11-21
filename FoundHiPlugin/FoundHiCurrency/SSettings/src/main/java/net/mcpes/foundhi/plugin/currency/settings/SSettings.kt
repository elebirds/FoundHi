package net.mcpes.foundhi.plugin.currency.settings

import cn.nukkit.Player
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerServerSettingsRequestEvent
import cn.nukkit.event.player.PlayerSettingsRespondedEvent
import cn.nukkit.form.element.Element
import cn.nukkit.form.element.ElementLabel
import cn.nukkit.form.window.FormWindowCustom
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.FoundHiPlugin.Companion.SERVER_TITLE
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.core.plugin.modal.FoundHiPluginBase

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/12
 * version 1.0
 */
class SSettings : FoundHiPluginBase(), Listener {
    override fun onEnable() {
        this.server.pluginManager.registerEvents(this, this)
    }

    private fun getSettings(player: Player): FormWindowCustom {
        val contents: ArrayList<Element> = ArrayList()
        val pd = FoundHiPlugin.instance.getPlayer(player.name)!!
        contents.add(ElementLabel("欢迎来到${SERVER_TITLE}服务器!\n\n你的名字：${player.displayName}" +
                "\n\n当前拥有：§e${pd.money1}枚 §6金币,§e${pd.money2}枚 §b钻石,§e${pd.money3}枚 §d点券"))
        //return FormWindowCustom("Summit服务器设置",contents,this.dataFolder.path+"/logo.png")
        return FormWindowCustom("Summit服务器设置", contents, "C:/Users/cheng/Pictures/logo.jpg")
    }

    @EventHandler
    fun onOpen(event: PlayerServerSettingsRequestEvent) {
        if (event.player !is NukkitPlayer) return
        event.settings.clear()
        event.settings[(event.player as NukkitPlayer).getFormWindowCount()] = this.getSettings(event.player)
    }

    @EventHandler
    fun onClose(event: PlayerSettingsRespondedEvent) {

    }
}