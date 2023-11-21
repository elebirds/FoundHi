package net.mcpes.foundhipro.core.db.action.get

import net.mcpes.foundhipro.core.db.action.DatabaseAction
import net.mcpes.foundhipro.core.db.data.PlayerData
import net.mcpes.foundhipro.core.db.io.FHPBaseIO

/**
 * FoundHiPro
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class GetPlayerDataAction(private val playerName: String,baseDao: FHPBaseIO) : DatabaseAction(baseDao) {

    var playerData: PlayerData? = null

    override fun execute() {
        val ps = dao.getConnection().prepareStatement("SELECT * FROM players WHERE player = ?")
        ps.setString(1,playerName)
        val rs = ps.executeQuery()
        playerData = PlayerData(
                rs.getString("player"),
                rs.getInt("coin"),
                rs.getInt("emerald"),
                rs.getInt("diamond"),
                rs.getInt("level"),
                rs.getInt("exp"))
        rs.close()
        ps.close()
    }

}