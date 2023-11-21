package net.mcpes.foundhipro.core.app

import net.mcpes.foundhipro.core.app.bootstrap.FoundHiProCoreBootstrap
import net.mcpes.foundhipro.core.app.command.FHPCommandManager
import net.mcpes.foundhipro.core.app.config.FHPCoreMainConfiguration
import net.mcpes.foundhipro.core.app.lock.TickLock
import net.mcpes.foundhipro.core.app.logger.ApplicationLogger
import net.mcpes.foundhipro.core.app.network.ServerNetwork
import net.mcpes.foundhipro.core.app.scheduler.ServerScheduler
import net.mcpes.foundhipro.core.db.FHPDatabaseLauncher
import net.mcpes.foundhipro.core.logger.Logger
import net.mcpes.foundhipro.utils.file.FileOtherUtils
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicBoolean

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/8/31 下午4:04
 * version 1.0
 */
class CoreServer(jarPath: String, dataPath: String, configPath: String, val ansiEnabled: Boolean) {
    val jarPath: Path = Paths.get(jarPath)
    val dataPath: Path = Paths.get(dataPath)
    val configPath: Path = Paths.get(configPath)
    val logger: Logger = Logger(ApplicationLogger(configPath))
    lateinit var config: FHPCoreMainConfiguration
    lateinit var network: ServerNetwork
    lateinit var databaseLauncher: FHPDatabaseLauncher
    lateinit var scheduler: ServerScheduler
        private set

    var isRunning = AtomicBoolean(true)

    fun boot() {
        this.saveConfig("database/database.properties", "database.properties")
        this.saveConfig("FHPCoreMain.yml")
        this.config = FHPCoreMainConfiguration(this)
        this.scheduler = ServerScheduler()
        this.network = ServerNetwork()
        this.databaseLauncher = FHPDatabaseLauncher
                .Builder()
                .setConfigPath(this.configPath.toFile().absolutePath)
                .setIsAPI(true)
                .build()
        this.databaseLauncher.launch()
        FHPCommandManager.boot()
        if (this.config.debug) {
            this.logger.trace("FoundHiProCore 以调试模式启动")
        }
        this.logger.info("此程序正在运行 FoundHi §e${FoundHiProCoreBootstrap.VERSION} §c版本\"§b${FoundHiProCoreBootstrap.CODENAME}\"")
        this.logger.info("${FoundHiProCoreBootstrap.TITLE} §6${FoundHiProCoreBootstrap.VERSION}§a开§c启§d成§e功!")
        this.logger.info("启动完成（${((System.currentTimeMillis() - FoundHiProCoreBootstrap.START_TIME).toDouble() / 1000)}s）!")
        this.loop()
    }

    private fun loop() {
        val lock = TickLock()
        while (isRunning.get()) {
            if (lock.loop()) {
                this.scheduler.mainThreadHeartbeat(lock.tick)
            } else {
                lock.lock()
            }
        }
        this.shutdown()
    }

    fun shutdown() {
        this.isRunning.set(false)
        this.scheduler.cancelAllTasks()
        this.scheduler.mainThreadHeartbeat(Int.MAX_VALUE)
        System.exit(0)
    }

    fun kill() {

    }

    @JvmOverloads
    fun saveResources(filename: String, outputName: String = filename, replace: Boolean = false) = FileOtherUtils.saveResource(this.dataPath.toFile().absolutePath, filename, outputName, replace, this.javaClass.classLoader)

    @JvmOverloads
    fun saveConfig(filename: String, outputName: String = filename, replace: Boolean = false) = FileOtherUtils.saveResource(this.configPath.toFile().absolutePath, filename, outputName, replace, this.javaClass.classLoader)

    init {
        instance = this
    }

    companion object {
        lateinit var instance: CoreServer
    }
}