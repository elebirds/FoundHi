package net.mcpes.foundhi.core.plugin

import cn.nukkit.Nukkit
import cn.nukkit.Nukkit.PATH
import cn.nukkit.Server
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.plugin.PluginBase
import cn.nukkit.scheduler.AsyncTask
import cn.nukkit.utils.Config
import cn.nukkit.utils.ConfigSection
import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.ServerType.LOGIN
import net.mcpes.foundhi.core.ServerType.NORMAL
import net.mcpes.foundhi.core.dao.Database
import net.mcpes.foundhi.core.handle.Handle
import net.mcpes.foundhi.core.handle.bag.LoadBagHandle
import net.mcpes.foundhi.core.network.Network
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.player.PlayerTransferPacket
import net.mcpes.foundhi.core.network.protocol.server.ServerDisconnectPacket
import net.mcpes.foundhi.core.network.protocol.server.UpdateServerPerformanceData
import net.mcpes.foundhi.core.plugin.banned.PlayerBannedData
import net.mcpes.foundhi.core.plugin.command.defaults.*
import net.mcpes.foundhi.core.plugin.data.GuildCache
import net.mcpes.foundhi.core.plugin.data.MailCache
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.core.plugin.data.PlayerCache
import net.mcpes.foundhi.core.plugin.gui.NukkitGraphicalUserInterface
import net.mcpes.foundhi.core.plugin.gui.listener.GUIListener
import net.mcpes.foundhi.core.plugin.gui.window.lambda.defaults.LambdaTipGUI
import net.mcpes.foundhi.core.plugin.listener.login.LoginServerListener
import net.mcpes.foundhi.core.plugin.listener.normal.ChatListener
import net.mcpes.foundhi.core.plugin.listener.normal.NormalListener
import net.mcpes.foundhi.core.plugin.task.ServerCommandTask
import net.mcpes.foundhi.core.plugin.team.TeamFunction
import net.mcpes.foundhi.core.utils.FileUtils
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/25 下午10:27
 * version 1.0
 */
class FoundHiPlugin : PluginBase() {
    private var willJoin = CopyOnWriteArrayList<String>()
    private var players = ConcurrentHashMap<String, PlayerCache>()
    private var guilds = ConcurrentHashMap<Int, GuildCache>()
    private var mails = HashMap<String, MailCache>()
    var teamInvitation = HashMap<String, Int>()
    var overTime: Long = 0
    var overCount: Int = 0
    private val commonGUIs = HashMap<String, NukkitGraphicalUserInterface>()
    private var handles = HashMap<Long, Handle>()
    private var loadBagHandles = HashMap<String, LoadBagHandle>()
    var count: Int = 0
    lateinit var money1Top: HashMap<String, Double>
    lateinit var money2Top: HashMap<String, Double>
    lateinit var money3Top: HashMap<String, Double>
    lateinit var willKick: CopyOnWriteArrayList<String>
    lateinit var bannedPlayerInfos: HashMap<Int, PlayerBannedData>
    lateinit var playerBannedInfos: HashMap<String, ArrayList<PlayerBannedData>>
    lateinit var playerEnabledBannedInfos: HashMap<String, ArrayList<PlayerBannedData>>
    private lateinit var fConfig: Config
    lateinit var id: String
        private set
    lateinit var serverName: String
        private set
    lateinit var serverAPIIP: String
        private set
    var serverAPIPort: Int = 0
        private set
    lateinit var network: Network
        private set
    lateinit var teamFunction: TeamFunction
        private set
    lateinit var serverIP: String
        private set
    var serverPort: Int = 0
        private set
    var isDebug = false
        private set
    var isUseCBag: Boolean = false
        private set
    lateinit var cBagID: String
        private set
    private var proved = false

    fun setProved(proved: Boolean) {
        this.proved = proved
    }

    fun dataPacket(pk: DataPacket?, force: Boolean) {
        if (this.proved || force) this.network.pluginListener.dataPacket(pk)
    }

    fun dataPacket(pk: DataPacket?) {
        this.dataPacket(pk, false)
    }

    fun getMoney(name: String, type: Int): Double {
        if (!players.containsKey(name)) return 0.0
        var m = 0.0
        when (type) {
            1 -> m = players[name]!!.money1
            2 -> m = players[name]!!.money2
            3 -> m = players[name]!!.money3
        }
        return m
    }

    override fun onLoad() {
        instance = this
        willKick = CopyOnWriteArrayList()
        playerBannedInfos = HashMap()
        bannedPlayerInfos = HashMap()
        playerEnabledBannedInfos = HashMap()
        FileUtils.saveResource("database.properties", File(Nukkit.DATA_PATH + "/database.properties"), false)
        FileUtils.saveResource("redis.yml", File(Nukkit.DATA_PATH + "/redis.yml"), false)
        this.logger.info("加载 foundhi.yml ...")
        this.fConfig = Config(PATH + "foundhi.yml", Config.YAML, object : ConfigSection() {
            init {
                put("启用", false)
                put("服务器id", "")
                put("服务器名称", "")
                put("API-IP", "localhost")
                put("API-Port", 23333)
                put("API服务器密钥", "123456")
                put("服务器IP", "localhost")
                put("服务器端口", 21356)
                put("debug", false)
                put("服务器类型", NORMAL)
                put("使用云背包", false)
                put("云背包ID", null)
            }
        })
        if (!this.fConfig.getBoolean("启用")) this.server.pluginManager.disablePlugin(this)
        SERVER_SECRET_KEY = this.fConfig.getString("API服务器密钥")
        this.logger.info("初始化服务器类型")
        ServerType.init(this.fConfig.getInt("服务器类型"))
        this.id = this.fConfig.getString("服务器id")
        this.serverName = this.fConfig.getString("服务器名称")
        this.serverAPIIP = this.fConfig.getString("API-IP")
        this.serverAPIPort = this.fConfig.getInt("API-Port")
        this.serverIP = this.fConfig.getString("服务器IP")
        this.serverPort = this.fConfig.getInt("服务器端口")
        this.isUseCBag = this.fConfig.getBoolean("使用云背包", false)
        this.cBagID = this.fConfig.getString("云背包ID")
        this.isDebug = this.fConfig.getBoolean("debug", false)
        this.initGUI()
        this.teamFunction = TeamFunction()
    }

    override fun onEnable() {
        this.logger.info("监听端口....")
        try {
            Database.init()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        this.network = Network()
        this.server.pluginManager.registerEvents(NormalListener(), this)
        this.server.pluginManager.registerEvents(GUIListener(), this)
        if (ServerType.getType() != LOGIN) {
            this.server.commandMap.register("FoundHi", MoneyCommand("money"))
            this.server.commandMap.register("FoundHi", AdminCommand("admin"))
            this.server.commandMap.register("FoundHi", MyCommand("my"))
            this.server.commandMap.register("FoundHi", GuildCommand("guild"))
            this.server.commandMap.register("FoundHi", MarryCommand("marry"))
            this.server.commandMap.register("FoundHI", FriendCommand("friend"))
        }
        this.server.scheduler.scheduleAsyncTask(this, object : AsyncTask() {
            override fun onRun() {
                while (true) {
                    try {
                        Thread.sleep(10000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    val pk = UpdateServerPerformanceData()
                    pk.id = FoundHiPlugin.instance.id
                    pk.count = FoundHiPlugin.instance.server.onlinePlayers.values.size
                    pk.tps = FoundHiPlugin.instance.server.ticksPerSecond
                    pk.upload = FoundHiPlugin.instance.server.network.upload
                    pk.download = FoundHiPlugin.instance.server.network.download
                    pk.load = FoundHiPlugin.instance.server.tickUsage
                    if (FoundHiPlugin.instance.network.pluginListener.channel != null && FoundHiPlugin.instance.network.pluginListener.channel.isOpen && FoundHiPlugin.instance.network.pluginListener.channel.isActive) {
                        FoundHiPlugin.instance.dataPacket(pk)
                    }
                }
            }
        })
        this.server.scheduler.scheduleRepeatingTask(ServerCommandTask(), 2)
        this.server.scheduler.scheduleRepeatingTask(this, {
            val n = System.currentTimeMillis() - FoundHiPlugin.instance.overTime
            if (n > 15 * 1000) {
                FoundHiPlugin.instance.overCount += 1
                if (FoundHiPlugin.instance.overCount >= 3) {
                    Server.getInstance().logger.info("与API服务器连接超时,自动断开连接!")
                    Server.getInstance().scheduler.scheduleDelayedTask(FoundHiPlugin.instance, {
                        FoundHiPlugin.instance.network.pluginListener.restart()
                    }, 200)
                }
            } else {
                if (FoundHiPlugin.instance.overCount >= 0) {
                    FoundHiPlugin.instance.overTime = System.currentTimeMillis()
                }
                FoundHiPlugin.instance.overCount = 0
            }
        }, 200)
        when (ServerType.getType()) {
            LOGIN -> this.server.pluginManager.registerEvents(LoginServerListener(), this)
        }
        if (ServerType.getType() != LOGIN) {
            this.server.pluginManager.registerEvents(ChatListener(), this)
            this.server.scheduler.scheduleRepeatingTask(this, {
                for (name in FoundHiPlugin.instance.willKick) {
                    if (players.containsKey(name)) {
                        players[name]!!.kick()
                    }
                }
            }, 20)
            this.server.commandMap.register("FoundHi", object : Command("transfer") {
                override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
                    if (strings.size != 1) {
                        sender.sendMessage("§c未知的指令。请使用 /help 来显示指令列表。")
                    } else {
                        if (sender !is NukkitPlayer) return true
                        sender.sendBagTag()
                        val pk = PlayerTransferPacket()
                        pk.player_id = sender.getName()
                        pk.source = FoundHiPlugin.instance.id
                        pk.server_id = strings[0]
                        FoundHiPlugin.instance.dataPacket(pk)
                        sender.sendMessage("§a将你转移到:" + strings[0])
                    }
                    return true
                }
            })
        }
    }

    override fun onDisable() {
        val pk = ServerDisconnectPacket()
        pk.id = this.id
        this.dataPacket(pk)
    }

    fun addPlayer(playerCache: PlayerCache) {
        players[playerCache.name] = playerCache
    }

    fun delPlayer(id: String) = this.players.remove(id)

    fun getPlayer(id: String) = this.players[id]

    fun isPlayerOnline(id: String) = this.players.containsKey(id)

    fun getPlayers() = this.players.values

    fun getPlayerCount() = this.players.size

    fun getGuild(id: Int) = this.guilds[id]

    fun hasGuild(id: Int) = this.guilds.containsKey(id)

    fun addGuild(guild: GuildCache) = this.guilds.put(guild.id, guild)

    fun removeGuild(guild: GuildCache) = this.guilds.remove(guild.id)

    fun removeGuild(guild: Int) = this.guilds.remove(guild)

    fun addHandle(id: Long, handle: Handle) {
        this.handles[id] = handle
    }

    fun removeHandle(id: Long) = this.handles.remove(id)

    fun getHandle(id: Long) = this.handles[id]

    fun addWillJoin(id: String) {
        this.willJoin.add(id)
    }

    fun delWillJoin(id: String) = this.willJoin.remove(id)

    fun hasWillJoin(id: String) = this.willJoin.contains(id)

    fun hasMail(id: String) = this.mails.containsKey(id)

    fun getMail(id: String) = this.mails[id]

    fun addMail(mailCache: MailCache) {
        this.mails[mailCache.id] = mailCache
    }

    fun addLoadBagHandle(loadBagHandle: LoadBagHandle) {
        loadBagHandles[loadBagHandle.name] = loadBagHandle
    }

    fun getAndRemoveLoadBagHandle(name: String) = this.loadBagHandles.remove(name)

    private fun initGUI() {
        commonGUIs["common_error_param"] = LambdaTipGUI("foundhi_plugin_common_tip_param_error", "§a§l操作失败:未知参数错误!")
        commonGUIs["common_error_idto"] = LambdaTipGUI("foundhi_plugin_common_tip_i_don't_online", "§a§l操作失败:出现未知错误,请重新进入服务器再次尝试!")
    }

    fun getCommonGUI(id: String) = this.commonGUIs[id]

    fun addCommonGUI(id: String, gui: NukkitGraphicalUserInterface) {
        this.commonGUIs[id] = gui
    }

    companion object {
        const val SERVER_TITLE = "§l§bS§au§dm§em§4i§6t"
        lateinit var SERVER_SECRET_KEY: String
        @JvmStatic
        lateinit var instance: FoundHiPlugin
            private set
    }
}
