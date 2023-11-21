package net.mcpes.foundhi.core.game.`fun`

import cn.nukkit.Player

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/9
 * version 1.0
 */
abstract class GameFunction {
    abstract fun onJoin(player: Player)

    abstract fun onQuit(player: Player)

    abstract fun onStart()

    abstract fun onStop()
}