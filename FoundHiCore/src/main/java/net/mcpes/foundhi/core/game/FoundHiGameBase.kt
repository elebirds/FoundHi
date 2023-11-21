package net.mcpes.foundhi.core.game

import net.mcpes.foundhi.core.plugin.modal.FoundHiPluginBase

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/6
 * version 1.0
 */
abstract class FoundHiGameBase(val type: GameType) : FoundHiPluginBase() {
    abstract fun loadData()


}