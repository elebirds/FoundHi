package net.mcpes.foundhipro.core.db.io

import net.mcpes.foundhipro.core.db.FHPDatabaseLauncher
import net.mcpes.foundhipro.core.db.action.DatabaseAction
import net.mcpes.foundhipro.core.db.callback.DatabaseCallback
import net.mcpes.foundhipro.core.db.data.PlayerData
import java.sql.Connection

/**
 * FoundHiPro
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
interface FHPBaseIO {

    /**
     * 连接数据库
     */
    fun connect(builder: FHPDatabaseLauncher.Builder,launcher: FHPDatabaseLauncher): Boolean

    /**
     * 是否已经连接
     */
    fun isConnected(): Boolean

    fun getConnection(): Connection

    /**
     * 异步执行一个数据库操作
     * @param action 传入的实例Action
     * @param dbCallback 回调
     */
    fun doAction(action: DatabaseAction,dbCallback: DatabaseCallback?)

    /**
     * 同步执行一个的数据库操作
     * @param action 传入的实例Action
     */
    fun doActionSync(action: DatabaseAction)

    /**
     * 安全断开与数据库的连接
     */
    fun disconnect()

    /**
     * 获取玩家的数据
     * @param player 玩家名称
     */
    fun getPlayerData(player: String) : PlayerData?
}