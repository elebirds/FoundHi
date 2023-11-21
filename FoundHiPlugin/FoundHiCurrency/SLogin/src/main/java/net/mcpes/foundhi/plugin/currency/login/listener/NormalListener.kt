package net.mcpes.foundhi.plugin.currency.login.listener

import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerJoinEvent
import cn.nukkit.event.player.PlayerPreLoginEvent
import cn.nukkit.event.player.PlayerQuitEvent
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.plugin.currency.login.SLogin
import net.mcpes.foundhi.plugin.currency.login.SLogin.Companion.TITLE
import net.mcpes.foundhi.plugin.currency.login.config.MainConfig
import net.mcpes.foundhi.plugin.currency.login.data.PlayerLoginCache
import net.mcpes.foundhi.plugin.currency.login.utils.LoginUtils

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/26
 * version 1.0
 */
class NormalListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPrelogin(event: PlayerPreLoginEvent) {
        SLogin.instance.pData[event.player.name] = PlayerLoginCache(event.player.name)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(event: PlayerJoinEvent) {
        if (event.player !is NukkitPlayer) return
        if (event.player.name.toLowerCase() == "steve" && !MainConfig.isCanSteve) {
            event.player.kick("$TITLE 禁止使用Steve账户登录!")
        }
        SLogin.instance.notLogin.add(event.player.name)
        SLogin.instance.showTitle(event.player as NukkitPlayer)
        when (LoginUtils.login(event.player)) {
            0, 1, 2 -> event.player.sendMessage("$TITLE 自动登陆成功,稍后将把您转移到大厅服务器")
            -2 -> SLogin.instance.openRegister(event.player as NukkitPlayer)
            -3 -> SLogin.instance.openLogin(event.player as NukkitPlayer)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onQuit(event: PlayerQuitEvent) {
        SLogin.instance.notLogin.remove(event.player.name)
        if (SLogin.instance.logged.contains(event.player.name)) {
            SLogin.instance.logged.remove(event.player.name)
            SLogin.instance.freeLogin[event.player.name] = 0
        }
        if (SLogin.instance.pData.containsKey(event.player.name)) {
            SLogin.instance.pData[event.player.name]!!.save(true)
        }
    }
}
