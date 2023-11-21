package net.mcpes.foundhi.plugin.currency.imeu

import cn.nukkit.item.Item
import cn.nukkit.utils.Config
import net.mcpes.foundhi.core.plugin.modal.FoundHiPluginBase
import net.mcpes.foundhi.plugin.currency.imeu.command.ItemMenuCommand
import net.mcpes.foundhi.plugin.currency.imeu.listener.PlayerListener
import net.mcpes.foundhi.plugin.currency.imeu.menu.ItemMenu
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/13
 * version 1.0
 */
class SItemMenu : FoundHiPluginBase() {
    companion object {
        lateinit var instance: SItemMenu
        @JvmStatic
        var menus = HashMap<String, ItemMenu>()
        @JvmStatic
        var `in` = HashMap<String, String>()
        @JvmStatic
        var touch = HashMap<String, Int>()
        @JvmStatic
        var world = HashMap<String, String>()
        @JvmStatic
        var playerItems = HashMap<String, Map<Int, Item>>()
    }

    init {
        instance = this
    }

    lateinit var mainConfig: Config
    lateinit var menuConfig: Config

    override fun onLoad() {
        this.saveResource("config.yml")
        this.saveResource("menus.yml")
        mainConfig = Config(this.dataFolder.toString() + "/config.yml")
        menuConfig = Config(this.dataFolder.toString() + "/menus.yml")
        world = mainConfig.get("指定世界") as HashMap<String, String>
        this.logger.info("加载成功")
    }

    override fun onEnable() {
        this.loadMenu()
        this.server.commandMap.register("imeu", ItemMenuCommand("imeu"))
        this.server.pluginManager.registerEvents(PlayerListener(), this)
        this.logger.info("开启成功")
    }

    override fun onDisable() {
        this.logger.info("关闭成功")
    }

    private fun loadMenu() {
        menuConfig.all.keys.forEach { key: String -> menus[key] = ItemMenu(key) }
    }
}