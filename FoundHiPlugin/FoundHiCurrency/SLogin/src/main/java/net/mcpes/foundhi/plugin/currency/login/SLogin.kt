package net.mcpes.foundhi.plugin.currency.login

import cn.nukkit.plugin.PluginBase
import cn.nukkit.scheduler.AsyncTask
import net.mcpes.foundhi.core.plugin.FoundHiPlugin.Companion.SERVER_TITLE
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.core.plugin.gui.window.lambda.LambdaCustomGUI
import net.mcpes.foundhi.plugin.currency.login.command.ChangePasswordCommand
import net.mcpes.foundhi.plugin.currency.login.command.LoginCommand
import net.mcpes.foundhi.plugin.currency.login.command.RegisterCommand
import net.mcpes.foundhi.plugin.currency.login.config.MainConfig
import net.mcpes.foundhi.plugin.currency.login.data.PlayerLoginCache
import net.mcpes.foundhi.plugin.currency.login.data.PlayerLoginDatabase
import net.mcpes.foundhi.plugin.currency.login.listener.ActionListener
import net.mcpes.foundhi.plugin.currency.login.listener.NormalListener
import net.mcpes.foundhi.plugin.currency.login.utils.LoginUtils
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/26
 * version 1.0
 */
class SLogin : PluginBase() {
    var pData = HashMap<String, PlayerLoginCache>()
    var logged = ArrayList<String>()
    var notLogin = ArrayList<String>()
    var freeLogin = ConcurrentHashMap<String, Int>()
    private var registerGUIer = ArrayList<String>()
    private lateinit var registerGUI: LambdaCustomGUI
    private var loginGUIer = ArrayList<String>()
    private lateinit var loginGUI: LambdaCustomGUI
    var registerCommands = ArrayList<String>()
    var loginCommands = ArrayList<String>()
    var changeCommands = ArrayList<String>()

    override fun onLoad() {
        instance = this
        this.server.logger.info(TITLE + "由 " + SERVER_TITLE + " 团队制作")
        this.saveResource("config.yml", "config.yml", false)
        MainConfig.init()
        registerGUI = LambdaCustomGUI("SLogin_register", "$TITLE- 注册")
        registerGUI.addText("t1", "§l§6欢迎来到$SERVER_TITLE!请开始进行注册!")
        registerGUI.addInput("pw", "密码")
        registerGUI.addText("t2", "§l§6如果您选择绑定设备,那么之后用这个设备进入服务器,将不需要登陆!")
        registerGUI.addCheckbox("bcid", "绑定此设备", true)
        registerGUI.addText("t3", "§l§6如果您选择绑定XBOX,那么之后用这个XBOX进入服务器,将不需要登陆!(前提是你用了XBOX账户)")
        registerGUI.addCheckbox("bxbox", "绑定XBOX", true)
        registerGUI.submittedClickedListener = BiConsumer { data, player ->
            if (player !is NukkitPlayer) return@BiConsumer
            if (!registerGUIer.contains(player.name)) return@BiConsumer
            when (LoginUtils.register(player, data["pw"].toString(), data["bcid"].toString().toBoolean(), data["bxbox"].toString().toBoolean())) {
                0 -> player.sendMessage("$TITLE 注册成功,稍后将把您转移到大厅服务器!")
                1 -> player.sendMessage("$TITLE 你已经注册了!")
                -1 -> player.sendMessage("$TITLE 发生未知错误!")
            }
        }
        registerGUI.closedClickedListener = Consumer {
            registerGUIer.remove(it.name)
        }
        loginGUI = LambdaCustomGUI("SLogin_login", "$TITLE- 登录")
        loginGUI.addText("t1", "§l§6欢迎来到$SERVER_TITLE!登录即可进入服务器!")
        loginGUI.addInput("pw", "密码")
        loginGUI.submittedClickedListener = BiConsumer { data, player ->
            if (!loginGUIer.contains(player.name)) return@BiConsumer
            if (player !is NukkitPlayer) return@BiConsumer
            when (LoginUtils.login(player, data["pw"].toString())) {
                0 -> {
                    loginGUIer.remove(name)
                    player.sendMessage("$TITLE 登陆成功,稍后将把您转移到大厅服务器!")
                }
                -1 -> {
                    player.sendMessage("$TITLE 密码错误!")
                    loginGUIer.remove(name)
                    this.openRegister(player)
                }
                -2 -> {
                    player.sendMessage("$TITLE 你还没有注册!")
                    loginGUIer.remove(name)
                    this.openLogin(player)
                }
                -3 -> {
                    loginGUIer.remove(name)
                    player.sendMessage("$TITLE 发生未知错误!")
                }
                -4 -> {
                    loginGUIer.remove(name)
                    player.sendMessage("$TITLE 你已经登陆!")
                }
            }
        }
        loginGUI.closedClickedListener = Consumer {
            loginGUIer.remove(it.name)
        }
    }

    override fun onEnable() {
        try {
            PlayerLoginDatabase.newTable()
            for (cmd in registerCommands) {
                this.server.commandMap.register(cmd, RegisterCommand(cmd))
            }
            for (cmd in loginCommands) {
                this.server.commandMap.register(cmd, LoginCommand(cmd))
            }
            for (cmd in changeCommands) {
                this.server.commandMap.register(cmd, ChangePasswordCommand(cmd))
            }
            this.server.scheduler.scheduleAsyncTask(this, object : AsyncTask() {
                override fun onRun() {
                    val tick = MainConfig.free_login_time / 1000
                    while (true) {
                        try {
                            Thread.sleep(1000)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                        for (name in freeLogin.keys) {
                            val time = freeLogin[name]!!
                            if (time.plus(1) >= tick) {
                                freeLogin.remove(name)
                            } else {
                                freeLogin[name] = time + 1
                            }
                        }
                    }
                }
            })
            this.server.pluginManager.registerEvents(NormalListener(), this)
            this.server.pluginManager.registerEvents(ActionListener(), this)
            this.server.logger.info(TITLE + "启动成功。")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDisable() {
        this.server.logger.info(TITLE + "关闭成功。")
    }

    fun openRegister(player: NukkitPlayer) {
        this.server.scheduler.scheduleDelayedTask(this, {
            registerGUIer.add(player.name)
            player.showGUI(registerGUI)
        }, 20)

    }

    fun openLogin(player: NukkitPlayer) {
        this.server.scheduler.scheduleDelayedTask(this, {
            loginGUIer.add(player.name)
            player.showGUI(loginGUI)
        }, 20)
    }

    fun showTitle(player: NukkitPlayer) {
        this.server.scheduler.scheduleDelayedTask(this, {
            val notice = MainConfig.joinNotice + ""
            val gg = MainConfig.joinAnnouncement + ""
            this.server.broadcastMessage(gg.replace("%ServerTITLE", SERVER_TITLE).replace("%name", player.name))
            player.sendMessage(notice.replace("%ServerTITLE", SERVER_TITLE).replace("%name", player.name))
            if (MainConfig.title["启用"] as Boolean) {
                player.setTitleAnimationTimes(20, 50, 20)
                player.setSubtitle(MainConfig.title["第二行"].toString().replace("%ServerTITLE", SERVER_TITLE).replace("%name", player.getName()))
                player.sendTitle(MainConfig.title["第一行"].toString().replace("%ServerTITLE", SERVER_TITLE).replace("%name", player.getName()))
                //player.sendTitle(MainConfig.getTitle().get("第一行").toString().replace("%ServerTITLE", SERVER_TITLE).replace("%name", player.getName()), MainConfig.getTitle().get("第二行").toString().replace("%ServerTITLE", SERVER_TITLE).replace("%name", player.getName()), 20, 50, 20);
            }
        }, 40)
    }

    companion object {
        const val TITLE = "§l§7|§bS§a登§c录§7| §6"
        lateinit var instance: SLogin
            private set
    }
}
