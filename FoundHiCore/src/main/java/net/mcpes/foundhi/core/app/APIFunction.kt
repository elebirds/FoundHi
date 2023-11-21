package net.mcpes.foundhi.core.app

import net.mcpes.foundhi.core.app.command.CommandSender
import net.mcpes.foundhi.core.app.command.SimpleCommandMap
import net.mcpes.foundhi.core.app.data.banned.BannedInfo
import net.mcpes.foundhi.core.app.data.guild.Guild
import net.mcpes.foundhi.core.app.data.mail.Mail
import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.app.data.server.NoProveServer
import net.mcpes.foundhi.core.app.data.server.ProvenServer
import net.mcpes.foundhi.core.app.data.server.Server
import net.mcpes.foundhi.core.app.task.GetMoneyTopTask
import net.mcpes.foundhi.core.dao.Database
import net.mcpes.foundhi.core.dao.DatabaseQueue
import net.mcpes.foundhi.core.dao.banned.BannedPlayerDatabase
import net.mcpes.foundhi.core.dao.guild.GuildDatabase
import net.mcpes.foundhi.core.dao.mail.MailDatabase
import net.mcpes.foundhi.core.dao.player.*
import net.mcpes.foundhi.core.network.Network
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.banned.AddPlayerBannedPacket
import net.mcpes.foundhi.core.network.protocol.server.ServerDisconnectPacket
import net.mcpes.foundhi.core.network.protocol.server.UpdateServerPerformanceData
import net.mcpes.foundhi.core.network.server.listener.ServerListener
import net.mcpes.foundhi.core.network.server.listener.ServerListener.overCount
import net.mcpes.foundhi.core.network.server.listener.ServerListener.overTime
import net.mcpes.foundhi.core.scheduler.AsyncTask
import net.mcpes.foundhi.core.scheduler.ServerScheduler
import net.mcpes.foundhi.core.team.TeamSystem
import net.mcpes.foundhi.core.utils.*
import java.io.File
import java.sql.SQLException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.max

/**
 * FoundHi API部分主体
 *
 * @author hhm Copyright (c) 2018/4/10
 * @version 1.0
 */

class APIFunction
/**
 * APIFunction 构造函数
 * 初始化变量并启动API服务器(即调用`start`方法)。
 */
@Throws(Exception::class)
constructor(
        /**
         * 记录器
         */
        val logger: MainLogger,
        /**
         * 运行目录
         */
        private val PATH: String,
        /**
         * 数据保存目录
         */
        val dataFolder: String) {
    private var nextTick: Long = 0
    /**
     * 运行时间
     */
    private var tickCounter: Int = 0
    /**
     * 是否运行
     */
    var isRunning = true
        private set
    /**
     * 服务器主配置文件
     */
    lateinit var config: Config
        private set
    /**
     * 网络管理对象
     */
    lateinit var network: Network
        private set
    /**
     * 命令管理对象
     */
    lateinit var commandMap: SimpleCommandMap
        private set
    /**
     * 最优大厅
     */
    var bestHall: String = ""
    /**
     * 任务管理对象
     */
    lateinit var scheduler: ServerScheduler
        private set
    /**
     * 小队系统
     */
    lateinit var teamSystem: TeamSystem
        private set
    /**
     * API服务器监听端口
     */
    var serverAPIPort: Int = 0
    /**
     * 服务器列表
     */
    private var servers = ConcurrentHashMap<String, Server>()
    /**
     * 未验证服务器列表
     */
    private var proveServers = ConcurrentHashMap<String, NoProveServer>()
    /**
     * 被ban玩家数据类的实例
     */
    private var bannedPlayerInfos = ConcurrentHashMap<Int, BannedInfo>()
    /**
     * 以被ban玩家名作为key的被ban实例
     */
    private var playerBannedInfos = ConcurrentHashMap<String, ArrayList<BannedInfo>>()
    /**
     * 大厅服务器列表
     */
    private var hubs = CopyOnWriteArrayList<String>()
    /**
     * 玩家列表
     */
    private var allPlayers = CopyOnWriteArrayList<String>()
    /**
     * 登录玩家的信息
     */
    private var players = ConcurrentHashMap<String, Player>()
    /**
     * 退出30秒内玩家的信息
     */
    private var exitedPlayers = ConcurrentHashMap<String, Player>()
    /**
     * 玩家保存任务
     */
    private var saveTask = ConcurrentHashMap<String, Int>()
    /**
     * 公会列表
     */
    private var guilds = ConcurrentHashMap<Int, Guild>()
    /**
     * 邮件列表
     */
    private var mails = ConcurrentHashMap<String, Mail>()
    /**
     * 跨服聊天
     */
    private var uniteChats = ConcurrentHashMap<String, CopyOnWriteArrayList<String>>()
    /**
     * 服务器连接密钥
     */
    lateinit var serverSecretKey: String
    /**
     * 调试开关
     */
    var debug: Boolean = false
    /**
     * 更新最优大厅时间
     */
    var updateBestHall: Int = 0
    /**
     * 服务器人数限制
     */
    var serverPlayerCount: Int = 0
    /**
     * 货币1排行榜
     */
    lateinit var money1Top: HashMap<String, Double>
    /**
     * 货币2排行榜
     */
    lateinit var money2Top: HashMap<String, Double>
    /**
     * 货币3排行榜
     */
    lateinit var money3Top: HashMap<String, Double>
    /**
     * 货币1排行榜-String
     */
    var money1TopS: String = ""
    /**
     * 货币2排行榜-String
     */
    var money2TopS: String = ""
    /**
     * 货币3排行榜-String
     */
    var money3TopS: String = ""

    init {
        instance = this
        this.start()
    }

    /**
     * FoundHi API部分 启动方法
     */
    @Throws(Exception::class)
    private fun start() {
        this.scheduler = ServerScheduler()
        this.commandMap = SimpleCommandMap(this)
        this.scheduler.scheduleAsyncTask(logger)
        FileUtils.saveResource("database.properties", "database.properties", false)
        FileUtils.saveResource("redis.yml", "redis.yml", false)
        if (!File(PATH + "foundhi.yml").exists()) {
            //初始化
            this.init()
        } else {
            this.logger.info("加载 foundhi.yml ...")
            this.config = Config(PATH + "foundhi.yml", Config.YAML, object : ConfigSection() {
                init {
                    put("监听端口", 23333)
                    put("服务器密钥", "123456")
                    put("debug", false)
                    put("更新best大厅时间", 1000)
                    put("服务器人数限制", -1)
                }
            })
            serverPlayerCount = this.config.getInt("服务器人数限制")
            updateBestHall = this.config.getInt("更新best大厅时间")
            serverAPIPort = this.config.getInt("监听端口")
            serverSecretKey = this.config.getString("服务器密钥")
            debug = this.config.getBoolean("debug")
        }
        this.teamSystem = TeamSystem()
        this.logger.info("初始化数据库连接池...")
        Database.init()
        this.logger.info("初始化数据库...")
        DatabaseQueue()
        PlayerBaseDatabase.newTable()
        GuildDatabase.newTable()
        PlayerGuildDatabase.newTable()
        PlayerFriendsDatabase.newTable()
        PlayerMailDatabase.newTable()
        BannedPlayerDatabase.newTable()
        BannedPlayerDatabase.load()
        Guild.load()
        PlayerDatabase.loadAllPlayers()
        network = Network()
        this.logger.info("开始监听服务器")
        this.logger.info("注册任务...")
        this.scheduler.scheduleAsyncTask(object : AsyncTask() {
            override fun onRun() {
                while (APIFunction.instance.isRunning) {
                    try {
                        MailDatabase.setDatabaseName()
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }

                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
            }
        })
        this.scheduler.scheduleRepeatingTask({
            val count = HashMap<String, Int>()
            for (id in APIFunction.instance.hubs) {
                val server = APIFunction.instance.getServer(id)
                if (server != null) {
                    count[id] = server.count
                }
            }
            if (count.size != 0) APIFunction.instance.bestHall = count.bubbleAscendingSort()[0].key.toString()
        }, this.updateBestHall * 20)
        this.scheduler.scheduleRepeatingTask({
            for (channel in overTime.keys) {
                val time = overTime[channel]!!
                val n = System.currentTimeMillis() - time
                if (n > 15 * 1000) {
                    overCount[channel] = overCount.getOrDefault(channel, 0) + 1
                    if (overCount[channel]!! >= 3) {
                        for (server in APIFunction.instance.getServers()) {
                            if (server.channel === channel) {
                                APIFunction.instance.logger.info("服务器" + server.name + "(id:" + server.id + ")连接超时,自动断开连接!")
                                server.shutdown(true)
                                continue
                            }
                        }
                    } else {
                        overTime[channel] = System.currentTimeMillis()
                    }
                }
            }
        }, 200)
        this.scheduler.scheduleAsyncTask(GetMoneyTopTask())
        this.scheduler.scheduleRepeatingTask({
            APIFunction.instance.ping()
        }, 200)
        this.logger.info("此程序正在运行 FoundHi " + TextFormat.YELLOW + FoundHiAPIMain.VERSION + " 版本\"" + TextFormat.AQUA + FoundHiAPIMain.CODENAME + "\"")
        this.logger.info("启动完成（" + ((System.currentTimeMillis() - FoundHiAPIMain.START_TIME).toDouble() / 1000).toString() + "s）!")
        this.tickCounter = 0
        this.nextTick = System.currentTimeMillis()
        this.tickProcessor()
    }

    private fun tickProcessor() {
        this.nextTick = System.currentTimeMillis()
        try {
            while (this.isRunning) {
                try {
                    this.tick()
                    val next = this.nextTick
                    val current = System.currentTimeMillis()
                    if (next - 0.1 > current) {
                        val allocated = next - current - 1
                        if (allocated > 0) {
                            Thread.sleep(allocated, 900000)
                        }
                    }
                } catch (e: RuntimeException) {
                    this.logger.logException(e)
                }
            }
        } catch (e: Throwable) {
            this.logger.critical("Exception happened while ticking server")
            this.logger.alert(Utils.getExceptionMessage(e))
            this.logger.alert(Utils.getAllThreadDumps())
        }
    }

    private fun tick(): Boolean {
        val tickTime = System.currentTimeMillis()
        val sleepTime = tickTime - this.nextTick
        if (sleepTime < -25) {
            try {
                Thread.sleep(max(5, -sleepTime - 25))
            } catch (e: InterruptedException) {
                this.logger.logException(e)
            }
        }
        if (tickTime - this.nextTick < -25) {
            return false
        }
        this.tickCounter++
        this.scheduler.mainThreadHeartbeat(this.tickCounter)
        if (this.nextTick - tickTime < -1000) {
            this.nextTick = tickTime
        } else {
            this.nextTick += 50
        }
        return true
    }

    /**
     * FoundHi API部分 关闭方法。
     */
    fun shutdown() {
        this.logger.info("正在关闭FoundHi...\n")
        for (player in players.values) {
            player.save()
        }
        for (server in servers.values) {
            server.shutdown(true)
        }
        var i = 0
        val start = System.currentTimeMillis()
        for (id in guilds.keys) {
            try {
                guilds[id]?.save()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

            guilds.remove(id)
            i++
        }
        for (banned in bannedPlayerInfos.values) {
            banned.save()
        }
        this.logger.info("成功保存" + i + "个公会信息!耗时" + (System.currentTimeMillis() - start) / 1000 + "s")
        this.network.serverListener.shutdown()
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        this.scheduler.cancelAllTasks()
        this.scheduler.mainThreadHeartbeat(Integer.MAX_VALUE)
        this.isRunning = false
        this.commandMap.reader.shutdown()
        Database.getDruidDataSource().close()
        if (Database.isEnableRedis()) Database.getJedisPool().close()
        this.logger.debug("Stopping all tasks")
        System.exit(0)
    }

    /**
     * 执行API端命令
     *
     * @param commandLine String 命令<br></br>
     * @param sender      CommandSender 命令执行者
     */
    fun dispatchCommand(sender: CommandSender?, commandLine: String): Boolean {
        if (sender == null) {
            return false
        }
        if (this.commandMap.dispatch(sender, commandLine)) {
            return true
        }
        sender.sendMessage("§c未知的指令。请使用 /help 来显示指令列表。")
        return false
    }

    /**
     * 初始化FoundHi
     * 第一次运行使用
     */
    private fun init() {
        this.logger.info("欢迎使用FoundHi " + TextFormat.YELLOW + FoundHiAPIMain.VERSION + " 版本\"" + TextFormat.AQUA + FoundHiAPIMain.CODENAME + "\"")
        this.logger.info("开始初始化...")
        this.logger.info("默认语言: chs - 中文·简体")
        var step = 0
        this.logger.info("请输入FoundHi连接密钥(默认:123456)")
        var databaseIP = ""
        var databaseName = ""
        var databaseUser = ""
        var databasePassword = ""
        var con = true
        while (con) {
            val line = readLine()
            when (step) {
                0 -> {
                    serverSecretKey = line ?: "123456"
                    this.logger.info("成功设置连接密钥为:$serverSecretKey")
                    this.logger.info("请输入FoundHi监听端口(默认:23333)")
                    step++
                }
                1 -> {
                    try {
                        if (line == null) throw NumberFormatException()
                        serverAPIPort = Integer.parseInt(line)
                        this.logger.info("成功设置监听端口为:$serverAPIPort")
                    } catch (e: NumberFormatException) {
                        serverAPIPort = 23333
                        this.logger.info("已经设置监听端口为默认:23333")
                    }
                    this.logger.info("请输入更新最佳大厅时间(默认:10,单位:s)")
                    step++
                }
                2 -> {
                    try {
                        if (line == null) throw NumberFormatException()
                        updateBestHall = Integer.parseInt(line)
                        this.logger.info("成功设置更新最佳大厅时间为:$updateBestHall")
                    } catch (e: NumberFormatException) {
                        updateBestHall = 10
                        this.logger.info("已经设置更新最佳大厅时间为默认:10")
                    }

                    this.logger.info("请输入服务器人数限制(默认:-1,-1为无限)")
                    step++
                }
                3 -> {
                    try {
                        if (line == null) throw NumberFormatException()
                        serverPlayerCount = Integer.parseInt(line)
                        if (serverPlayerCount < -1 || serverPlayerCount == 0) throw NumberFormatException()
                        this.logger.info("成功设置服务器人数限制为:$serverPlayerCount")
                    } catch (e: NumberFormatException) {
                        serverPlayerCount = -1
                        this.logger.info("已经设置服务器人数限制为默认:-1")
                    }
                    this.logger.info("请输入是否开启DEBUG调试模式(true/false)(默认false)")
                    step++
                }
                4 -> {
                    debug = try {
                        if (line == null) throw Exception()
                        val s = line.toString().toBoolean()
                        this.logger.info("成功设置DEBUG调试模式为:" + if (s) "开启" else "关闭")
                        s
                    } catch (e: Exception) {
                        this.logger.info("已经设置DEBUG调试模式为默认:关闭")
                        false
                    }

                    this.logger.info("主配置文件设置完成!")
                    this.logger.info("开始设置MySQL数据库信息!")
                    this.logger.info("请输入数据库IP与端口(格式:xxx.xxx.xxx.xxx:port)(默认:127.0.0.1:3306)")
                    step++
                }
                5 -> {
                    databaseIP = try {
                        if (line == null) throw Exception()
                        if (!line.matches("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?[1-9])))(:\\d{1,5}))\n".toRegex()))
                            throw Exception()
                        line
                    } catch (e: Exception) {
                        "127.0.0.1:3306"
                    }
                    this.logger.info("成功设置数据库IP与端口为:$databaseIP")
                    this.logger.info("请输入数据库名称(默认:foundhi)")
                    step++
                }
                6 -> {
                    databaseName = try {
                        if (line == null) throw Exception()
                        line
                    } catch (e: Exception) {
                        "foundhi"
                    }
                    this.logger.info("成功设置数据库名称为:$databaseName")
                    this.logger.info("请输入数据库用户名(默认:root)")
                    step++
                }
                7 -> {
                    databaseUser = try {
                        if (line == null) throw Exception()
                        line
                    } catch (e: Exception) {
                        "root"
                    }
                    this.logger.info("成功设置数据库用户名为:$databaseUser")
                    this.logger.info("请输入数据库密码(默认:123456)")
                    step++
                }
                8 -> {
                    databasePassword = try {
                        if (line == null) throw Exception()
                        line
                    } catch (e: Exception) {
                        "123456"
                    }
                    this.logger.info("成功设置数据库密码为:$databasePassword")
                    step++
                    con = false
                }
            }
        }
        this.logger.info("配置完毕,正在生成配置文件...")
        this.config = Config(PATH + "foundhi.yml", Config.YAML, object : ConfigSection() {
            init {
                put("监听端口", serverAPIPort)
                put("服务器密钥", serverSecretKey)
                put("debug", debug)
                put("更新best大厅时间", updateBestHall)
                put("服务器人数限制", serverPlayerCount)
            }
        })
        config.save()
        val dbc = Config(dataFolder + "database.properties")
        dbc.set("username", databaseUser)
        dbc.set("url", "jdbc:mysql://$databaseIP/$databaseName?useSSL=true")
        dbc.set("password", databasePassword)
        dbc.save()
        this.logger.info("初始化成功!")
    }

    /**
     * 保存玩家信息，一个API接口
     *
     * @param player Player 玩家
     */
    fun savePlayer(player: Player) {
        player.save()
    }

    /**
     * 广播Packet到各个服务器
     *
     * @param pk DataPacket 数据包
     */
    fun broadcastPacket(pk: DataPacket) {
        broadcastPacket(servers.values, pk)
    }

    /**
     * 广播Packet到指定服务器
     *
     * @param pk      DataPacket 数据包 <br></br>
     * @param servers Collection&lt;Server&gt; 服务器列表
     */
    fun broadcastPacket(servers: Collection<Server>, pk: DataPacket) {
        broadcastPacket(servers.toTypedArray(), pk)
    }

    /**
     * 广播Packet到指定服务器
     *
     * @param pk      DataPacket 数据包 <br></br>
     * @param servers Server[] 服务器列表
     */
    fun broadcastPacket(servers: Array<Server>, pk: DataPacket) {
        for (server in servers) {
            server.dataPacket(pk)
        }
    }

    fun getPlayer(id: String) = this.players[id]

    fun addPlayer(id: String) {
        if (this.isPlayerOnline(id)) return
        do {
            if (this.exitedPlayers.containsKey(id)) {
                players[id] = exitedPlayers.remove(id) ?: continue
                exitedPlayers.remove(id)
                saveTask[id]?.let { this.scheduler.cancelTask(it) }
                saveTask.remove(id)
                APIFunction.instance.logger.info("玩家 " + id + " 加入了Summit服务器,现在人数(${players.size}/${this.serverPlayerCount})")
                return
            }
        } while (false)
        this.players[id] = Player(id)
        APIFunction.instance.logger.info("玩家 " + id + " 加入了Summit服务器,现在人数(${players.size}/${this.serverPlayerCount})")
    }

    @JvmOverloads
    fun delPlayer(id: String, kicked: Boolean = false) {
        val player = this.players.remove(id) ?: return
        this.exitedPlayers[id] = player
        saveTask[id] = this.scheduler.scheduleDelayedTask({
            player.save()
            this.alreadySavePlayerData(player.name)
        }, 600).taskId
        if (!kicked) this.logger.info("玩家 $id 退出了服务器,现在人数(${players.size}/${this.serverPlayerCount})")
    }

    @JvmOverloads
    fun kickPlayer(player: Player, reason: String = "无原因") {
        this.delPlayer(player.name, true)
        this.logger.info("玩家 ${player.name} 被踢出了服务器,原因:$reason,现在人数(${players.size}/${this.serverPlayerCount}")
    }

    fun alreadySavePlayerData(id: String) {
        this.saveTask.remove(id)
        this.exitedPlayers.remove(id)
    }

    fun isPlayerOnline(id: String) = this.players.containsKey(id)

    fun hasPlayer(id: String) = this.allPlayers.contains(id)

    fun getPlayers() = this.players.values

    fun getPlayerCount() = this.players.size

    fun addProvenServer(provenServer: ProvenServer) = this.servers.put(provenServer.id, provenServer)

    fun getServer(id: String) = this.servers[id]

    fun getServers() = this.servers.values

    fun getServerCount() = this.servers.size

    fun isProveServer(id: String) = this.proveServers.containsKey(id)

    fun getProveServer(id: String) = this.proveServers[id]

    fun addProveServer(proveServer: NoProveServer) {
        this.proveServers[proveServer.id] = proveServer
    }

    fun sendBannedData(server: Server) {
        if (!Database.isEnableRedis()) {
            val sban = AddPlayerBannedPacket()
            sban.data = hashMapOf()
            bannedPlayerInfos.forEach {
                sban.data[it.key.toString()] = it.value.toString()
            }
            server.dataPacket(sban)
        }
    }

    fun toProvenServer(noProveServer: NoProveServer) {
        if (!noProveServer.verification) return
        val server = ProvenServer(noProveServer.id, noProveServer.name, noProveServer.server_ip, noProveServer.server_port, noProveServer.channel, noProveServer.cBagID, noProveServer.type)
        this.proveServers.remove(noProveServer.id)
        this.addProvenServer(server)
    }

    fun serverShutdown(server: Server, stopServer: Boolean) {
        if (server is NoProveServer) {
            if (stopServer) {
                for (al in this.uniteChats.values) {
                    al.remove(server.id)
                }
                ServerListener.overTime.remove(server.channel)
                ServerListener.overCount.remove(server.channel)
                this.proveServers.remove(server.id)
                server.channel.close()
                this.logger.notice("与未经验证的服务器:${server.name}(id:${server.id})的连接已经关闭!")
                return
            }
            this.logger.notice("正在与未经验证的服务器:${server.name}(id:${server.id})重新建立连接!")
        } else {
            if (stopServer) {
                for (player in players.values) {
                    if (player.inServer == server.id) {
                        exitedPlayers[player.name] = players[player.name]!!
                        saveTask[player.name] = this.scheduler.scheduleDelayedTask({
                            player.save()
                            this.alreadySavePlayerData(player.name)
                        }, 600).taskId
                        players.remove(player.name)
                    }
                }
                for (al in this.uniteChats.values) {
                    al.remove(server.id)
                }
                val pk = ServerDisconnectPacket()
                pk.id = server.id
                server.dataPacket(pk)
                this.servers.remove(server.id)
                server.channel.close()
                ServerListener.overTime.remove(server.channel)
                ServerListener.overCount.remove(server.channel)
                this.logger.notice("与服务器:${server.name}(id:${server.id})的连接已经关闭!")
                return
            }
            this.logger.notice("正在与服务器:${server.name}(id:${server.id})重新建立连接!")
        }
    }

    fun isServerOnline(id: String) = this.servers.containsKey(id)

    fun getGuild(id: Int) = this.guilds[id]

    fun hasGuild(id: Int) = this.guilds.containsKey(id)

    fun addGuild(guild: Guild) = this.guilds.put(guild.id, guild)

    fun removeGuild(guild: Guild) = this.guilds.remove(guild.id)

    fun hasBanned(id: Int) = this.bannedPlayerInfos.containsKey(id)

    fun getBanned(id: Int) = this.bannedPlayerInfos[id]

    fun addBanned(bannedInfo: BannedInfo) {
        this.bannedPlayerInfos[bannedInfo.id] = bannedInfo
        synchronized(bannedInfo.banned) {
            if (playerBannedInfos.containsKey(bannedInfo.banned)) {
                playerBannedInfos[bannedInfo.banned]!!.add(bannedInfo)
            } else {
                val list = ArrayList<BannedInfo>()
                list.add(bannedInfo)
                playerBannedInfos[bannedInfo.banned] = list
            }
        }
    }

    fun hasPlayerBanned(id: String) = this.playerBannedInfos.containsKey(id) && this.playerBannedInfos[id]!!.size != 0

    fun getPlayerBanneds(id: String) = this.playerBannedInfos[id]

    fun stillBanned(id: String): Boolean {
        this.getPlayerBanneds(id)?.forEach {
            if ((!it.revised) && it.endTime.after(Date())) {
                return true
            }
        }
        return false
    }

    fun hasUniteChat(id: String) = this.uniteChats.containsKey(id)

    fun getUniteChat(id: String) = this.uniteChats[id]

    fun addUniteChat(serverID: String, ucID: String) {
        if (this.uniteChats.containsKey(ucID)) {
            this.uniteChats[ucID]!!.add(serverID)
        } else {
            this.uniteChats[ucID] = object : CopyOnWriteArrayList<String>() {
                init {
                    this.add(serverID)
                }
            }
        }
    }

    fun hasMail(id: String) = this.mails.containsKey(id)

    fun addMail(mail: Mail) {
        this.mails[mail.id] = mail
    }

    fun getMail(id: String) = this.mails[id]

    fun addAllPlayer(id: String) = this.allPlayers.add(id)

    fun isDebug() = this.debug

    fun hasHubs() = this.hubs.size != 0

    fun getHub(id: Int): String? = this.hubs[id]

    fun addHub(id: String) {
        this.hubs.add(id)
    }

    private fun ping() {
        val pk = UpdateServerPerformanceData()
        pk.count = this.getPlayerCount()
        this.broadcastPacket(pk)
    }

    companion object {
        /**
         * APIFunction已实例化对象
         */
        @JvmStatic
        lateinit var instance: APIFunction
    }
}
