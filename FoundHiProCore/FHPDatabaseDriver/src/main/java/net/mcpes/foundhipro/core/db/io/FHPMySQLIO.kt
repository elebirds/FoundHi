package net.mcpes.foundhipro.core.db.io

import com.alibaba.druid.pool.DruidDataSource
import com.alibaba.druid.pool.DruidDataSourceFactory
import net.mcpes.foundhipro.core.db.FHPDatabaseLauncher
import net.mcpes.foundhipro.core.db.action.DatabaseAction
import net.mcpes.foundhipro.core.db.action.get.GetPlayerDataAction
import net.mcpes.foundhipro.core.db.callback.DatabaseCallback
import net.mcpes.foundhipro.core.db.data.PlayerData
import net.mcpes.foundhipro.core.db.queue.SQLQueueThread
import net.mcpes.foundhipro.utils.file.FileOtherUtils
import java.io.File
import java.sql.Connection
import java.util.concurrent.ConcurrentLinkedDeque

/**
 * FoundHiPro
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class FHPMySQLIO : FHPBaseIO {

    private var source: DruidDataSource? = null

    var currentRequest = 0

    var maxRequest = 1

    val sqlQueue = ConcurrentLinkedDeque<Pair<DatabaseAction,DatabaseCallback?>>()

    lateinit var launcher: FHPDatabaseLauncher

    lateinit var sqlQueueThread: Thread

    override fun connect(builder: FHPDatabaseLauncher.Builder,launcher: FHPDatabaseLauncher): Boolean {
        this.launcher = launcher
        source = DruidDataSourceFactory.createDataSource(FileOtherUtils.readProperties(File(builder.getDatabaseConfigPath()))) as DruidDataSource
        sqlQueueThread = Thread(SQLQueueThread(this))
        sqlQueueThread.start()
        return isConnected()
    }

    override fun getConnection(): Connection {
        return source!!.connection
    }

    override fun isConnected(): Boolean {
        return source?.isEnable ?: false
    }

    override fun doAction(action: DatabaseAction,dbCallback: DatabaseCallback?) {
        sqlQueue.offer(Pair(action,dbCallback))
    }

    /**
     * @warning 此方法需要异步调用
     */
    override fun getPlayerData(player: String): PlayerData? {
        val action = GetPlayerDataAction(player,this)
        doActionSync(action)
        return action.playerData
    }

    /**
     * 同步没有回调一说 所以略去
     */
    override fun doActionSync(action: DatabaseAction) {
        action.execute()
    }

    override fun disconnect() {
        source?.close()
    }
}