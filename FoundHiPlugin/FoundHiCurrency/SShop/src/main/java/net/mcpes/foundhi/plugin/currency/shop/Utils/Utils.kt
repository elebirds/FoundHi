package net.mcpes.foundhi.plugin.currency.shop.utils

import cn.nukkit.Server
import cn.nukkit.level.Location
import net.mcpes.foundhi.plugin.currency.shop.SShopMain

/**
 * FoundHi
 *
 * @author yui Copyright (c) 2018/5/21
 * version 1.0
 */
object Utils {
    fun locationToString(pos: Location): String {
        return pos.x.toInt().toString() + ":" + pos.y.toInt().toString() + ":" + pos.z.toInt().toString() + ":" + pos.level.name.toString()
    }

    fun stringToLocation(pos: String): Location? {
        val a: List<String> = pos.split(":")
        if (a.size != 4) return null
        if (!Server.getInstance().isLevelLoaded(a[3])) return null
        return Location(a[0].toInt().toDouble(), a[0].toInt().toDouble(), a[2].toInt().toDouble(), Server.getInstance().getLevelByName(a[3]))
    }

    fun getMessage(message: String): String {
        return SShopMain.TITLE + message
    }
}