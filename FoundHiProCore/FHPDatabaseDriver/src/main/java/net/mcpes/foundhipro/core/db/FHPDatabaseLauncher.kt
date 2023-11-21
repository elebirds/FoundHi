package net.mcpes.foundhipro.core.db

import net.mcpes.foundhipro.core.ServerType
import net.mcpes.foundhipro.core.db.io.FHPBaseIO
import net.mcpes.foundhipro.core.db.io.FHPDebugIO
import net.mcpes.foundhipro.core.db.io.FHPMySQLIO
import net.mcpes.foundhipro.core.logger.Logger

/**
 * FoundHiPro
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class FHPDatabaseLauncher(val builder: FHPDatabaseLauncher.Builder) {
    var isLaunched = false //是否已经启动数据库驱动系统
    var useTestMode = true //使用测试模式
    lateinit var dao: FHPBaseIO
    var isAPI = false // 是否为API服务器

    init {
        useTestMode = builder.isTestMode()
        isAPI = builder.isAPI()
    }

    fun launch() {
        if (useTestMode) {
            dao = FHPDebugIO()
            Logger.instance.warn("FHPDatabaseDriver 将以Debug模式启动")
        } else {
            dao = FHPMySQLIO()
            Logger.instance.warn("FHPDatabaseDriver 将以MySQL模式启动")
        }
        dao.connect(builder,this)
        isLaunched = true
    }

    class Builder {
        private var useTestMode = false
        private var databaseConfigPath = System.getProperty("user.dir") + "/database.properties"
        private var isAPI = ServerType.type == ServerType.ServerType.API

        fun setConfigPath(path: String): Builder {
            databaseConfigPath = "$path/database.properties"
            return this
        }

        fun setDatabaseConfigPath(path: String): Builder {
            databaseConfigPath = path
            return this
        }

        fun setIsAPI(isAPI: Boolean): Builder {
            this.isAPI = isAPI
            return this
        }

        fun isAPI(): Boolean {
            return isAPI
        }

        fun getDatabaseConfigPath(): String {
            return databaseConfigPath
        }

        fun isTestMode(): Boolean {
            return useTestMode
        }

        fun build(): FHPDatabaseLauncher {
            return FHPDatabaseLauncher(this)
        }

        fun buildTest(): FHPDatabaseLauncher {
            this.useTestMode = true
            return FHPDatabaseLauncher(this)
        }
    }
}