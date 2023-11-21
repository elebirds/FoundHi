package net.mcpes.foundhi.core.utils

import net.mcpes.foundhi.core.app.data.player.Player

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/28
 * version 1.0
 */

fun Int.getPlayerPermission() = when (this) {
    -1 -> Player.Permission.Ban
    1 -> Player.Permission.LOW_ADMIN
    2 -> Player.Permission.HIGH_ADMIN
    3 -> Player.Permission.OWNER
    4 -> Player.Permission.DEVELOPER
    else -> Player.Permission.PLAYER
}