package net.mcpes.foundhipro.core.db.io

import net.mcpes.foundhipro.core.db.FHPDatabaseLauncher
import net.mcpes.foundhipro.core.db.action.DatabaseAction
import net.mcpes.foundhipro.core.db.callback.DatabaseCallback
import net.mcpes.foundhipro.core.db.data.PlayerData
import net.mcpes.foundhipro.core.logger.Logger
import java.sql.Connection

/**
 * FoundHiPro
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class FHPDebugIO : FHPBaseIO {

    override fun connect(builder: FHPDatabaseLauncher.Builder,launcher: FHPDatabaseLauncher): Boolean {
        Logger.instance.info("成功连接以Debug模式连接不存在的数据库")
        return true
    }

    override fun getConnection(): Connection {
        return null!!
    }

    override fun isConnected(): Boolean {
        return true
    }

    override fun doAction(action: DatabaseAction,dbCallback: DatabaseCallback?) {

    }

    override fun doActionSync(action: DatabaseAction) {

    }

    override fun disconnect() {

    }

    override fun getPlayerData(player: String): PlayerData? {
        return PlayerData(player,10000,1000,1000,10,2333)
    }

}