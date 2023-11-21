package net.mcpes.foundhi.plugin.currency.login.config

import cn.nukkit.utils.Config
import net.mcpes.foundhi.plugin.currency.login.SLogin
import java.io.File
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/26
 * version 1.0
 */
object MainConfig {
    lateinit var config: Config
        private set
    lateinit var joinAnnouncement: String
        private set
    lateinit var joinNotice: String
        private set
    lateinit var title: HashMap<String, Any>
        private set
    var isCanSteve: Boolean = false
        private set
    var free_login_time: Int = 0
        private set

    fun init() {
        config = Config(File(SLogin.instance.dataFolder.toString() + "/config.yml"))
        isCanSteve = (config.get("玩家名称限制") as HashMap<String, Any>)["允许Steve"] as Boolean
        title = config.get("进服Title") as HashMap<String, Any>
        joinAnnouncement = config.getString("进服公告")
        joinNotice = config.getString("进服提醒")
        free_login_time = config.getInt("免登录") * 60 * 1000
        SLogin.instance.registerCommands = MainConfig.config.get("注册指令") as ArrayList<String>
        SLogin.instance.loginCommands = MainConfig.config.get("登录指令") as ArrayList<String>
        SLogin.instance.changeCommands = MainConfig.config.get("改密码指令") as ArrayList<String>
    }
}
