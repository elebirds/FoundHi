package net.mcpes.foundhi.core.game

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/9
 * version 1.0
 */
enum class GameType(val id: Int, name: String, val code: String) {
    BEDWARS(1, "起床战争", "bw"), SKYWARS(2, "空岛战争", "sk");

    val gname: String = name
}