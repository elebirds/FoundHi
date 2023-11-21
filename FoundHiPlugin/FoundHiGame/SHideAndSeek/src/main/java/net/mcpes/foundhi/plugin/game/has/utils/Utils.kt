package net.mcpes.foundhi.plugin.game.has.utils

import cn.nukkit.Server
import cn.nukkit.level.Location

object Utils {
    fun locationToString(pos: Location): String {
        return pos.x.toInt().toString() + ":" + pos.y.toInt().toString() + ":" + pos.z.toInt().toString() + ":" + pos.level.name.toString()
    }

    fun stringToLocation(pos: String): Location? {
        var a: List<String> = pos.split(":")
        if (a.size != 4) return null
        if (!Server.getInstance().isLevelLoaded(a[3])) return null
        return Location(a[0].toInt().toDouble(), a[0].toInt().toDouble(), a[2].toInt().toDouble(), Server.getInstance().getLevelByName(a[3]))
    }
}