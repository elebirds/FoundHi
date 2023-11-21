package net.mcpes.foundhi.plugin.game.has.utils

import java.util.*

object EntityUtils {
    val random = Random(System.currentTimeMillis())

    fun rand(min: Int, max: Int): Int {
        return if (min == max) max else min + random.nextInt(max - min)
    }

    fun rand(): Boolean {
        return random.nextBoolean()
    }
}